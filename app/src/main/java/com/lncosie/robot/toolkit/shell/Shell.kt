package com.lncosie.toolkit.shell

import java.io.BufferedOutputStream
import java.io.BufferedWriter

/**
 * Created by lncosie on 2016/4/30.
 */
object Shell{
    var process:Process?=null
    var cs:BufferedOutputStream?=null
    fun post(command:String){
        cs?.write((command+"\n").toByteArray())
        cs?.flush()
    }
    fun open(){
        if(process==null)
        {
            process= Runtime.getRuntime().exec("su")
            cs=BufferedOutputStream(process!!.outputStream)
        }
    }
    fun close(){
        if(process!=null){
            cs?.close()
            process?.destroy()
            cs=null
            process=null
        }
    }
}