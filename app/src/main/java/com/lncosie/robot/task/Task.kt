package com.lncosie.robot.task

import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by lncosie on 2016/5/2.
 */

interface Task{
    enum class Direction{
        Forward,Back,Waiting
    }
    fun start(env: Envirment): Unit
    fun step(event: Event, env: Envirment): Direction = Direction.Forward
    fun end(env: Envirment){}
    fun timeout():Long=600000
    fun isPassive():Boolean
    fun wait_moment(ms:Long): Unit {
        Thread.sleep(ms)
    }

}
