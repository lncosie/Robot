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
        NodeStart.reset(Node("NodeStart",WxClose(), NodeOpenWechat, nop))
        NodeOpenWechat.reset(Node("NodeOpenWechat",WxOpen(), NodeGotoMe, nop))
    }

    private fun ConfigServer() {
        NodeGotoMe.reset(Node("NodeGotoMe",FromHomeToMe(), NodeSaveID, nop))
        NodeSaveID.reset(Node("NodeSaveID",SaveMeId(), NodeReset, nop))
    }

    private fun ResetForRun() {
        //reset
        NodeReset.reset(Node("NodeReset",Reset(), NodeCloseWechat, NodeFinish))
        NodeCloseWechat.reset(Node("NodeCloseWechat",WxClose(), NodeOpenNewFriends, NodeReset))
    }

    private fun AcceptNewFriend() {
        //accept
        NodeOpenNewFriends.reset(Node("NodeOpenNewFriends",FriendOpen(), NodeAcceptFriend, NodeReset))
        NodeAcceptFriend.reset(Node("NodeAcceptFriend",FriendAccept(), NodeDetailGotoChat, NodeReset))
        NodeDetailGotoChat.reset(Node("NodeGotoChat", DetailGotoChat_SaveUser(), NodeSayWelcome, NodeReset))
        NodeSayWelcome.reset(Node("NodeSayWelcome",MessageSend(), NodeGotoDetail, NodeReset))
    }
    private fun FetchDataAndUpload() {
        NodeGotoDetail.reset(Node("NodeGotoDetail",FromChatToDetail(), NodeGotoPhoto, NodeReset))
        NodeGotoPhoto.reset(Node("NodeGotoPhoto",DetailGotoPhoto(), NodeScrollPhoto, NodeReset))
        NodeScrollPhoto.reset(Node("NodeScrollPhoto",ScrollWhileFind(WechatId.IDPhotoList, WechatId.IDPhotoEndline),
                NodeUploadDb, NodeReset))
        //upload
        NodeUploadDb.reset(Node("NodeUploadDb",DbUpload(), NodeGotoChatWithUser, NodeReset))
    }
    private fun FinishAndFetchNew() {

        NodeGotoChatWithUser.reset(Node("NodeGotoChatWithUser",FromChatHistoryToChat(), NodeSayFinish, NodeReset))
        NodeSayFinish.reset(Node("NodeSayFinish",MessageSend(), NodeReset, NodeReset))
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
    val NodeGotoChatWithUser = Ptr<Node>()
    val NodeSayFinish = Ptr<Node>()
    val nop = Ptr<Node>()

}