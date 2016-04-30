package com.lncosie.robot.task

import android.content.Intent
import com.lncosie.robot.flow.Envirment
import com.lncosie.toolkit.shell.Shell

/**
 * Created by lncosie on 2016/4/30.
 */
class WxOpen :AutoDriveTask(){
    override fun start(env: Envirment): Boolean {
        Shell.post("am start "+WechatId.WechatApp+WechatId.Home)
        wait_moment(1000)
        return true
    }
}
class WxClose :AutoDriveTask(){
    override fun start(env: Envirment): Boolean {
        Shell.post("am kill "+WechatId.WechatApp)
        wait_moment(1000)
        return true
    }
}
class FriendOpen:AutoDriveTask(){
    override fun start(env: Envirment): Boolean {
        Shell.post("am start "+WechatId.WechatApp+WechatId.WechatNewFriend)
        wait_moment(1000)
        return true
    }
}
class RunFinish :AutoDriveTask(){
    override fun start(env: Envirment): Boolean {
        return true
    }
}
