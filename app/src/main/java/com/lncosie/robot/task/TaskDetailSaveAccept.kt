package com.lncosie.robot.task

import com.lncosie.robot.Config.WechatId
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.Event
import com.lncosie.robot.log.FetchList
import com.lncosie.robot.orm.Orm

/**
 * Created by guazi on 2016/5/6.
 */
open class TaskDetailSaveAccept() : TaskPasssive() {
    override fun step(event: Event, env: Envirment): Task.Direction {
        return runOnNode(event, WechatId.IDDetailNickname, true) {
            val t = it.text.toString()
            env.usernick = t.trim()
            val addTask = FetchList()
            addTask.user = env.usernick
            Orm.save(addTask)
            Task.Direction.Forward
        }
    }
}