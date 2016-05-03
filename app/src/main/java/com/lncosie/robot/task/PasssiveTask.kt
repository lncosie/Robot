package com.lncosie.robot.task

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.os.Looper
import android.text.ClipboardManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event
import com.lncosie.toolkit.shell.Shell

/**
 * Created by lncosie on 2016/5/2.
 */


class MessageSend() : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDChatMessageEditor, true) {
            if (Looper.myLooper() == null) {
                Looper.prepare()
            }
            it.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            it.performAction(AccessibilityNodeInfo.FOCUS_INPUT)
            val clip = event.service.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
            clip.setText(env.msg);
            it.performAction(AccessibilityNodeInfo.ACTION_PASTE)
            it.performAction(AccessibilityNodeInfo.FOCUS_INPUT)
            wait_moment(500)
            runOnNode(event,WechatId.IDChatMessageSend,true){
                it.click()
                Task.Direction.Forward
            }
        }
    }
}

class FriendAccept() : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        val inDelete = event.event.getClassName()?.toString()?.contains("com.tencent.mm.ui.base.k")?:false
        //delete accepted
        if(inDelete){
            return runOnNode(event,WechatId.IDDelInMenu,false){
                it.click()
                Task.Direction.Waiting
            }
        }
        //WechatId.IDHaveAccept
        //accept new IDAccept
//        runOnNode(event,WechatId.IDHaveAccept,true){
//            it.longClick()
//            Task.Direction.Waiting
//        }
        return runOnNode(event,WechatId.IDHaveAccept,true){
            it.click()
            Task.Direction.Forward
        }
    }
}

class FromChatToDetail() : PerfermClick(WechatId.IDChatFriendImage) {
}

class DetailGotoChat_SaveUser() : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDDetailSendMessage0, false) {
            runOnNode(event, WechatId.IDDetailNickname, true) {
                env.usernick = it.text.toString()
                Task.Direction.Waiting
            }
            it.click()
            Task.Direction.Forward
        }
    }
}

open class DetailGotoPhoto() : PerfermClick(WechatId.IDDetailPhotos0,false) {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return super.step(event, env)
    }
}


class FromHomeToMe() : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event,WechatId.IDHome4ButtonName,true){
            if(it.text.toString().equals(WechatId.TextMe)){
                it.click()
                return@runOnNode Task.Direction.Forward
            }
            Task.Direction.Waiting
        }
    }
}

class SaveMeId() : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDMePageSelfName, true) {
            env.wxid = it.text.toString()
            Task.Direction.Forward
        }
    }
}


class ScrollWhileFind(val list: String, val end: String) : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        if(event.event.eventType!=AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
            return Task.Direction.Waiting

        val ended = runOnNode(event, end, true) {
            event.service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            wait_moment(500)
            event.service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            wait_moment(500)
            event.service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
            wait_moment(500)
            Shell.close()
            wait_moment(1000)
            Task.Direction.Forward
        }
        if (ended == Task.Direction.Forward)
            return Task.Direction.Forward
        //list, true, AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
        return runOnNode(event,list,true){
            it.scroll()
            Task.Direction.Waiting
        }
    }
}

open class PerfermClick(var key: String, val id: Boolean = true) : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event,key,id){
            it.click()
            Task.Direction.Forward
        }
    }
}
class FromChatHistoryToChat() : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event,WechatId.IDChatHistoryNickname,true){
            if(env.usernick.trim().equals(it.text.toString())){
                it.click()
                return@runOnNode Task.Direction.Forward
            }
            Task.Direction.Waiting
        }
    }
}
abstract open class PasssiveTask : Task {
    override fun start(env: Envirment) {
    }

    override fun end(env: Envirment) {
    }

    override fun isPassive() = true
    protected fun runOnNode(event: Event, key: String, isID: Boolean = true, fn: (AccessibilityNodeInfo) -> Task.Direction): Task.Direction {
        val root=event.service.rootInActiveWindow
        if(root==null)
            return Task.Direction.Waiting
        val nodes = if (isID)root.findAccessibilityNodeInfosByViewId(key)
        else root.findAccessibilityNodeInfosByText(key);
        if(nodes!=null)
            for (node in nodes) {
                if (node.isVisibleToUser)
                    if (fn(node) == Task.Direction.Forward) {
                        node.recycle()
                        return Task.Direction.Forward
                    }
                node.recycle()
            }
        return Task.Direction.Waiting
    }

    override fun toString(): String {
        return this.javaClass.simpleName
    }
    fun AccessibilityNodeInfo.scroll()=perform(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
    fun AccessibilityNodeInfo.longClick()=perform(AccessibilityNodeInfo.ACTION_LONG_CLICK)
    fun AccessibilityNodeInfo.click()=perform(AccessibilityNodeInfo.ACTION_CLICK)
    fun AccessibilityNodeInfo.perform(e:Int){
        if(e==AccessibilityNodeInfo.ACTION_SCROLL_FORWARD){
            wait_moment(500)
            Shell.post("input swipe 100 700 100 100")
            wait_moment(1000)
            return
        }
        var p = this
        while (p != null) {
            if(p.isClickable){
                wait_moment(300)
                p.performAction(e)
                wait_moment(500)
                break
            }
            p = p.parent
        }
    }
}