package com.lncosie.robot.flow

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
    private @Volatile var stop_rcv = true
    private val stepEnded = Object()


    fun start(start: Node) {
        thread {
            run(start)
        }
    }

    fun stop() {
        current = null
    }

    fun step(event: Event) {
        if (stop_rcv)
            return
        if (current != null) {
            if (current!!.task.isPassive()) {
                Logger.log("Step:" + envirment.usernick + current!!.descript())
                dir = current!!.task.step(event, envirment)
                Logger.log("Step:" + envirment.usernick + current!!.descript())
                if (dir != Task.Direction.Waiting) {
                    stop_rcv = true
                    synchronized(stepEnded) {
                        stepEnded.notify()
                    }
                }
            }
        }
    }

    fun run(start: Node) {
        current = start
        var worker: Future<Unit>? = null

        while (true) {
            if (current == null) {
                return
            }
            worker = executor.submit<Unit> {
                try {
                    Logger.log("Runs:" + envirment.usernick + current!!.descript())
                    current!!.task.start(envirment)
                    if (current!!.task.isPassive()) {
                        dir = Task.Direction.Waiting
                        stop_rcv = false
                        synchronized(stepEnded) {
                            stepEnded.wait()
                        }
                    } else {
                        dir = Task.Direction.Forward
                    }
                    current!!.task.end(envirment)
                    Logger.log("Runs:" + envirment.usernick + current!!.descript())
                } catch(time: TimeoutException) {
                    dir= Task.Direction.Back
                } catch(e: NoElementFound) {
                    dir= Task.Direction.Back
                    return@submit
                } finally {
                }
            }
            worker.get(current!!.task.timeout(), TimeUnit.MILLISECONDS)
            Logger.log("Switch:" + dir.toString())
            try{
                when (dir) {
                    Task.Direction.Forward -> current = current!!.forward.value
                    Task.Direction.Back -> current = current!!.backward.value
                }
            }catch(np:NullPointerException){
                Logger.log("NotPointer stop")
                current=null
            }
        }
    }
}