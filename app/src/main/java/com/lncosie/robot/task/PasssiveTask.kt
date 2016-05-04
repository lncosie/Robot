package com.lncosie.robot.task

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Rect
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.Config.Config
import com.lncosie.robot.books.FetchList
import com.lncosie.robot.books.SuccessList
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event
import com.lncosie.robot.log.Orm
import com.lncosie.toolkit.shell.Shell

/**
 * Created by lncosie on 2016/5/2.
 */

class MessageSendFinish:MessageSendWelcome(){
    override fun step(event: Event, env: Envirment): Task.Direction {
        val ret=super.step(event, env)
        if(ret== Task.Direction.Forward){
            val finishTask=SuccessList()
            finishTask.user=env.usernick
            finishTask.uri=env.msg
            Orm.save(finishTask)
        }
        return ret
    }
}
open class MessageSendWelcome() : PasssiveTask() {
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
                Task.Direction.Forward
            }
        }
    }
}

class FriendAccept() : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        val inDelete = event.event.getClassName()?.toString()?.contains("com.tencent.mm.ui.base.k") ?: false
        //delete accepted
        if (inDelete) {
            return runOnNode(event, WechatId.IDDelInMenu, false) {
                it.click()
                Task.Direction.Waiting
            }
        }
        //WechatId.IDHaveAccept
        //accept new IDAccept
        runOnNode(event, WechatId.IDHaveAccept, true) {
            it.longClick()
            Task.Direction.Waiting
        }
        return runOnNode(event, WechatId.IDAccept, true) {
            it.click()
            Task.Direction.Forward
        }
    }
}

class FromChatToDetail() : PerfermClick(WechatId.IDChatFriendImage) {
}

class DetailGotoChat() : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDDetailSendMessage0, false) {

            it.click()
            Task.Direction.Forward
        }
    }
}

open class DetailGotoPhoto_SaveUser() : PerfermClick(WechatId.IDDetailPhotos0, false) {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDDetailNickname, true) {
            val t = it.text.toString()
            env.usernick = t.trim()
            val addTask=FetchList()
            addTask.user=env.usernick
            Orm.save(addTask)

            val has = runOnNode(event, key, id) {
                it.click()
                Task.Direction.Forward
            }
            if (has == Task.Direction.Forward)Task.Direction.Forward
            else Task.Direction.Back
        }
    }
}


class FromHomeToMe() : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDHome4ButtonName, true) {
            if (it.text.toString().equals(WechatId.TextMe)) {
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
            Config.wxid = env.wxid
            Task.Direction.Forward
        }
    }
}


class ScrollWhileFind(val list: String, val end: String) : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        if (event.event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
            return Task.Direction.Waiting

        var ended = runOnNode(event, end, true) {
            reachEnd(event)
        }
        if (ended == Task.Direction.Forward)
            return Task.Direction.Forward
        ended = runOnNode(event, WechatId.IDPhotoEndline0, false) {
            reachEnd(event)
        }
        if (ended == Task.Direction.Forward)
            return Task.Direction.Forward

        //list, true, AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
        return runOnNode(event, list, true) {
            it.scroll()
            Task.Direction.Waiting
        }
    }

    private fun reachEnd(event: Event): Task.Direction {
        //        event.service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
        //        wait_moment(500)
        //        event.service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
        //        wait_moment(500)
        //        event.service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
        //        wait_moment(500)
        Shell.close()
        wait_moment(1000)
        return Task.Direction.Forward
    }
}

open class PerfermClick(var key: String, val id: Boolean = true) : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, key, id) {
            it.click()
            Task.Direction.Forward
        }
    }
}

class FromChatHistoryToChat() : PasssiveTask() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDChatHistoryNickname, true) {
            if (env.usernick.trim().contains(it.text.toString())) {
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
        val root = event.service.rootInActiveWindow
        if (root == null)
            return Task.Direction.Waiting
        val nodes = if (isID) root.findAccessibilityNodeInfosByViewId(key)
        else root.findAccessibilityNodeInfosByText(key);
        if (nodes != null)
            for (node in nodes) {
                if (node.isVisibleToUser) {
                    val dir=fn(node)
                    if (dir== Task.Direction.Waiting) {
                        node.recycle()
                        continue
                    }else{
                        node.recycle()
                        return dir
                    }

                }
            }
        return Task.Direction.Waiting
    }

    override fun toString(): String {
        return this.javaClass.simpleName
    }

    fun AccessibilityNodeInfo.scroll() = perform(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
    fun AccessibilityNodeInfo.longClick() = perform(AccessibilityNodeInfo.ACTION_LONG_CLICK)
    fun AccessibilityNodeInfo.click() = perform(AccessibilityNodeInfo.ACTION_CLICK)
    fun AccessibilityNodeInfo.perform(e: Int) {
        if (e == AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) {
            wait_moment(500)
            Shell.post("input swipe 100 750 100 50")
            wait_moment(800)
            return
        } else if (e == AccessibilityNodeInfo.ACTION_CLICK) {
            wait_moment(300)
            val rect = Rect()
            this.getBoundsInScreen(rect)
            Shell.post("input tap " + rect.centerX() + " " + rect.centerY())
            wait_moment(500)
            return
        }
        var p = this
        while (p != null) {
            if (p.isClickable) {
                wait_moment(500)
                p.performAction(e)
                wait_moment(100)
                break
            }
            p = p.parent
        }
    }
}