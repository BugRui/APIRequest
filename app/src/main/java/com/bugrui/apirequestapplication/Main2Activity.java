package com.bugrui.apirequestapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bugrui.apirequestapplication.api.APIService;
import com.bugrui.apirequestapplication.data.Gank;
import com.bugrui.request.APIRequest;
import com.bugrui.request.RequestWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tv;
    private ProgressBar progressBar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        tv = findViewById(R.id.tv);
        progressBar = findViewById(R.id.progressBar);
        fab = findViewById(R.id.fab);

        //初始化
        APIRequest.INSTANCE.init(this, APIService.BASE_URL, new Function1<RequestWrapper, Unit>() {
            @Override
            public Unit invoke(RequestWrapper requestWrapper) {
                requestWrapper.okHttp(new Function2<OkHttpClient.Builder, OkHttpClient.Builder, OkHttpClient.Builder>() {
                    @Override
                    public OkHttpClient.Builder invoke(OkHttpClient.Builder builder, OkHttpClient.Builder builder2) {
                        builder.cache(new Cache(Main2Activity.this.getCacheDir(), 10 * 1024 * 1024L))
                                .addNetworkInterceptor(new HttpLoggingInterceptor()
                                        .setLevel(HttpLoggingInterceptor.Level.BODY));
                        return builder;
                    }
                }).retrofit(new Function2<Retrofit.Builder, Retrofit.Builder, Retrofit.Builder>() {
                    @Override
                    public Retrofit.Builder invoke(Retrofit.Builder builder, Retrofit.Builder builder2) {
                        builder.addConverterFactory(GsonConverterFactory.create(new Gson()));
                        return builder;
                    }
                });
                return Unit.INSTANCE;
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                tv.setText(null);
                APIRequest.INSTANCE.create(APIService.class)
                        .getData(0)
                        .enqueue(new Callback<Gank>() {
                            @Override
                            public void onResponse(Call<Gank> call, Response<Gank> response) {
                                progressBar.setVisibility(View.GONE);
                                tv.setText(response.body().toString());
                            }

                            @Override
                            public void onFailure(Call<Gank> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                tv.setText(t.getMessage());
                            }
                        });
            }
        });


    }
}
