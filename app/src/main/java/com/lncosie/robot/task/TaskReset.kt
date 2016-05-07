package com.lncosie.robot.task

import com.lncosie.robot.Config.RobotConfig
import com.lncosie.robot.flow.Envirment

/**
 * Created by guazi on 2016/5/7.
 */
class TaskReset : TaskInitiative(){
    override fun start(env: Envirment): Unit {
        env.usernick =""
        env.msg= RobotConfig.welcome
    }
}