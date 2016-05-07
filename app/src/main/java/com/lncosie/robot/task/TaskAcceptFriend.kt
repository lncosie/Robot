package com.lncosie.robot.task

import com.lncosie.robot.Config.TimeoutConfig
import com.lncosie.robot.Config.WechatId
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by guazi on 2016/5/5.
 */

class TaskAcceptFriend() : TaskPasssive() {
    override fun timeout(): Long {
        return TimeoutConfig.TimeOut_AcceptNewFriend
    }
    override fun step(event: Event, env: Envirment): Task.Direction {
        val inDel = event.event.getClassName()?.toString()?.contains("com.tencent.mm.ui.base.k") ?: false
        //delete accepted
        if (inDel) {
            return runOnNode(event, WechatId.IDDelInMenu, false) {
                it.click()
                Task.Direction.Waiting
            }
        }
        //WechatId.IDHaveAccept
        //accept new IDAccept


        val found=runOnNode(event, WechatId.IDHaveAccept, true) {
            it.longClick()
            Task.Direction.Forward
        }
        if(found== Task.Direction.Forward)
            return Task.Direction.Waiting
        return runOnNode(event, WechatId.IDAccept, true) {
            it.click()
            Task.Direction.Forward
        }


    }
}