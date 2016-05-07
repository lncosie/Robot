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
object ThreadMakebookAlive{
    fun loopMakebook(){
        thread {
            while (true) {
                val qlist= Orm.load(BookSendUriList::class.java,null,null)
                for(user in qlist){
                    if(user.uri==null||user.uri.length==0) {
                        val uri = queryBookUri(user)
                        if (uri != null) {
                            user.uri = uri
                            Orm.save(qlist)
                        }
                    }
                }
            }
        }
    }
    private fun queryBookUri(upcode:BookSendUriList):String?{
        Thread.sleep(TimeoutConfig.Time_QueryMakeBook)
        val loopRsps = HttpUtil.loopMsg(RobotConfig.wxid, upcode.userid, upcode.upcode)
        return loopRsps?.message
    }
}