package com.lncosie.robot.flow

import com.lncosie.robot.task.Task
import com.lncosie.toolkit.log
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.concurrent.timerTask

/**
 * Created by lncosie on 2016/4/29.
 */
class FlowRunner(val envirment: Envirment) {
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var node: Node
    private @Volatile var busy = true

    private var future: Future<*>? = null

    fun start(start: Node) {
        node = start
        step_start()
        busy = false
    }

    fun stop(finish: Node) {
        busy = true
        future?.cancel(true)
        Thread.sleep(500)
        finish.task.start(envirment)
        finish.task.end(envirment)
    }
    private fun step_start() {
        while (node.task.LimitRun {
            log("Start:" + node.toString())
            start(envirment)
        }) {
            step_end()
            node = node.forward.value
        }
    }

    private fun step_end() {
        node.task.LimitRun { end(envirment);true }
        log("End:" + node.toString())
    }

    private fun switch(dir: Task.Direction) {
        if (dir == Task.Direction.Waiting)
            return
        future = executor.submit {
            busy = true
            step_end()
            log("Switch Node")
            node = if (dir == Task.Direction.Forward) node.forward.value else node.backward.value
            step_start()
            busy = false
        }
        future?.get()
        future = null
    }

    fun step(event: Event) {
        log(event.event.eventType.toString())
        if (busy)
            return
        var dir = Task.Direction.Waiting
        try {
            log("Step start:" + node.toString())
            var dir = node.task.LimitStep { run(event, envirment) }
            log("Step end:" + node.toString())
        } catch(e: Exception) {
            dir = Task.Direction.Back
        } finally {
            switch(dir)
        }
    }

    fun Task.LimitStep(fn: Task.() -> Task.Direction): Task.Direction {
        try {
            return executor.submit<Task.Direction> {  this.fn()}.get(this.timeout(), TimeUnit.MILLISECONDS)
        } catch(e:TimeoutException){
            log("Timeout step:"+this.toString())
            return Task.Direction.Back
        }finally {

        }
    }

    fun Task.LimitRun(fn: Task.() -> Boolean): Boolean {
        try {
            busy=true
            return executor.submit<Boolean> {  this.fn()}.get(this.timeout(), TimeUnit.MILLISECONDS)
        } catch(e:TimeoutException){
            log("Timeout run:"+this.toString())
            return true
        }finally {
            busy=false
        }
    }
}