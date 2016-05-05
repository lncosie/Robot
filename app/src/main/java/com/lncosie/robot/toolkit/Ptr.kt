package com.lncosie.toolkit

/**
 * Created by lncosie on 2016/5/2.
 */
class Ptr<T>(var ptr:(()-> T)?=null) {
    constructor(other: Ptr<T>):this({other.value}){
    }
    constructor(init:T):this({init}){
    }
    fun reset(fn:()-> T):Ptr<T>{
        ptr =fn
        return this
    }
    fun reset(init: T):Ptr<T>{
        ptr={init}
        return this
    }
    fun notNull()=ptr!=null
    val  value by lazy { ptr!!()}
}