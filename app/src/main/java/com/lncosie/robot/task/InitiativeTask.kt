package com.lncosie.robot.task

import android.content.Intent
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event
import com.lncosie.toolkit.shell.Shell

/**
 * Created by lncosie on 2016/4/30.
 */

abstract open class InitiativeTask : Task{
    override fun step(event: Event, env: Envirment): Task.Direction = Task.Direction.Forward
    override fun end(env: Envirment) {
    }
    override fun isPassive()=false
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
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
class Nop:InitiativeTask(){
    override fun start(env: Envirment) {
    }
}