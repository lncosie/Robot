package com.lncosie.robot.task

import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by guazi on 2016/5/5.
 */
class TaskPerformClick(val key:String, val isId:Boolean=true) : TaskPasssive() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event,key,isId){
            it.click()
            Task.Direction.Forward
        }
    }
}