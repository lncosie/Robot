package com.lncosie.toolkit

/**
 * Created by lncosie on 2016/4/30.
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
    val  value by lazy { ptr!!()}
}