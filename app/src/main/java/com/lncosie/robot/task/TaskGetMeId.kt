package com.lncosie.robot.task

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.Config.RobotConfig
import com.lncosie.robot.Config.WechatId
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by guazi on 2016/5/6.
 */
class TaskGetMeId:TaskPasssive(){
    override fun step(event: Event, env: Envirment): Task.Direction {
        //In page 4
        //微信号：wuqi660016
        var found = runOnNode(event, WechatId.IDMePageMeId, true) {
            RobotConfig.wxid=it.text.toString().substring(4)
            return@runOnNode Task.Direction.Forward
        }
        if (found == Task.Direction.Forward) return found

        //Goto Page4 Me
        found = runOnNode(event, WechatId.IDHome4ButtonName, true) {
            if (it.text.toString().equals(WechatId.HomeMe)) {
                it.click()
            }
            Task.Direction.Waiting
        }
        //if (found == Task.Direction.Forward) return found
        //event.service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
        return Task.Direction.Waiting

    }

}