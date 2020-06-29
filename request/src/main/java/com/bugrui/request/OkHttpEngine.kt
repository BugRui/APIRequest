package com.bugrui.request

import android.content.Context
import retrofit2.Retrofit

class OkHttpEngine {

    companion object {
        val instance: OkHttpEngine by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            OkHttpEngine()
        }
    }

    private lateinit var appContext: Context
    private lateinit var baseUrl: String
    private lateinit var retrofit: Retrofit
    private lateinit var requestDSL: (RequestWrapper.() -> Unit)

    fun initEngine(
        context: Context,
        baseUrl: String,
        requestDSL: (RequestWrapper.() -> Unit)
    ) {
        this.appContext = context.applicationContext
        this.baseUrl = baseUrl
        this.requestDSL = requestDSL

        execute(RequestWrapper().apply(requestDSL))
    }

    private fun execute(wrap: RequestWrapper) {

        val client = wrap.okHttpBuilder
            .build()

        //多线程并发最大数
        client.dispatcher().maxRequests = 100
        client.dispatcher().maxRequestsPerHost = 10

        this.retrofit = wrap.retrofitBuilder
            .baseUrl(this.baseUrl)
            .client(client)
            .build()
    }

    fun getBaseUrl(): String = baseUrl

    fun <T> create(tClass: Class<T>): T = retrofit.create(tClass)

}