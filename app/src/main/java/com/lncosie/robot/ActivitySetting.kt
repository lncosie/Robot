package com.lncosie.wxrobot

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings

import android.view.View
import com.lncosie.robot.R
import com.lncosie.toolkit.shell.Shell

/**
 * Created by lncosie on 2016/4/29.
 */

class ActivitySetting : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        Shell.open()
    }
    public fun stop(v: View) {
        send("stop",true)
    }
    public fun start_auto(v: View) {
        send("auto",true)
    }
    public fun start_man(v: View) {
        send("auto",false)
    }
    private fun send(flag:String,value:Boolean){
        val intent= Intent(this,RobotService::class.java)
        intent.putExtra(flag,value)
        startService(intent)
    }
    public fun sc(v:View){
        val sc = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(sc)
    }
}
