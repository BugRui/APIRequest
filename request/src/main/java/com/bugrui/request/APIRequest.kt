package com.bugrui.request

import android.content.Context
import retrofit2.Retrofit

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/9 15:53
 * @Description:       API请求
 */
object APIRequest {

    private lateinit var appContext: Context
    lateinit var baseUrl: String
    private lateinit var retrofit: Retrofit

    private lateinit var requestDSL: (RequestWrapper.() -> Unit)


    fun init(
        context: Context,
        baseUrl: String,
        requestDSL: (RequestWrapper.() -> Unit)
    ) {
        appContext = context.applicationContext
        APIRequest.baseUrl = baseUrl
        APIRequest.requestDSL = requestDSL

        execute(RequestWrapper().apply(requestDSL))
    }

    private fun execute(wrap: RequestWrapper) {

        val client = wrap.okHttpBuilder
            .build()

        //多线程并发最大数
        client.dispatcher().maxRequests = 100
        client.dispatcher().maxRequestsPerHost = 10

        retrofit = wrap.retrofitBuilder
            .baseUrl(baseUrl)
            .client(client)
            .build()
    }


    /**
     * 重置baseUrl
     */
    fun resetBaseUrl(newBaseUrl: String): Boolean {
        val isOK = !newBaseUrl.isBlank()
                && (baseUrl.startsWith("http://")
                || baseUrl.startsWith("https://"))
        check(isOK) { "baseUrl is illegal: $baseUrl" }
        val isChanged = isOK && baseUrl != newBaseUrl
        if (isChanged) {
            init(appContext, baseUrl, requestDSL)
        }
        return isChanged
    }


    fun <T> create(tClass: Class<T>): T = retrofit.create(tClass)

}