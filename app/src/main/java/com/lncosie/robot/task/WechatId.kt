package com.lncosie.robot.task

import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by lncosie on 2016/4/30.
 */
object WechatId{


    final val NickName="must set to NickName of user at runtime"



    //am start -n yourpackagename/.activityname
    //am kill com.google.android.contacts

    final val WechatApp=" com.tencent.mm"
    final val WechatNewFriend="/com.tencent.mm.ui.LauncherUI"
    final val Home="/com.tencent.mm.ui.LauncherUI"


    final val IDHome4ButtonName="com.tencent.mm:id/id"
    final val IDHomeHistoryNickname="com.tencent.mm:id/cc"

    //me page
    final val IDMePageSelfName="com.tencent.mm:id/c46"

    //friend list page
    final val IDFriendListNickname="com.tencent.mm:id/b5"
    final val IDFriendListNewFriends="com.tencent.mm:id/kt"
    //
    final val IDAccept ="Accept"

    final val TextMe ="我"
    //detail page
    final val IDDetailSendMessage="com.tencent.mm:id/aio"
    final val IDDetailPhotos="com.tencent.mm:id/ej"
    //message with
    final val IDChatMessageEditor="com.tencent.mm:id/c6v"
    final val IDChatMessageSend="com.tencent.mm:id/c70"
    final val IDChatFriendImage="com.tencent.mm:id/bc"
    //photopage
    final val IDPhotoList="com.tencent.mm:id/bsn"
    final val IDPhotoEndline="com.tencent.mm:id/ayk"
}
