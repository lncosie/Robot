package com.lncosie.robot.task

import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.log.BookSendUriList
import com.lncosie.robot.log.RedoUserList
import com.lncosie.robot.orm.Orm


/**
 * Created by guazi on 2016/5/5.
 */
class TaskCheckFinishUser() : TaskInitiative() {
    override fun start(env: Envirment) {
        val finished = Orm.load(BookSendUriList::class.java,null,null)
        finished.forEach {
            if(it.uri!=null&&it.uri.length>0) {
                env.msg = it.uri
                env.userid = it.userid
                env.usernick = it.user
                return@start
            }
        }
        throw Exception()
    }
}