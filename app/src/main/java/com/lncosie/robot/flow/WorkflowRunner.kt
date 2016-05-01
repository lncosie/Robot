package com.lncosie.robot.flow

import com.lncosie.robot.task.Task
import com.lncosie.toolkit.log
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created by lncosie on 2016/4/29.
 */
class WorkflowRunner(val envirment: Envirment) {
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var current: Node
    private @Volatile var busy = true
    private var timeTaskStart=0L
    fun start(start: Node) {
        current = start
        switchTask(Task.Direction.Forward)
        busy = false
    }

    fun stop(finish: Node) {
        busy = true
        Thread.sleep(500)
        finish.task.start(envirment)
        finish.task.end(envirment)
    }

    private fun taskStart() {
        timeTaskStart=System.currentTimeMillis()
        current.task.LimitRun {
            start(envirment)
        }
    }

    private fun endTask() {
        current.task.LimitRun {
            end(envirment)
        }
    }

    private fun switchTask(dir: Task.Direction) {
        if (dir == Task.Direction.Waiting)
            return
        do {
            endTask()
            log("Switch Node:${if (dir == Task.Direction.Forward) "Forward" else "Backward"}")
            current = if (dir == Task.Direction.Forward) current.forward.value else current.backward.value
            taskStart()
        }while(current.task.isPassive())
    }

    fun step(event: Event) {
        log(event.event.toString())
        if (busy)
            return
        var dir = Task.Direction.Waiting
        try {
            var dir = current.task.LimitStep { step(event, envirment) }
        } catch(e: Exception) {
            dir = Task.Direction.Back
        } finally {
            switchTask(dir)
        }
    }

    fun Task.LimitStep(fn: Task.() -> Task.Direction): Task.Direction {
        log("Step start:" + current.toString())
        try {
            return executor.submit<Task.Direction> {
                this.fn()
            }.get(this.timeout()+timeTaskStart-System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        } catch(e: TimeoutException) {
            log("Timeout step:" + this.toString())
            throw e
        } finally {
            log("Step end:" + current.toString())
        }
    }

    fun Task.LimitRun(fn: Task.() -> Unit): Unit {
        log("Run start:" + current.toString())
        try {
            busy = true
            executor.submit<Unit> {
                this.fn()
            }.get(this.timeout()+timeTaskStart-System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        } catch(e: TimeoutException) {
            log("Run timeout:" + this.toString())
            throw e
        } finally {
            log("Run end:" + this.toString())
            busy = false
        }
    }
}