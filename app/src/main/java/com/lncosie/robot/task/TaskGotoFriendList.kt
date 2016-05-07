package com.lncosie.robot.task

import android.accessibilityservice.AccessibilityService
import com.lncosie.robot.Config.WechatId
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by guazi on 2016/5/5.
 */
class TaskGotoFriendList : TaskPasssive() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        //Goto Page2 通讯录
        var found = runOnNode(event, WechatId.IDHome4ButtonName, true) {
            if (it.text.toString().equals(WechatId.HomeFriendList)) {
                it.click()
                return@runOnNode Task.Direction.Forward
            }
            Task.Direction.Waiting
        }
        if (found == Task.Direction.Forward) return found
        return Task.Direction.Waiting
    }
}