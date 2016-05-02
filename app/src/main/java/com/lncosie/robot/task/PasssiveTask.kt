package com.lncosie.robot.task

import android.content.ClipData

import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.text.ClipboardManager
import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by lncosie on 2016/4/30.
 */
abstract open class PasssiveTask : Task {
    override fun start(env: Envirment) {}
    override fun end(env: Envirment){}
    override fun isPassive()=true
    protected fun get(event: Event, key:String, isID:Boolean=true):AccessibilityNodeInfo?{
        val nodes=if(isID)event.service.rootInActiveWindow.findAccessibilityNodeInfosByViewId(key)
        else event.service.rootInActiveWindow.findAccessibilityNodeInfosByText(key);
        if(nodes.size>0)
            return nodes.get(0)
        return null
    }
    override fun toString(): String {
        return this.javaClass.simpleName
    }
}
class MessageSend(): PerfermAction(WechatId.IDChatMessageSend){
    override fun step(event: Event, env: Envirment): Task.Direction {

        val past_edit=get(event,WechatId.IDChatMessageEditor)
        if(past_edit==null)
            return Task.Direction.Waiting
        Looper.prepare()
        past_edit.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
        past_edit.performAction(AccessibilityNodeInfo.FOCUS_INPUT)

        val clip = event.service.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager;
        clip.setText(env.msg);
        past_edit.performAction(AccessibilityNodeInfo.ACTION_PASTE)
        past_edit.performAction(AccessibilityNodeInfo.FOCUS_INPUT)

        wait_moment(500)
        return super.step(event, env)
    }
}

class FriendAccept():PerfermAction(WechatId.IDAccept,false){

}
class FromChatHistoryToChat():PerfermAction(WechatId.NickName,false){
    override fun step(event: Event, env: Envirment): Task.Direction {
        this.key=env.user
        return super.step(event, env)
    }
}
class FromHomeToMe():PerfermAction(""){
    override fun step(event: Event, env: Envirment): Task.Direction {
        val home4button=event.service.rootInActiveWindow.findAccessibilityNodeInfosByViewId(WechatId.IDHome4ButtonName)
        for(button in home4button){
            if(button.text.toString().equals(WechatId.TextMe)){
                return perform(button)
            }
        }
        return Task.Direction.Waiting
    }
}
class SaveMeId():PasssiveTask(){
    override fun step(event: Event, env: Envirment): Task.Direction {
        val myname=get(event,WechatId.IDMePageSelfName)
        if(myname!=null){
            env.wxid=myname.text.toString()
            return Task.Direction.Forward
        }
        return Task.Direction.Waiting
    }
}
open class GotoPage(key:String,id:Boolean=true):PerfermAction(key,id){

}

class ScrollWhileFind(val list:String,val end:String):PerfermAction(list,true,AccessibilityNodeInfo.ACTION_SCROLL_FORWARD){
    override fun step(event: Event, env: Envirment): Task.Direction {
        if(get(event,end)!=null){
            return Task.Direction.Forward
        }
        super.step(event, env)
        return Task.Direction.Waiting
    }
}

open class PerfermAction(var key:String,val id:Boolean=true,val action:Int=AccessibilityNodeInfo.ACTION_CLICK): PasssiveTask(){
    override fun step(event: Event, env: Envirment): Task.Direction {
        return doAction(event)
    }

    protected fun doAction(event: Event): Task.Direction {
        var node=get(event, key, id)
        return perform(node)
    }

    protected fun perform(node: AccessibilityNodeInfo?): Task.Direction {
        var node1 = node
        while (node1 != null) {
            val accept = when (action) {
                AccessibilityNodeInfo.ACTION_CLICK -> node1.isClickable
                AccessibilityNodeInfo.ACTION_SCROLL_FORWARD -> node1.isScrollable
                AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD -> node1.isScrollable
                AccessibilityNodeInfo.ACTION_LONG_CLICK -> node1.isClickable
                else -> false
            }
            if (accept)
                break
            node1 = node1.parent
        }
        if (node1 == null) {
            return Task.Direction.Waiting
        } else {
            node1.performAction(action)
            return Task.Direction.Forward
        }
    }


}
