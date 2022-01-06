package com.liangguo.mediaBrowser.interfaces


/**
 * @author ldh
 * 时间: 2021/12/16 20:16
 * 邮箱: 2637614077@qq.com
 */
interface SingleParamCallBack<T : Any> {
    fun callBack(param: T)
}