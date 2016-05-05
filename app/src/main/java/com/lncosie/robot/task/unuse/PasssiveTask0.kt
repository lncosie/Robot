package com.lncosie.robot.task.unuse

import android.view.accessibility.AccessibilityEvent
import com.lncosie.robot.Config.Config
import com.lncosie.robot.books.FetchList
import com.lncosie.robot.books.SuccessList
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event
import com.lncosie.robot.log.Orm
import com.lncosie.robot.task.Task
import com.lncosie.robot.task.TaskPasssive
import com.lncosie.robot.task.TaskSendMessage
import com.lncosie.robot.task.WechatId
import com.lncosie.toolkit.shell.Shell

/**
 * Created by lncosie on 2016/5/2.
 */

class MessageSendFinish: TaskSendMessage(){
    override fun step(event: Event, env: Envirment): Task.Direction {
        val ret=super.step(event, env)
        if(ret== Task.Direction.Forward){
            val finishTask= SuccessList()
            finishTask.user=env.usernick
            Orm.save(finishTask)
        }
        return ret
    }
}


class FromChatToDetail() : PerfermClick(WechatId.IDChatFriendImage) {
}

class DetailGotoChat() : TaskPasssive() {
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
            val addTask= FetchList()
            addTask.user=env.usernick
            Orm.save(addTask)

            val has = runOnNode(event, key, id) {
                it.click()
                Task.Direction.Forward
            }
            if (has == Task.Direction.Forward) Task.Direction.Forward
            else Task.Direction.Back
        }
    }
}


class FromHomeToMe() : TaskPasssive() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDHome4ButtonName, true) {
            if (it.text.toString().equals(WechatId.HomeMe)) {
                it.click()
                return@runOnNode Task.Direction.Forward
            }
            Task.Direction.Waiting
        }
    }
}

class SaveMeId() : TaskPasssive() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDMePageSelfName, true) {
            env.wxid = it.text.toString()
            Config.wxid = env.wxid
            Task.Direction.Forward
        }
    }
}


class ScrollWhileFind(val list: String, val end: String) : TaskPasssive() {
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

open class PerfermClick(var key: String, val id: Boolean = true) : TaskPasssive() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, key, id) {
            it.click()
            Task.Direction.Forward
        }
    }
}

class FromChatHistoryToChat() : TaskPasssive() {
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
