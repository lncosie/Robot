package com.lncosie.robot.task

import com.lncosie.robot.Config.WechatId
import com.lncosie.robot.flow.Envirment
import com.lncosie.toolkit.shell.Shell

/**
 * Created by guazi on 2016/5/5.
 */
class TaskOpenNewFriends : TaskInitiative() {
    override fun start(env: Envirment): Unit {
        Shell.post("am start " + WechatId.WechatApp + WechatId.WechatNewFriend)
    }
}