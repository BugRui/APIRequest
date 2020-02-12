package com.bugrui.request

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @Author:            BugRui
 * @CreateDate:        2020/2/11 11:48
 * @Description:       添加头拦截器
 */
class HeaderInterceptor : Interceptor {

    private var headers = hashMapOf<String, String>()

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        headers.forEach { (t, u) ->
            requestBuilder.addHeader(t, u)
        }
        return chain.proceed(requestBuilder.build())
    }

    fun put(key: String, value: String): HeaderInterceptor {
        headers[key] = value
        return this
    }

    fun put(headers: HashMap<String, String>): HeaderInterceptor {
        this.headers.putAll(headers)
        return this
    }
}