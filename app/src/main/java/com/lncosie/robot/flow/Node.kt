package com.lncosie.robot.flow

import com.lncosie.robot.task.Task
import com.lncosie.toolkit.Ptr

/**
 * Created by lncosie on 2016/4/29.
 */
data class Node(val task: Task, val forward: Ptr<Node>, val backward: Ptr<Node>)
fun Node.descript():String{
    return StringBuilder().run {
        append(this@descript.task.toString())
        append("[OK->")
        if(forward.notNull())
            append(forward.value.task.toString())
        append("   Fail->")
        if(backward.notNull())
            append(backward.value.task.toString())
        append("]")
    }.toString()
}