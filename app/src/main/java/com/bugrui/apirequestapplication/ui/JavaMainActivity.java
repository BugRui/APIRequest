package com.bugrui.apirequestapplication.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugrui.apirequestapplication.JavaApp;
import com.bugrui.apirequestapplication.R;
import com.bugrui.apirequestapplication.api.APIService;
import com.bugrui.apirequestapplication.api.ApiResponse;
import com.bugrui.apirequestapplication.data.Gank;
import com.bugrui.request.APIRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class JavaMainActivity extends AppCompatActivity implements OnRefreshLoadMoreListener {

    private MainAdapter mAdapter = new MainAdapter();
    private int mPage = 8;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        refreshLayout = findViewById(R.id.refreshLayout);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new GridItemDecoration(2, KotlinMainActivityKt.getDp(6), true));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        fab.show();
                        Glide.with(JavaApp.sApp).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        fab.hide();
                        Glide.with(JavaApp.sApp).pauseRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        fab.hide();
                        Glide.with(JavaApp.sApp).resumeRequests();
                        break;
                }
            }
        });
        fab.setOnClickListener(v -> {
            mRecyclerView.smoothScrollToPosition(0);
        });

        refreshLayout.setOnRefreshLoadMoreListener(this);
        refreshLayout.autoRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getList(false);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getList(true);
    }

    private APIService apiService = APIRequest.INSTANCE.create(APIService.class);

    private void getList(boolean isRefresh) {
        int page;
        if (isRefresh) {
            page = 8;
        } else {
            page = mPage;
        }
        apiService.getData(page)
                .observe(this, new Observer<ApiResponse<List<Gank>>>() {
                    @Override
                    public void onChanged(ApiResponse<List<Gank>> it) {
                        refreshLayout.finishRefresh(it.isSuccessful());
                        refreshLayout.finishLoadMore(it.isSuccessful());
                        if (it.isSuccessful()) {
                            if (isRefresh) {
                                mPage = 9;
                                mAdapter.setDatas(it.getResults());
                                refreshLayout.setNoMoreData(false);
                            } else {
                                mPage++;
                                mAdapter.addDatas(it.getResults());
                                if ( mAdapter.getMDatas().isEmpty()) {
                                    refreshLayout.finishLoadMoreWithNoMoreData();
                                }
                            }
                        } else {
                            Toast.makeText(JavaApp.sApp, it.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
