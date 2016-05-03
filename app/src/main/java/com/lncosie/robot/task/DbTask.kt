package com.lncosie.robot.task

import com.lncosie.robot.bean.UploadRsps
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.utils.HttpUtil
import com.lncosie.robot.utils.ReadDbUtil

/**
 * Created by lncosie on 2016/5/2.
 */
class DbUpload : InitiativeTask() {

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