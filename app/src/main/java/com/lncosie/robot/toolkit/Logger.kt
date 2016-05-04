package com.lncosie.toolkit

import android.util.Log
import com.lncosie.robot.utils.OverlayView

/**
 * Created by lncosie on 2016/5/2.
 */
object Logger {
    var overlay: OverlayView?=null
    fun log(msg:String?){
        overlay?.addLog(msg)
        Log.i("Robot",msg)
    }
    fun loge(msg:String){
        Log.e("Robot",msg)
    }
}
