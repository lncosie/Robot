package com.lncosie.toolkit

/**
 * Created by lncosie on 2016/5/2.
 */
class Ptr<T>(var ptr:(()-> T)?=null) {
    constructor(other: Ptr<T>):this({other.value}){
    }
    constructor(init:T):this({init}){
    }
    fun reset(fn:()-> T){
        ptr =fn
    }
    fun reset(init: T){
        ptr={init}
    }
    fun notNull()=ptr!=null
    val  value by lazy { ptr!!()}
}