package com.lncosie.robot.unuse

import com.lncosie.robot.Config.RobotConfig
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.task.TaskInitiative
import com.lncosie.robot.Config.WechatId
import com.lncosie.toolkit.shell.Shell

/**
 * Created by lncosie on 2016/5/2.
 */





class FriendOpen: TaskInitiative(){
    override fun start(env: Envirment): Unit {
        Shell.post("am start "+ WechatId.WechatApp+ WechatId.WechatNewFriend)
        wait_moment(1000)
    }
}


class RunFinish : TaskInitiative(){
    override fun start(env: Envirment): Unit {

    }
}
class Nop: TaskInitiative(){
    override fun start(env: Envirment) {
    }
}