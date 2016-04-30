package com.lncosie.wxrobot

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.lncosie.robot.flow.*

/**
 * Created by lncosie on 2016/4/29.
 */

class RobotService : AccessibilityService() {

    val runner= FlowRunner(Envirment("user","welcome",this))
    val workflow= WorkFlow()
    lateinit var start: Node

    override fun onCreate() {
        super.onCreate()
        workflow.make_flow()
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
        runner.stop(workflow.finish())
        start=if(auto)workflow.autoWork()else workflow.manWork()
        runner.start(start)
    }
    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    override fun onInterrupt() {
        runner.stop(workflow.finish())
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        runner.step(Event(this, event!!))
    }
}
