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
        NodeFinish.reset(Node("NodeFinish",RunFinish(), nop, nop))
        NodeStart.reset(Node("NodeStart",Nop(), NodeOpenWechat, NodeFinish))
        NodeOpenWechat.reset(Node("NodeOpenWechat",WxOpen(), NodeGotoMe, NodeFinish))
    }

    private fun ConfigServer() {
        val NodeSaveID = Ptr<Node>()
        NodeGotoMe.reset(Node("NodeGotoMe",FromHomeToMe(), NodeSaveID, NodeFinish))
        NodeSaveID.reset(Node("NodeSaveID",SaveMeId(), NodeReset, NodeFinish))
    }

    private fun ResetForRun() {
        //reset
        NodeReset.reset(Node("NodeReset",WxClose(), NodeOpenWechat, NodeFinish))
        //start
        //NodeOpenWechat.reset(Node("NodeOpenWechat",WxOpen(), NodeOpenNewFriends, NodeFinish))
        NodeOpenWechat.reset(Node("NodeOpenWechat",WxOpen(), NodeScrollPhoto, NodeReset))

    }

    private fun AcceptNewFriend() {
        //accept
        NodeOpenNewFriends.reset(Node("NodeOpenNewFriends",FriendOpen(), NodeAcceptFriend, NodeReset))
        NodeAcceptFriend.reset(Node("NodeAcceptFriend",FriendAccept(), NodeSayWelcome, NodeReset))
        NodeSayWelcome.reset(Node("NodeSayWelcome",MessageSend(), NodeGotoPhoto, NodeReset))
    }
    private fun FetchDataAndUpload() {
        NodeGotoPhoto.reset(Node("NodeGotoPhoto",GotoPage(WechatId.IDDetailPhotos), NodeScrollPhoto, nop))
        NodeScrollPhoto.reset(Node("NodeScrollPhoto",ScrollWhileFind(WechatId.IDPhotoList, WechatId.IDPhotoEndline),
                NodeUploadDb, nop))
        //upload
        NodeUploadDb.reset(Node("NodeUploadDb",DbUpload(), NodeCloseWechat, nop))
    }
    private fun FinishAndFetchNew() {
        //send finish
        NodeCloseWechat.reset(Node("NodeCloseWechat",WxClose(), NodeReopenWechat, nop))
        NodeReopenWechat.reset(Node("NodeReopenWechat",WxOpen(), NodeSayFinish, nop))
        //NodeChatWith.reset(Node("NodeChatWith",FromChatHistoryToChat(), NodeAcceptFriend, nop))
        NodeSayFinish.reset(Node("NodeSayFinish",MessageSend(), NodeReset, NodeReset))
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