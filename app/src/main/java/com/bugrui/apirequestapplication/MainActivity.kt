package com.bugrui.apirequestapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bugrui.apirequestapplication.api.APIService
import com.bugrui.apirequestapplication.data.Gank
import com.bugrui.request.APIRequest
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        fab.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            tv.text = null
            APIRequest.create(APIService::class.java)
                .getData(0)
                .enqueue(object : Callback<Gank> {
                    override fun onFailure(call: Call<Gank>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        tv.text = t.message
                    }

                    override fun onResponse(call: Call<Gank>, response: Response<Gank>) {
                        progressBar.visibility = View.GONE
                        tv.text = response.body().toString()
                    }
                })
        }
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


}
