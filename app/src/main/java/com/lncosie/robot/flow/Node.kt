package com.lncosie.robot.flow

import com.lncosie.robot.Config.RobotConfig
import com.lncosie.robot.task.Task
import com.lncosie.toolkit.Ptr

/**
 * Created by lncosie on 2016/5/2.
 */
data class Node(val name:String,val task: Task, val forward: Ptr<Node>, val backward: Ptr<Node>)
fun Node.descript(user:String):String{
    return StringBuilder().run {
        append(this@descript.name)
        append('[')
        append(RobotConfig.wxid)
        append("->")
        append(user)
        append(']')
    }.toString()
}