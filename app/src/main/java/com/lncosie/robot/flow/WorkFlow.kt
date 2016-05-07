package com.lncosie.robot.flow

import com.lncosie.robot.Config.WechatId
import com.lncosie.robot.task.*
import com.lncosie.robot.unuse.TaskReset
import com.lncosie.toolkit.Ptr

/**
 * Created by lncosie on 2016/5/2.
 */
class WorkFlow {
    private var NodeStart = Ptr<Node>()

    private var nop = Ptr<Node>()
    fun autoWork() = NodeStart.value
    fun manWork() = NodeManstart.value
    fun make() {
        graphFetchUser()
        graphSendFinish()
        graphReset()
        graphGetconfig()
    }

    fun graphGetconfig() {
        var Config = Ptr<Node>(Node("配置服务", TaskGetMeId(), NodeReset, nop))
        var Start = Ptr<Node>(Node("开启微信", TaskOpenWx(), Config, nop))
        var Close = Ptr<Node>(Node("关闭微信", TaskCloseWx(), Start, nop))
        NodeStart.reset(Node("开始服务", TaskReset(), Close, nop))
    }

    fun graphReset() {
        var Start = Ptr<Node>(Node("开启微信", TaskOpenWx(), CheckRedoUser, nop))
        var Close = Ptr<Node>(Node("关闭微信", TaskCloseWx(), Start, nop))
        NodeReset.reset(Node("重置", TaskReset(), Close, nop))
        //NodeStart.reset(Node("NodeStart", TaskReset(), NodeReset, nop))
    }

    fun graphSendFinish() {
        val godetail = GotoDetail(SendFinish, false)
        CheckRedoUser.reset(Node("检查完成用户", TaskCheckFinishUser(), godetail, AcceptNew))
    }

    fun graphFetchUser() {
        SendFinish.reset(Node("发送印书地址", TaskSendMessage(), NodeReset, nop))
        Scroll.reset(Node("滑动朋友圈", TaskScrollPhoto(), Upload, NodeReset))

        val goToPhoto = GotoDetail(Scroll, false)
        val sendFinish = GotoDetail(SendFinish, true)

        var Start = Ptr<Node>(Node("开启微信", TaskOpenWx(), sendFinish, nop))
        var Close = Ptr<Node>(Node("关闭微信", TaskCloseWx(), NodeMakeBook, nop))
        val csend = Ptr<Node>(Node("发送欢迎信息", TaskPerformClick(WechatId.IDDetailSendMessage), SendWelcome, NodeReset))
        NodeManstart.reset(Node("接受新朋友", TaskDetailSaveAccept(), csend, NodeReset))
        val accept = Ptr<Node>(Node("接受新朋友", TaskAcceptFriend(), NodeManstart, NodeReset))
        AcceptNew.reset(Node("接受新朋友", TaskOpenNewFriends(), accept, NodeReset))
        SendWelcome.reset(Node("发送欢迎信息", TaskSendMessage(), goToPhoto, NodeReset))
        Upload.reset(Node("上传数据", TaskDbUpload(), Close, NodeReset))
        NodeMakeBook.reset(Node("等待打印", TaskWaitMakeBook(), Start, NodeReset))
    }

    fun GotoDetail(follow: Ptr<Node>, sendOrView: Boolean): Ptr<Node> {
        val btn = if (sendOrView) WechatId.IDDetailSendMessage0 else WechatId.IDDetailPhotos0
        var click = Ptr<Node>(Node("转向到用户", TaskPerformClick(btn, false), follow, NodeReset))
        var detail = Ptr<Node>(Node("转向到用户", TaskGotoUserDetail(), click, NodeReset))
        var home = Ptr<Node>(Node("转向到用户", TaskGotoFriendList(), detail, NodeReset))
        return home
    }

    var NodeManstart = Ptr<Node>();
    var NodeReset = Ptr<Node>();
    var CheckRedoUser = Ptr<Node>();

    var Scroll = Ptr<Node>();
    var Upload = Ptr<Node>();
    var NodeMakeBook = Ptr<Node>();
    var SendFinish = Ptr<Node>();
    var SendWelcome = Ptr<Node>();
    var AcceptNew = Ptr<Node>();
}