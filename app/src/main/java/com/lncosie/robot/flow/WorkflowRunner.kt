package com.lncosie.robot.flow

import com.lncosie.robot.Config.RobotConfig
import com.lncosie.robot.task.Task
import com.lncosie.toolkit.Logger
import java.util.concurrent.Executors
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


    private var dirChange = Object()
    private var taskChange = Object()
    private var mt: Thread? = null
    fun start(start: Node) {
        envirment.msg = RobotConfig.welcome
        mt = thread {
            run(start)
        }
    }

    fun stop() {
        mt?.interrupt()
        current = null
    }

    fun step(event: Event) {
        if (current != null) {
            val now=current!!
            if (now.task.isPassive()) {

                Logger.log(now.descript(envirment.usernick))
                var i = now.task.step(event, envirment)
                Logger.log(now.descript(envirment.usernick))
                Logger.logi(event.event.eventType.toString())
                if (i != Task.Direction.Waiting) {
                    synchronized(dirChange) {
                        dirChange.notifyAll()
                    }
                    synchronized(taskChange){
                        taskChange.wait()
                    }
                }
            }
        }
    }

    fun run(start: Node) {
        dir = Task.Direction.Forward
        current = start
        while (true) {
            if (current == null) {
                return
            }
            val now = current!!
            var worker = executor.submit<Unit> {
                try {
                    Logger.log(now.descript(envirment.usernick))
                    now.task.start(envirment)
                    synchronized(taskChange){
                        taskChange.notifyAll()
                    }

                    if (now.task.isPassive()) {
                        synchronized(dirChange) {
                            dirChange.wait()
                        }
                    }
                    now.task.end(envirment)
                    dir= Task.Direction.Forward
                    Logger.log(now.descript(envirment.usernick))
                } catch(time: TimeoutException) {
                } catch(e: Exception) {
                    dir = Task.Direction.Back
                } finally {

                }
            }
            //Logger.log("Switch:" + dir.toString())
            try {
                worker?.get(now.task.timeout(), TimeUnit.MILLISECONDS)
            } catch(to: TimeoutException) {
                dir = Task.Direction.Back
            } catch(stop: InterruptedException) {
                dir = Task.Direction.Waiting
                worker?.cancel(true)
                mt = null
                current = null
                return
            } catch(np: Exception) {
                dir = Task.Direction.Back
            } finally {
                when (dir) {
                    Task.Direction.Forward -> current = now.forward.value
                    Task.Direction.Back -> current = now.backward.value
                }
            }
        }
    }
}