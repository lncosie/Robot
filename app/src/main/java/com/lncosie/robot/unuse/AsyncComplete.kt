package com.lncosie.robot.unuse

import android.view.accessibility.AccessibilityEvent
import com.lncosie.robot.log.FetchList
import com.lncosie.robot.log.TodoList
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event
import com.lncosie.robot.orm.Orm
import com.lncosie.robot.task.Task
import com.lncosie.robot.task.TaskInitiative
import com.lncosie.robot.task.TaskPasssive
import com.lncosie.robot.Config.WechatId

/**
 * Created by guazi on 2016/5/5.
 */

class FetchFriendUsers(): TaskPasssive(){
    override fun step(event: Event, env: Envirment): Task.Direction {
        if (event.event.eventType != AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
            return Task.Direction.Waiting
        runOnNode(event, WechatId.IDFriendListNickname, true) {
            val user= FetchList()
            user.user=it.text.toString()
            Orm.save(user)
            Task.Direction.Waiting
        }
        return runOnNode(event, WechatId.IDFriendListEndline, true) {
            return@runOnNode Task.Direction.Forward
        }
    }
}
class FetchTodoUser(): TaskInitiative(){
    override fun start(env: Envirment) {
        val user= Orm.load(TodoList::class.java,null,null)
        user.forEach {
            env.todo=it.user
            return@start
        }
        throw Exception("No user to do")
    }
}