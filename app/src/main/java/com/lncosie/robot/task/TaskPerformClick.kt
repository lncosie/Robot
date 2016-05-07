package com.lncosie.robot.task

import com.lncosie.robot.Config.RobotConfig
import com.lncosie.robot.Config.TimeoutConfig
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by guazi on 2016/5/5.
 */
class TaskPerformClick(val key:String, val isId:Boolean=true) : TaskPasssive() {
    override fun timeout(): Long {
        return TimeoutConfig.TimeOut_Click
    }
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event,key,isId){
            it.click()
            Task.Direction.Forward
        }
    }
}