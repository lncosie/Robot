package com.lncosie.robot.task

import android.text.TextUtils
import com.lncosie.robot.bean.UploadRsps
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.utils.HttpUtil
import com.lncosie.robot.utils.ReadDbUtil
import com.lncosie.toolkit.Logger


/**
 * Created by lncosie on 2016/5/2.
 */
class DbUpload : TaskInitiative() {

    override fun start(env: Envirment): Unit {
        try {
            if (!ReadDbUtil.readDataBase(env.context, env)) {
                return
            }
        } catch (e: Exception) {
            return
        }

        var rsps: UploadRsps? = null
        for (i in 0..9) {
            rsps = HttpUtil.postData(env.upload, env.wxid, env.userid, "sn")
            if (rsps != null) {
                break
            }
        }
        if (rsps == null || rsps!!.interval === 0) {
            return
        }
        env.upcode = rsps!!.up_code
        env.msg = rsps!!.message
    }

    override fun end(env: Envirment) {

    }
}

class WaitMakeBook : TaskInitiative() {
    override fun start(env: Envirment) {
        for (i in 0..1499) {
            val loopRsps = HttpUtil.loopMsg(env.wxid, env.userid, env.upcode)
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