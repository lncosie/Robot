package com.lncosie.robot.task

import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by lncosie on 2016/4/30.
 */
object WechatId{
    final val Accept="Accept"
    final val Photo="Photo"
    final val Send="Send"

    final val NickName="must set to NickName of user at runtime"

    final val ID_SendEdit="com.aa.mm/"
    final val ID_PhotoView ="com.aa.mm/"
    final val ID_PhotoEnd ="com.aa.mm"

    //am start -n yourpackagename/.activityname
    //am kill com.google.android.contacts

    final val WechatApp=" com.tencent.mm"
    final val WechatNewFriend="/com.tencent.mm.ui.LauncherUI"
    final val Home="/com.tencent.mm.ui.LauncherUI"
}
