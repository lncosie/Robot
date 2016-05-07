package com.lncosie.robot.task

import android.text.TextUtils
import com.lncosie.robot.Config.RobotConfig
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.utils.HttpUtil
import com.lncosie.toolkit.Logger

/**
 * Created by guazi on 2016/5/7.
 */

class TaskWaitMakeBook : TaskInitiative() {
    override fun start(env: Envirment) {
        for (i in 0..1499) {
            val loopRsps = HttpUtil.loopMsg(RobotConfig.wxid, env.userid, env.upcode)
            if (loopRsps == null) {
                Logger.log("轮询出现问题")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                continue
            }
            var interval = 1000
            if (TextUtils.isEmpty(loopRsps.message)) {
                if (loopRsps.interval > 0) {
                    //interval = loopRsps.interval;
                    //不管服务器怎么指定了直接写死3秒
                    interval = 3
                }
                try {
                    Thread.sleep((interval * 1000).toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                continue
            }
            env.msg = loopRsps.message
            return
        }

    }
}