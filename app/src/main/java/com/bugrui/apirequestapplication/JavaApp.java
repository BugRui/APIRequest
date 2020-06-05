package com.bugrui.apirequestapplication;

import android.app.Application;

import com.bugrui.apirequestapplication.api.APIService;
import com.bugrui.apirequestapplication.api.LiveDataCallAdapterFactory;
import com.bugrui.request.APIRequest;
import com.bugrui.request.RequestWrapper;
import com.google.gson.Gson;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JavaApp extends Application {

    public static Application sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        //初始化
        APIRequest.INSTANCE.init(this, APIService.BASE_URL, new Function1<RequestWrapper, Unit>() {
            @Override
            public Unit invoke(RequestWrapper requestWrapper) {
                requestWrapper.okHttp(new Function2<OkHttpClient.Builder, OkHttpClient.Builder, OkHttpClient.Builder>() {
                    @Override
                    public OkHttpClient.Builder invoke(OkHttpClient.Builder builder, OkHttpClient.Builder builder2) {
                        builder.cache(new Cache(JavaApp.this.getCacheDir(), 10 * 1024 * 1024L))
                                .addNetworkInterceptor(new HttpLoggingInterceptor()
                                        .setLevel(HttpLoggingInterceptor.Level.BODY));
                        return builder;
                    }
                }).retrofit(new Function2<Retrofit.Builder, Retrofit.Builder, Retrofit.Builder>() {
                    @Override
                    public Retrofit.Builder invoke(Retrofit.Builder builder, Retrofit.Builder builder2) {
                        builder.addConverterFactory(GsonConverterFactory.create(new Gson()));
                        builder.addCallAdapterFactory(new LiveDataCallAdapterFactory());
                        return builder;
                    }
                });
                return Unit.INSTANCE;
            }
        });
    }
}
