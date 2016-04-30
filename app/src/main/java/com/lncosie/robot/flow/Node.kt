package com.lncosie.robot.flow

import com.lncosie.robot.task.Task
import com.lncosie.toolkit.Ptr

/**
 * Created by lncosie on 2016/4/29.
 */
data class Node(val task: Task, val forward: Ptr<Node>, val backward: Ptr<Node>)
