package com.bugrui.request

import android.content.Context
import retrofit2.Retrofit

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/9 15:53
 * @Description:       API请求
 */
class APIRequest {

    companion object {

        fun init(
            context: Context,
            baseUrl: String,
            requestDSL: (RequestWrapper.() -> Unit)
        ) {
            OkHttpEngine.instance.initEngine(context, baseUrl, requestDSL)
        }

        fun <T> create(tClass: Class<T>): T = OkHttpEngine.instance.create(tClass)
    }

}