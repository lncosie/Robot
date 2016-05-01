package com.lncosie.robot.task

import android.content.Intent
import com.lncosie.robot.flow.Envirment
import com.lncosie.toolkit.shell.Shell

/**
 * Created by lncosie on 2016/4/30.
 */
class WxOpen : InitiativeTask(){
    override fun start(env: Envirment): Unit {
        Shell.post("am start "+WechatId.WechatApp+WechatId.Home)
        wait_moment(1000)
    }
}
class WxClose : InitiativeTask(){
    override fun start(env: Envirment): Unit {
        Shell.post("am kill "+WechatId.WechatApp)
        wait_moment(1000)
    }
}
class FriendOpen: InitiativeTask(){
    override fun start(env: Envirment): Unit {
        Shell.post("am start "+WechatId.WechatApp+WechatId.WechatNewFriend)
        wait_moment(1000)
    }
}
class RunFinish : InitiativeTask(){
    override fun start(env: Envirment): Unit {
    }
}
