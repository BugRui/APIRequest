# APIRequest

API网络请求框架，基于封装Retrofit+OkHttp封装，集成简单，方便快捷食用。

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
implementation 'com.github.BugRui:APIRequest:1.0.0'
```

## 在Application中初始化,根据自己需求自己配置

```

//kotlin
APIRequest.init(this, APIService.BASE_URL) {
            okHttp {
                cache(Cache(this@MainActivity.cacheDir, 10 * 1024 * 1024L))
                //日志
                addNetworkInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            retrofit {
                addConverterFactory(GsonConverterFactory.create(Gson()))
            }
        }
        
//Java
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

```

## 食用,具体请看Dome

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
 APIRequest.INSTANCE.create(APIService.class)
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

