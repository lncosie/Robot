package com.lncosie.robot.flow

import android.content.Context
import android.view.accessibility.AccessibilityEvent
import com.lncosie.wxrobot.RobotService

/**
 * Created by lncosie on 2016/5/2.
 */
data class Event(val service: RobotService, val event: AccessibilityEvent)
data class Envirment(
        var msg:String,
        var usernick:String,
        var userid:String,
        var upcode:String,
        var upload:String,
        var todo:String?,//user
        val context: Context)