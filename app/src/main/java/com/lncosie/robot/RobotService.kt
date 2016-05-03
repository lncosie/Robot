package com.lncosie.wxrobot

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import com.lncosie.robot.flow.*
import com.lncosie.robot.utils.OverlayView

/**
 * Created by lncosie on 2016/5/2.
 */

class RobotService : AccessibilityService() {
    var overlay: OverlayView?=null

    val runner= WorkflowRunner(Envirment("","","","","","",this))
    val workflow= WorkFlow()
    lateinit var start: Node

    override fun onCreate() {
        super.onCreate()
        workflow.make()
        //showOverlay()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val stop=intent?.getBooleanExtra("stop",false)?:false
        if(stop){
            stop()
        }else{
            val action=intent?.getBooleanExtra("auto",false)?:false
            start(action)
        }
        return super.onStartCommand(intent, flags, startId)
    }
    private fun stop(){
        runner.stop(workflow.finish())
    }
    private fun start(auto: Boolean) {
        //runner.stop(workflow.finish())
        start=if(auto)workflow.autoWork()else workflow.manWork()
        runner.start(start)
    }
    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
        //hideOverlay()
    }

    override fun onInterrupt() {
        runner.stop(workflow.finish())
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if(event==null||event.source==null)
            return
        val type=event!!.eventType
        if(event.source.refresh())
            runner.step(Event(this, event!!))
        //if(type==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED||type==AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)

    }
    private fun hideOverlay() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.removeView(overlay)
        overlay = null
    }

    private fun showOverlay() {
        overlay = OverlayView(applicationContext)
        val params = WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSPARENT)
        params.title = "RobotWechat"
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.addView(overlay, params)
    }
}
