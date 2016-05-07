package com.lncosie.robot.task

import android.text.TextUtils
import com.lncosie.robot.Config.RobotConfig
import com.lncosie.robot.bean.UploadRsps
import com.lncosie.robot.flow.Envirment
import com.lncosie.robot.log.BookSendUriList
import com.lncosie.robot.orm.Orm
import com.lncosie.robot.utils.HttpUtil
import com.lncosie.robot.utils.ReadDbUtil
import com.lncosie.toolkit.Logger


/**
 * Created by lncosie on 2016/5/2.
 */
class TaskDbUpload : TaskInitiative() {

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
            rsps = HttpUtil.postData(env.upload, RobotConfig.wxid, env.userid, "sn")
            if (rsps != null) {
                break
            }
        }
        if (rsps == null || rsps!!.interval === 0) {
            return
        }
        env.upcode = rsps!!.up_code
        env.msg = rsps!!.message

        addQueryList(env)
    }
    fun addQueryList(env: Envirment){
        val book=BookSendUriList()
        //book.uri=env.msg
        book.upcode=env.upcode
        book.userid=env.userid
        book.user = env.usernick
        Orm.save(book)
    }

    override fun end(env: Envirment) {

    }
}
