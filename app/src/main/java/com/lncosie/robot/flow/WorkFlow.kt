package com.lncosie.robot.flow

import com.lncosie.robot.task.*
import com.lncosie.toolkit.Ptr
/**
 * Created by lncosie on 2016/5/2.
 */
class WorkFlow {




    private val NodeStart = Ptr<Node>()
    private val NodeFinish = Ptr<Node>()

    fun autoWork(): Node {
        return NodeStart.value
    }

    fun manWork(): Node {
        return NodeGotoPhoto.value
    }

    fun finish(): Node {
        return NodeFinish.value
    }

    fun make() {
        //start
        MakeStartFinishNode()
        ConfigServer()
        ResetForRun()
        AcceptNewFriend()
        //photo
        FetchDataAndUpload()
        FinishAndFetchNew()
        test()
    }
    fun test(){
        //NodeStart.reset{NodeFinish.value}
    }



    private fun MakeStartFinishNode() {
        NodeFinish.reset(Node("NodeFinish",RunFinish(), nop, nop))
        NodeStart.reset(Node("开始",WxClose(), NodeOpenWechat, nop))
        NodeOpenWechat.reset(Node("打开微信",WxOpen(), NodeGotoMe, nop))
    }

    private fun ConfigServer() {
        NodeGotoMe.reset(Node("打开Me",FromHomeToMe(), NodeSaveID, nop))
        NodeSaveID.reset(Node("获取MeID",SaveMeId(), NodeReset, nop))
    }

    private fun ResetForRun() {
        //reset
        NodeReset.reset(Node("重置",Reset(), NodeCloseWechat, NodeFinish))
        NodeCloseWechat.reset(Node("关闭微信",WxClose(), NodeOpenNewFriends, NodeReset))
    }

    private fun AcceptNewFriend() {
        //accept
        NodeOpenNewFriends.reset(Node("等待新朋友",FriendOpen(), NodeAcceptFriend, NodeReset))
        NodeAcceptFriend.reset(Node("等待新朋友",FriendAccept(), NodeDetailGotoChat, NodeReset))
        NodeDetailGotoChat.reset(Node("准备发送消息", DetailGotoChat(), NodeSayWelcome, NodeReset))
        NodeSayWelcome.reset(Node("发送Welcome", MessageSendWelcome(), NodeGotoDetail, NodeReset))
    }
    private fun FetchDataAndUpload() {
        NodeGotoDetail.reset(Node("打开详情页",FromChatToDetail(), NodeGotoPhoto, NodeReset))
        NodeGotoPhoto.reset(Node("打开相册", DetailGotoPhoto_SaveUser(), NodeScrollPhoto, NodeReset))
        NodeScrollPhoto.reset(Node("查看相册",ScrollWhileFind(WechatId.IDPhotoList, WechatId.IDPhotoEndline),
                NodeCloseAg, NodeReset))
        //close
        NodeCloseAg.reset(Node("关闭微信",WxClose(), NodeUploadDb, NodeReset))
        //upload
        NodeUploadDb.reset(Node("上传数据",DbUpload(), NodeWaitMakeBook, NodeReset))
        NodeWaitMakeBook.reset(Node("等待作书",WaitMakeBook(), NodeOpenAg, NodeReset))
    }
    private fun FinishAndFetchNew() {
        NodeOpenAg.reset(Node("打开微信",WxOpen(), NodeGotoChatWithUser, NodeReset))
        NodeGotoChatWithUser.reset(Node("准备发送完成信息",FromChatHistoryToChat(), NodeSayFinish, NodeReset))
        NodeSayFinish.reset(Node("发送做书地址", MessageSendFinish(), NodeReset, NodeReset))
    }
    val NodeOpenWechat = Ptr<Node>()
    val NodeGotoMe = Ptr<Node>()
    val NodeSaveID=Ptr<Node>()
    val NodeReset = Ptr<Node>()
    val NodeCloseWechat = Ptr<Node>()
    val NodeReOpenWetchat = Ptr<Node>()

    val NodeOpenNewFriends = Ptr<Node>()
    val NodeAcceptFriend = Ptr<Node>()
    val NodeDetailGotoChat = Ptr<Node>()
    val NodeSayWelcome = Ptr<Node>()
    val NodeGotoDetail = Ptr<Node>()
    val NodeGotoPhoto = Ptr<Node>()
    val NodeScrollPhoto = Ptr<Node>()
    val NodeUploadDb = Ptr<Node>()
    val NodeWaitMakeBook=Ptr<Node>()
    val NodeGotoChatWithUser = Ptr<Node>()
    val NodeSayFinish = Ptr<Node>()
    val nop = Ptr<Node>()

    val NodeCloseAg = Ptr<Node>()
    val NodeOpenAg = Ptr<Node>()


}