package com.lncosie.robot.task.unuse

import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.task.TaskInitiative
import com.lncosie.robot.task.WechatId
import com.lncosie.toolkit.shell.Shell

/**
 * Created by lncosie on 2016/5/2.
 */




class TaskReset : TaskInitiative(){
    override fun start(env: Envirment): Unit {
        env.usernick =""
        env.msg= WechatId.welcome
    }
}
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