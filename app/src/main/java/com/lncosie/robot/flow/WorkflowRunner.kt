package com.lncosie.robot.flow

import com.lncosie.robot.task.Task
import com.lncosie.toolkit.log
import com.lncosie.toolkit.loge
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created by lncosie on 2016/5/2.
 */
class WorkflowRunner(val envirment: Envirment) {
    private val executor = Executors.newCachedThreadPool()
    private @Volatile lateinit var current: Node
    private @Volatile var busy = true
    private var timeTaskStart=0L
    private var mainLoop:Future<Unit>?=null
    fun start(start: Node) {
        mainLoop?.cancel(true)
        current = start
        timeTaskStart=System.currentTimeMillis()
        switchTask(Task.Direction.Forward)
    }

    fun stop(finish: Node) {
        busy = true
        mainLoop?.cancel(true)
        mainLoop=null

    }

    private fun taskStart() {
        timeTaskStart=System.currentTimeMillis()
        current.task.LimitRun {
            start(envirment)
        }
    }

    private fun taskEnd() {
        current.task.LimitRun {
            end(envirment)
        }
    }

    private fun switchTask(dir: Task.Direction) {
        if (dir == Task.Direction.Waiting)
            return
        mainLoop=executor.submit<Unit> {
            try{
                do {
                    taskEnd()
                    log("Switch Node:${if (dir == Task.Direction.Forward) "Forward" else "Backward"}")
                    current = if (dir == Task.Direction.Forward) current.forward.value else current.backward.value
                    taskStart()
                    if(current.task.isPassive()){
                        break
                    }
                } while (true)

            }finally{
                busy=false
            }

        }
    }

    fun step(event: Event) {
        log(event.event.toString())
        if (busy||!current.task.isPassive())
            return
        var dir = Task.Direction.Waiting
        try {
            dir = current.task.LimitStep {
                step(event, envirment)
            }
            switchTask(dir)
        }catch(stop:NullPointerException){
            busy=true
        }catch(e:Exception){
            dir= Task.Direction.Back
            switchTask(dir)
        } finally {

        }
    }

    fun Task.LimitStep(fn: Task.() -> Task.Direction): Task.Direction {
        log("Step start:" + current.descript())
        var work:Future<Task.Direction>?=null
        try {
            work=executor.submit<Task.Direction> {
                try{
                    this.fn()
                }
                catch(e:Exception){
                    e.printStackTrace()
                    log(e.toString())
                    return@submit Task.Direction.Back
                }
                finally{

                }

            }
            return work.get(this.timeout()+timeTaskStart-System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        } catch(e: TimeoutException) {
            loge("Step Timeout:" + current.descript())
            work?.cancel(true)
            return Task.Direction.Back
        }
        catch(e:Exception){
            e.printStackTrace()
            loge("Step Error:" + e.toString())
            work?.cancel(true)
            return Task.Direction.Back
        }
        finally {
            log("Step End:" + current.descript())
        }
    }

    fun Task.LimitRun(fn: Task.() -> Unit): Unit {
        log("Run start:" + current.descript())
        var work:Future<Unit>?=null
        try {
            busy = true
            work=executor.submit<Unit> {
                try{
                    this.fn()
                }catch(e:Exception){
                }
            }
            work.get(this.timeout()+timeTaskStart-System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        } catch(e: TimeoutException) {
            loge("Run Timeout:" + current.descript())
            work?.cancel(true)
        }
        catch(e:Exception){
            e.printStackTrace()
            loge("Run Error:" + e.toString())
            work?.cancel(true)
        }
        finally {
            log("Run End:" + current.descript())
            busy = false
        }
    }
}