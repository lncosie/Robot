package com.lncosie.robot.task

import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by guazi on 2016/5/5.
 */

abstract open class TaskInitiative : Task {
    override fun step(event: Event, env: Envirment): Task.Direction = Task.Direction.Forward
    override fun end(env: Envirment) {
    }
    override fun isPassive()=false
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}