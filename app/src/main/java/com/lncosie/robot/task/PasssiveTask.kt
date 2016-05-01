package com.lncosie.robot.task

import android.os.Bundle
import android.view.accessibility.AccessibilityNodeInfo
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event

/**
 * Created by lncosie on 2016/4/30.
 */

class MessageSend(): PerfermAction(WechatId.Send){
    override fun step(event: Event, env: Envirment): Task.Direction {
        val past_edit=get(event,WechatId.ID_SendEdit,true)
        if(past_edit==null)
            return Task.Direction.Waiting
        val arguments = Bundle();
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,env.msg);
        past_edit.performAction(AccessibilityNodeInfo.ACTION_PASTE)
        wait_moment(500)
        return super.step(event, env)
    }
}

class FriendAccept():PerfermAction(WechatId.Accept){

}
class FriendGotoChat():PerfermAction(WechatId.NickName){
    override fun step(event: Event, env: Envirment): Task.Direction {
        this.key=env.user
        return super.step(event, env)
    }
}
class PhotoGoto():PerfermAction(WechatId.Photo){

}
class PhotoScroll():PerfermAction(WechatId.ID_PhotoView,true,AccessibilityNodeInfo.ACTION_SCROLL_FORWARD){
    override fun step(event: Event, env: Envirment): Task.Direction {
        if(get(event,WechatId.ID_PhotoEnd,true)!=null){
            return Task.Direction.Forward
        }
        return super.step(event, env)
    }
}
open class PerfermAction(var key:String,val id:Boolean=false,val action:Int=AccessibilityNodeInfo.ACTION_CLICK): PasssiveTask(){
    override fun step(event: Event, env: Envirment): Task.Direction {
        return doAction(event)
    }

    protected fun doAction(event: Event): Task.Direction {
        var node=get(event, key, id)
        while(node!=null){
            val accept=when(action){
                AccessibilityNodeInfo.ACTION_CLICK->node.isClickable
                AccessibilityNodeInfo.ACTION_SCROLL_FORWARD->node.isScrollable
                AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD->node.isScrollable
                AccessibilityNodeInfo.ACTION_LONG_CLICK->node.isClickable
                else->false
            }
            if(accept)
                break
            node=node.parent
        }
        if(node==null){
            return Task.Direction.Waiting
        }else{
            node.performAction(action)
            return Task.Direction.Forward
        }
    }
    protected fun get(event: Event, key:String, id:Boolean=false):AccessibilityNodeInfo?{
        val nodes=if(id)event.service.rootInActiveWindow.findAccessibilityNodeInfosByViewId(key)
                    else event.service.rootInActiveWindow.findAccessibilityNodeInfosByText(key);
        if(nodes.size>0)
            return nodes.get(0)
        return null
    }
}
