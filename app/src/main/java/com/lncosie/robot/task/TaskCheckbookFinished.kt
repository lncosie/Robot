package com.lncosie.robot.task

import com.lncosie.robot.books.MakebookList
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.flow.NoElementFound
import com.lncosie.robot.log.Orm


/**
 * Created by guazi on 2016/5/5.
 */
class TaskCheckbookFinished(): TaskInitiative(){
    override fun start(env: Envirment) {
        val finished= Orm.load(MakebookList::class.java)
        finished.forEach {
            env.msg=it.uri
            env.usernick=it.user
            return@start
        }
        throw NoElementFound()
    }
}