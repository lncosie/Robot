package com.lncosie.toolkit.shell

import java.io.BufferedOutputStream
import java.io.BufferedWriter
import java.io.OutputStreamWriter

/**
 * Created by lncosie on 2016/5/2.
 */
object Shell{
    var process:Process?=null

    var cs:OutputStreamWriter?=null
    fun post(command:String){
        open()
        cs!!.write(command)
        cs!!.write("\n")
        cs!!.flush()
    }
    fun open(){
        if(process==null)
        {
            process= Runtime.getRuntime().exec("su")
            cs= OutputStreamWriter(process!!.outputStream)
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