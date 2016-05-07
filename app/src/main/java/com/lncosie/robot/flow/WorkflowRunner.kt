package com.lncosie.robot.flow

import com.lncosie.robot.Config.RobotConfig
import com.lncosie.robot.Config.WechatId
import com.lncosie.robot.task.Task
import com.lncosie.toolkit.Logger

import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.concurrent.thread

/**
 * Created by lncosie on 2016/5/2.
 */
class WorkflowRunner(val envirment: Envirment) {
    private val executor = Executors.newCachedThreadPool()
    private @Volatile var current: Node? = null
    private @Volatile var dir: Task.Direction = Task.Direction.Forward


    private var stepEnded = Object()
    private var mt:Thread?=null
    fun start(start: Node) {
        envirment.msg=RobotConfig.welcome
        stepEnded = Object()
        mt=thread {
            run(start)
        }
    }

    fun stop() {
        mt?.interrupt()
        current = null
    }

    fun step(event: Event) {
        if (current != null) {
            if (current!!.task.isPassive()&&dir===Task.Direction.Waiting) {
                Logger.log(current!!.descript(envirment.usernick))
                dir = current!!.task.step(event, envirment)
                Logger.log( current!!.descript(envirment.usernick))
                if (dir != Task.Direction.Waiting) {
                    synchronized(stepEnded) {
                        stepEnded.notify()
                    }
                }
            }
        }
    }

    fun run(start: Node) {
        dir= Task.Direction.Forward
        current = start
        while (true) {
            if (current == null) {
                return
            }
            val now=current!!
            var worker = executor.submit<Unit> {
                try {
                    Logger.log(now.descript(envirment.usernick))
                    now.task.start(envirment)
                    if (now.task.isPassive()) {
                        dir = Task.Direction.Waiting
                        //stop_rcv = false
                        synchronized(stepEnded) {
                           // while(dir=== Task.Direction.Waiting)
                           stepEnded.wait()
                        }
                    } else {
                        dir = Task.Direction.Forward
                    }
                    now.task.end(envirment)
                    Logger.log(now.descript(envirment.usernick))
                } catch(time: TimeoutException) {
                    dir= Task.Direction.Back
                } catch(e: Exception) {
                    dir= Task.Direction.Back
                } finally {

                }
            }
            //Logger.log("Switch:" + dir.toString())
            try{
                worker?.get(now.task.timeout(), TimeUnit.MILLISECONDS)
            }catch(to:TimeoutException){
                dir= Task.Direction.Back
            }catch(stop:InterruptedException){
                dir= Task.Direction.Waiting
                worker?.cancel(true)
                mt=null
                current=null
                return
            }catch(np:Exception){
                dir= Task.Direction.Back
            }finally{
                when (dir) {
                    Task.Direction.Forward -> current = now.forward.value
                    Task.Direction.Back -> current = now.backward.value
                }
            }
        }
    }
}