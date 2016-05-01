package com.lncosie.robot.flow

import android.content.Context
import android.view.accessibility.AccessibilityEvent
import com.lncosie.wxrobot.RobotService

/**
 * Created by lncosie on 2016/4/29.
 */
data class Event(val service: RobotService, val event: AccessibilityEvent)
data class Envirment(var user:String,
                     var msg:String,
                     var wxid:String,
                     val context: Context)