package com.lncosie.robot.flow

import com.lncosie.robot.task.*
import com.lncosie.toolkit.Ptr

/**
 * Created by lncosie on 2016/4/30.
 */
class WorkFlow {


    private val NodeGotoPhoto = Ptr<Node>()

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
    }



    private fun MakeStartFinishNode() {
        val NodeOpenWechat = Ptr<Node>()
        NodeFinish.reset(Node(RunFinish(), nop, nop))
        NodeStart.reset(Node(Nop(), NodeOpenWechat, NodeFinish))
        NodeOpenWechat.reset(Node(WxOpen(), NodeGotoMe, NodeFinish))
    }

    private fun ConfigServer() {
        val NodeSaveID = Ptr<Node>()
        NodeGotoMe.reset(Node(FromHomeToMe(), NodeSaveID, NodeFinish))
        NodeSaveID.reset(Node(SaveMeId(), NodeReset, NodeFinish))
    }

    private fun ResetForRun() {
        //reset
        NodeReset.reset(Node(WxClose(), NodeOpenWechat, NodeFinish))
        //start
        NodeOpenWechat.reset(Node(WxOpen(), NodeOpenNewFriends, NodeFinish))
    }

    private fun AcceptNewFriend() {
        //accept
        NodeOpenNewFriends.reset(Node(FriendOpen(), NodeAcceptFriend, NodeReset))
        NodeAcceptFriend.reset(Node(FriendAccept(), NodeSayWelcome, NodeReset))
        NodeSayWelcome.reset(Node(MessageSend(), NodeGotoPhoto, NodeReset))
    }
    private fun FetchDataAndUpload() {
        NodeGotoPhoto.reset(Node(GotoPage(WechatId.IDDetailPhotos), NodeScrollPhoto, nop))
        NodeScrollPhoto.reset(Node(ScrollWhileFind(WechatId.IDDetailPhotos, WechatId.IDPhotoEndline), NodeUploadDb, nop))
        //upload
        NodeUploadDb.reset(Node(DbUpload(), NodeCloseWechat, nop))
    }
    private fun FinishAndFetchNew() {
        //send finish
        NodeCloseWechat.reset(Node(WxClose(), NodeReopenWechat, nop))
        NodeReopenWechat.reset(Node(WxOpen(), NodeChatWith, nop))
        NodeChatWith.reset(Node(FromChatHistoryToChat(), NodeAcceptFriend, nop))
        NodeSayFinish.reset(Node(MessageSend(), NodeReset, NodeReset))
    }
    val NodeGotoMe = Ptr<Node>()
    val NodeOpenWechat = Ptr<Node>()
    val NodeReopenWechat = Ptr<Node>()
    val NodeCloseWechat = Ptr<Node>()
    val NodeReset = Ptr<Node>()
    val NodeOpenNewFriends = Ptr<Node>()
    val NodeAcceptFriend = Ptr<Node>()
    val NodeSayWelcome = Ptr<Node>()
    val NodeScrollPhoto = Ptr<Node>()
    val NodeUploadDb = Ptr<Node>()
    val NodeChatWith = Ptr<Node>()
    val NodeSayFinish = Ptr<Node>()

    val nop = Ptr<Node>()

}