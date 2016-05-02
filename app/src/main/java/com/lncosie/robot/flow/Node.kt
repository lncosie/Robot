package com.lncosie.robot.flow

import com.lncosie.robot.task.Task
import com.lncosie.toolkit.Ptr

/**
 * Created by lncosie on 2016/4/29.
 */
data class Node(val name:String,val task: Task, val forward: Ptr<Node>, val backward: Ptr<Node>)
fun Node.descript():String{
    return StringBuilder().run {
        append(this@descript.name)
        if(forward.notNull()){
            append("[OK->")
            append(forward.value.name)

        }
        if(backward.notNull())
        {
            append("   Fail->")
            append(backward.value.name)
        }
        append("]")
    }.toString()
}