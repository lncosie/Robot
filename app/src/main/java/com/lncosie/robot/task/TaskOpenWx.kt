package com.lncosie.robot.task

import com.lncosie.robot.flow.Envirment
import com.lncosie.toolkit.shell.Shell

/**
 * Created by guazi on 2016/5/5.
 */
class TaskOpenWx : TaskInitiative(){
    override fun start(env: Envirment): Unit {
        Shell.post("am start  -n "+ WechatId.WechatApp+ WechatId.Home)
        wait_moment(5000)
    }
}