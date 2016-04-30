package com.lncosie.robot.flow

import com.lncosie.robot.task.*
import com.lncosie.toolkit.Ptr

/**
 * Created by lncosie on 2016/4/30.
 */
class WorkFlow{


    private val photo_goto= Ptr<Node>()
    private val run_reset = Ptr<Node>()
    private val run_finish= Ptr<Node>()

    fun autoWork():Node {
        return run_reset.value
    }
    fun manWork():Node{
        return photo_goto.value
    }
    fun finish():Node{
        return run_finish.value
    }

    fun make_flow(){
        //exit
        run_finish.reset(Node(RunFinish(), nop, nop))
        //reset
        run_reset.reset(Node(WxClose(), run_start, run_finish))
        //start
        run_start.reset(Node(WxOpen(), friend_open, run_finish))
        //accept
        friend_open.reset(Node(FriendOpen(), friend_accept, nop))
        friend_accept.reset(Node(FriendAccept(), log_open, nop))
        log_open.reset(Node(LogOpenUser(), message_welcome, nop))
        message_welcome.reset(Node(MessageSend(), photo_goto, nop))
        //photo
        photo_goto.reset(Node(PhotoGoto(), photo_scroll, nop))
        photo_scroll.reset(Node(PhotoScroll(), db_read_upload, nop))
        //upload
        db_read_upload.reset(Node(DbUpload(), wx_close, nop))
        //send finish
        wx_close.reset(Node(WxClose(), wx_open, nop))
        wx_open.reset(Node(WxOpen(), friend_gochat, nop))
        friend_gochat.reset(Node(FriendGotoChat(), friend_accept, nop))
        friend_say_finish.reset(Node(MessageSend(), log_close, run_reset))
        //redo
        log_close.reset(Node(LogCloseUser(), run_reset, run_reset))
    }
    val run_start= Ptr<Node>()
    val wx_open=Ptr<Node>()
    val wx_close=Ptr<Node>()
    val friend_open= Ptr<Node>()
    val friend_accept= Ptr<Node>()
    val log_open= Ptr<Node>()
    val message_welcome= Ptr<Node>()
    val photo_scroll= Ptr<Node>()
    val db_read_upload= Ptr<Node>()
    val log_close= Ptr<Node>()
    val friend_gochat= Ptr<Node>()
    val friend_say_finish = Ptr<Node>()

    val nop= Ptr<Node>()

}