package com.lncosie.robot.task

import com.lncosie.robot.Config.RobotConfig
import com.lncosie.robot.Config.TimeoutConfig
import com.lncosie.robot.log.BookSendUriList
import com.lncosie.robot.orm.Orm
import com.lncosie.robot.utils.HttpUtil
import kotlin.concurrent.thread

/**
 * Created by guazi on 2016/5/6.
 */
object ThreadCheckMakebook {
    fun loop() {
        thread(true, true) {
            while (true) {
                try {
                    val qlist = Orm.load(BookSendUriList::class.java, null, null)
                    var has = false
                    for (user in qlist) {
                        if (user.uri == null || user.uri.length == 0) {
                            has = true
                            val uri = queryBookUri(user)
                            if (uri != null) {
                                user.uri = uri
                                Orm.save(user)
                            }
                        }
                    }
                    if (!has)
                        Thread.sleep(60000)
                } catch(e: Exception) {

                }

            }
        }
    }

    private fun queryBookUri(upcode: BookSendUriList): String? {
        try {
            Thread.sleep(TimeoutConfig.Time_QueryMakeBook)
            val loopRsps = HttpUtil.loopMsg(RobotConfig.wxid, upcode.userid, upcode.upcode)
            return loopRsps?.message
        } catch(e: Exception) {
            return null
        }
    }
}