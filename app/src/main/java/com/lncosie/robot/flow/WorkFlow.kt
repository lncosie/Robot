package com.lncosie.robot.flow

import com.lncosie.robot.task.*
import com.lncosie.robot.task.unuse.TaskReset
import com.lncosie.toolkit.Ptr

/**
 * Created by lncosie on 2016/5/2.
 */
class WorkFlow {
    private var NodeStart = Ptr<Node>()

    private var nop = Ptr<Node>()
    fun autoWork() = NodeStart.value
    fun manWork() = NodeStart.value
    fun make() {
        graphFetchUser()
        graphSendFinish()
        graphReset()


    }

    fun graphReset() {
        var Start = Ptr<Node>(Node("Start", TaskOpenWx(), CheckFinishUser, nop))
        var Close = Ptr<Node>(Node("Close", TaskCloseWx(), Start, nop))
        NodeReset.reset(Node("NodeReset", TaskReset(), Close, nop))
        NodeStart.reset(Node("NodeStart", TaskReset(), NodeReset, nop))
    }

    fun graphSendFinish() {
        SendFinish.reset(Node("SendFinish", TaskSendMessage(), NodeReset, nop))
        val godetail = GotoDetail(SendFinish, true)
        CheckFinishUser.reset(Node("CheckFinishUser", TaskCheckbookFinished(), godetail, AcceptNew))
    }

    fun graphFetchUser() {
        Scroll.reset(Node("Scroll", TaskScrollPhoto(), Upload, NodeReset))
        val godetail = GotoDetail(Scroll, false)
        val accept = Ptr<Node>(Node("AcceptNew", TaskAcceptFriend(), SendWelcome, nop))
        AcceptNew.reset(Node("AcceptNew", TaskOpenNewFriends(), accept, nop))
        SendWelcome.reset(Node("SendWelcome", TaskSendMessage(), godetail, NodeReset))
        Upload.reset(Node("Upload", DbUpload(), NodeReset, NodeReset))
    }

    fun GotoDetail(follow: Ptr<Node>, sendOrView: Boolean): Ptr<Node> {
        val btn = if (sendOrView) WechatId.IDDetailSendMessage0 else WechatId.IDDetailPhotos0
        var click = Ptr<Node>(Node("TaskPerformClick", TaskPerformClick(btn, false), follow, NodeReset))
        var detail = Ptr<Node>(Node("TaskGotoUserDetail", TaskGotoUserDetail(), click, NodeReset))
        var home = Ptr<Node>(Node("TaskGotoFriendList", TaskGotoFriendList(), detail, NodeReset))
        return home
    }

    var NodeReset = Ptr<Node>();
    var CheckFinishUser = Ptr<Node>();

    var Scroll = Ptr<Node>();
    var Upload = Ptr<Node>();
    var SendFinish = Ptr<Node>();
    var SendWelcome = Ptr<Node>();
    var AcceptNew = Ptr<Node>();
}