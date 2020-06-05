package com.bugrui.apirequestapplication.ui

import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bugrui.apirequestapplication.KotlinApp
import com.bugrui.apirequestapplication.R
import com.bugrui.apirequestapplication.api.APIService
import com.bugrui.request.APIRequest
import com.bumptech.glide.Glide
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class KotlinMainActivity : AppCompatActivity(), OnRefreshLoadMoreListener {

    private val mAdapter = MainAdapter()
    private var mPage = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.apply {

            itemAnimator = DefaultItemAnimator()

            val gridLayoutManager = GridLayoutManager(this@KotlinMainActivity, 2)

            layoutManager = gridLayoutManager
            adapter = mAdapter

            addItemDecoration(GridItemDecoration(2, 6.dp, true))

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    when (newState) {
                        RecyclerView.SCROLL_STATE_IDLE -> {
                            fab.show()
                            Glide.with(KotlinApp.sApp).resumeRequests()
                        }
                        RecyclerView.SCROLL_STATE_DRAGGING -> {
                            fab.hide()
                            Glide.with(KotlinApp.sApp).pauseRequests()
                        }
                        RecyclerView.SCROLL_STATE_SETTLING -> {
                            fab.hide()
                            Glide.with(KotlinApp.sApp).resumeRequests()
                        }
                    }
                }
            })
        }

        fab.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }


        refreshLayout.setOnRefreshLoadMoreListener(this)
        refreshLayout.autoRefresh()

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        getList(false)
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        getList(true)
    }


    private val apiService = APIRequest.create(APIService::class.java)

    private fun getList(isRefresh: Boolean) {
        apiService.getData(if (isRefresh) 8 else mPage)
            .observe(this, Observer {
                refreshLayout.finishRefresh(it.isSuccessful())
                refreshLayout.finishLoadMore(it.isSuccessful())
                if (it.isSuccessful()) {
                    if (isRefresh) {
                        mPage = 9
                        mAdapter.setDatas(it.results)
                        refreshLayout.setNoMoreData(false)
                    } else {
                        mPage++
                        mAdapter.addDatas(it.results)
                        if (mAdapter.mDatas.isNullOrEmpty()) {
                            refreshLayout.finishLoadMoreWithNoMoreData()
                        }
                    }
                } else {
                    Toast.makeText(KotlinApp.sApp, it.msg, Toast.LENGTH_SHORT).show()
                }
            })

    }


}

/**
 * 转dp
 */
val Int.dp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()