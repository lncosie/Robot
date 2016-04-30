package com.lncosie.robot.task

import com.lncosie.robot.flow.Envirment

/**
 * Created by lncosie on 2016/4/30.
 */
class LogOpenUser:Task{
    override fun start(env: Envirment): Boolean {
        return true
    }

}
class LogCloseUser:Task{
    override fun start(env: Envirment): Boolean {
        return true
    }
}