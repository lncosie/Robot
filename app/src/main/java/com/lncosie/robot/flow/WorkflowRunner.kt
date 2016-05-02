package com.lncosie.robot.flow

import com.lncosie.robot.task.Task
import com.lncosie.toolkit.log
import com.lncosie.toolkit.loge
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created by lncosie on 2016/4/29.
 */
class WorkflowRunner(val envirment: Envirment) {
    private val executor = Executors.newCachedThreadPool()
    private @Volatile lateinit var current: Node
    private @Volatile var busy = true
    private var timeTaskStart=0L
    fun start(start: Node) {
        current = start
        timeTaskStart=System.currentTimeMillis()
        switchTask(Task.Direction.Forward)
    }

    fun stop(finish: Node) {
        busy = true
        current=finish
        taskStart()
        taskEnd()
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
        executor.submit {
            do {
                taskEnd()
                log("Switch Node:${if (dir == Task.Direction.Forward) "Forward" else "Backward"}")
                current = if (dir == Task.Direction.Forward) current.forward.value else current.backward.value
                taskStart()
                if(current.task.isPassive())
                    break
            } while (true)
            busy=false
        }
    }

    fun step(event: Event) {
        log(event.event.toString())
        if (busy||!current.task.isPassive())
            return
        var dir = Task.Direction.Waiting
        try {
            dir = current.task.LimitStep { step(event, envirment) }
        } finally {
            switchTask(dir)
        }
    }

    fun Task.LimitStep(fn: Task.() -> Task.Direction): Task.Direction {
        log("Step start:" + current.descript())
        var work:Future<Task.Direction>?=null
        try {
            work=executor.submit<Task.Direction> {
                try{
                    this.fn()
                }catch(e:Exception){
                    return@submit Task.Direction.Back
                }
            }
            return work.get(this.timeout()+timeTaskStart-System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        } catch(e: TimeoutException) {
            loge("Step Timeout:" + current.descript())
            work?.cancel(true)
            return Task.Direction.Back
        } catch(e:Exception){
            loge("Step Error:" + e.message)
            work?.cancel(true)
            return Task.Direction.Back
        }finally {
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
        } catch(e:Exception){
            loge("Run Error:" + e.message)
            work?.cancel(true)
        }finally {
            log("Run End:" + current.descript())
            busy = false
        }
    }
}