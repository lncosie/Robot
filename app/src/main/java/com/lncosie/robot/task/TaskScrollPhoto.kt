package com.lncosie.robot.task

import android.view.accessibility.AccessibilityEvent
import com.lncosie.robot.Config.WechatId
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event
import com.lncosie.toolkit.shell.Shell

/**
 * Created by guazi on 2016/5/5.
 */
class TaskScrollPhoto() : TaskPasssive() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        if (event.event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
            return Task.Direction.Waiting

        var ended = runOnNode(event, WechatId.IDPhotoEndline, true) {
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
        return runOnNode(event, WechatId.IDPhotoList, true) {
            it.scroll()
            Task.Direction.Waiting
        }
    }

    private fun reachEnd(event: Event): Task.Direction {
        Shell.close()
        wait_moment(1000)
        return Task.Direction.Forward
    }
}