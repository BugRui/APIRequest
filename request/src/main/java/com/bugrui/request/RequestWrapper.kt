package com.bugrui.request

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @Author:            BugRui
 * @CreateDate:        2020/2/11 14:39
 * @Description:       请求包装器
 */
class RequestWrapper {

    lateinit var okHttpBuilder: OkHttpClient.Builder
    lateinit var retrofitBuilder: Retrofit.Builder

    val headers: HeaderInterceptor = HeaderInterceptor()

    inline fun okHttp(okHttpBuilder: OkHttpClient.Builder.(OkHttpClient.Builder) -> OkHttpClient.Builder): RequestWrapper {

        //信任所有证书
        val sslParams: HttpsSSLParamsUtils.SSLParams =
            HttpsSSLParamsUtils.getSslSocketFactory()

        val builder = OkHttpClient.Builder()
            .connectTimeout(6, TimeUnit.SECONDS)    //连接默认超时时间(秒)
            .writeTimeout(12, TimeUnit.SECONDS)     //连接默认写超时时间(秒)
            .readTimeout(12, TimeUnit.SECONDS)      //连接默认读超时时间(秒)
            .sslSocketFactory(sslParams.sSLSocketFactory!!, sslParams.trustManager!!)
            .hostnameVerifier(sslParams.unSafeHostnameVerifier)
            .addInterceptor(headers)

        this.okHttpBuilder = okHttpBuilder.invoke(builder, builder)
        return this
    }


    inline fun retrofit(retrofitBuilder: Retrofit.Builder.(Retrofit.Builder) -> Retrofit.Builder): RequestWrapper {
        val builder = Retrofit.Builder()
        this.retrofitBuilder = retrofitBuilder.invoke(builder, builder)
        return this
    }

    /**
     * 添加请求头
     */
    fun addHeader(key: String, value: String): RequestWrapper {
        headers.put(key, value)
        return this
    }

}

