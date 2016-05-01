package com.lncosie.robot.task

import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by lncosie on 2016/4/29.
 */

interface Task{
    enum class Direction{
        Forward,Back,Waiting
    }
    fun start(env: Envirment): Unit
    fun step(event: Event, env: Envirment): Direction = Direction.Forward
    fun end(env: Envirment){}
    fun timeout():Long=3000
    fun isPassive():Boolean
    fun wait_moment(ms:Long){
        Thread.sleep(ms)
    }
}
abstract open class PasssiveTask : Task {
    override fun start(env: Envirment) {}
    override fun end(env: Envirment){}
    override fun isPassive()=true

}
abstract open class InitiativeTask : Task{
    override fun step(event: Event, env: Envirment): Task.Direction = Task.Direction.Forward
    override fun end(env: Envirment) {
    }
    override fun isPassive()=false
}