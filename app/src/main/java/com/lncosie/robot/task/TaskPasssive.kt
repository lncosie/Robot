package com.lncosie.robot.task

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event
import com.lncosie.toolkit.shell.Shell

/**
 * Created by guazi on 2016/5/5.
 */

abstract open class TaskPasssive : Task {
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

    fun AccessibilityNodeInfo.backscroll() = perform(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
    fun AccessibilityNodeInfo.scroll() = perform(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
    fun AccessibilityNodeInfo.longClick() = perform(AccessibilityNodeInfo.ACTION_LONG_CLICK)
    fun AccessibilityNodeInfo.click() = perform(AccessibilityNodeInfo.ACTION_CLICK)
    fun AccessibilityNodeInfo.perform(e: Int) {
        if (e == AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) {
            wait_moment(500)
            Shell.post("input swipe 200 800 200 100")
            wait_moment(800)
            return
        }else if(e == AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD) {
            wait_moment(500)
            Shell.post("input swipe 200 100 200 800")
            wait_moment(800)
            return
        }
        else if (e == AccessibilityNodeInfo.ACTION_CLICK) {
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