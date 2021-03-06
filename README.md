# APIRequest

API网络请求框架，基于封装Retrofit+OkHttp封装，集成简单，方便快捷食用，支持Java，Kotlin。

### 集成
#### Step 1. Add the JitPack repository to your build file
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

```
####  Step 2. Add the dependency
```
implementation 'com.github.BugRui:APIRequest:1.0.2'
```

####  Step 3. Java1.8
```

compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

```


## 在Application中初始化,根据自己需求自己配置

```


//kotlin
APIRequest.init(this, APIService.BASE_URL) {
            okHttp { //okHttpBuilder 
                cache(Cache(this@MainActivity.cacheDir, 10 * 1024 * 1024L))
                //日志
                addNetworkInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })

            }
            retrofit { //retrofitBuilder 
                addConverterFactory(GsonConverterFactory.create(Gson()))
            }

            //添加请求头
            addHeader("key","value")
        }
        
//Java
 APIRequest.Companion.init(this, APIService.BASE_URL, new Function1<RequestWrapper, Unit>() {
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

                //添加请求头
                requestWrapper.addHeader("key","value");

                return Unit.INSTANCE;
            }
        });

```

## 使用,具体请看Dome

```

interface APIService {
    companion object {
        const val BASE_URL = "http://gank.io/"
    }
    @GET("api/data/福利/20/{page}")
    fun getData(@Path("page") page: Int): Call<Gank>
}

```



```
//Kotlin
 APIRequest.create(APIService::class.java)
                .getData(0)
                .enqueue(object : Callback<Gank> {
                    override fun onFailure(call: Call<Gank>, t: Throwable) {
                       
                    }

                    override fun onResponse(call: Call<Gank>, response: Response<Gank>) {
                       
                    }
                })
                
//Java         
 APIRequest.Companion.create(APIService.class)
                        .getData(0)
                        .enqueue(new Callback<Gank>() {
                            @Override
                            public void onResponse(Call<Gank> call, Response<Gank> response) {
                               
                            }

                            @Override
                            public void onFailure(Call<Gank> call, Throwable t) {
                              
                            }
                        });
```

