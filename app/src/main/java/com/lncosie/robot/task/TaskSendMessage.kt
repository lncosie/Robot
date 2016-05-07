package com.lncosie.robot.task

import android.accessibilityservice.AccessibilityService
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Looper
import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.Config.WechatId
import com.lncosie.robot.log.SuccessList
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event
import com.lncosie.robot.log.BookSendUriList
import com.lncosie.robot.orm.Orm

/**
 * Created by guazi on 2016/5/5.
 */
open class TaskSendMessage(val finish:Boolean=false) : TaskPasssive() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDChatMessageEditor, true) {
            if (Looper.myLooper() == null) {
                Looper.prepare()
            }
            it.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            it.performAction(AccessibilityNodeInfo.FOCUS_INPUT)
            val paste=if(it.text==null)true else it.text.isEmpty()
            if(paste){
                val manager = event.service.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
                val clip = ClipData.newPlainText("label", env.msg)
                manager.setPrimaryClip(clip)
                it.performAction(AccessibilityNodeInfo.ACTION_PASTE)
                wait_moment(500)
            }
            it.click()
            runOnNode(event, WechatId.IDChatMessageSend, true) {
                it.click()
                if(finish)
                    saveFinishUser(env)
                event.service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
                Task.Direction.Forward
            }
        }
    }
    fun saveFinishUser(env: Envirment){
        val user=SuccessList()
        user.user=env.usernick
        Orm.save(user)
        Orm.delete(BookSendUriList::class.java,"userid=?",arrayOf(env.userid));
    }

}
