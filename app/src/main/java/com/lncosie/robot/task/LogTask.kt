package com.lncosie.robot.task

import com.lncosie.robot.flow.Envirment

/**
 * Created by lncosie on 2016/5/2.
 */
class LogOpenUser:InitiativeTask(){

    override fun start(env: Envirment): Unit {

    }

}
class LogCloseUser:InitiativeTask(){
    override fun isPassive()=false
    override fun start(env: Envirment): Unit {

    }
}