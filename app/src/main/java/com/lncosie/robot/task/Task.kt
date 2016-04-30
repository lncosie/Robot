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
    fun start(env: Envirment):Boolean
    fun run(event: Event, env: Envirment): Direction = Direction.Forward
    fun end(env: Envirment){}
    fun timeout():Long=3000
    fun wait_moment(ms:Long){
        Thread.sleep(ms)
    }
}
abstract open class EventDriveTask: Task {
    override fun start(env: Envirment): Boolean=false
    override fun end(env: Envirment){}


}
abstract open class AutoDriveTask: Task{
    override fun run(event: Event, env: Envirment): Task.Direction = Task.Direction.Forward
    override fun end(env: Envirment) {
    }

}