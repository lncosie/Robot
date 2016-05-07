package com.lncosie.toolkit

import android.util.Log
import com.lncosie.robot.log.Robot
import com.lncosie.robot.orm.Orm
import com.lncosie.robot.utils.OverlayView

/**
 * Created by lncosie on 2016/5/2.
 */
object Logger {
    var overlay: OverlayView? = null
    fun log(msg: String?) {
        overlay?.addLog(msg)
        Log.i("Robot", msg)
    }

    fun logi(msg: String) {
        Log.i("Robot", msg)
    }

    fun exEnable() {
        Thread.setDefaultUncaughtExceptionHandler { thread, e ->
            val ex = Robot()
            ex.message = e.toString()
            Orm.save(ex)
        }
    }
}
