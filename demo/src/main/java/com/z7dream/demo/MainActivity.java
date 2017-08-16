package com.z7dream.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.z7dream.android_recycler_control.EmptyViewControl;
import com.z7dream.android_recycler_control.RecyclerControl;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RecyclerControl.OnControlGetDataListListener, SwipeRefreshLayout.OnRefreshListener,
        EmptyViewControl.ErrorClickListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private RecyclerControl recyclerControl;
    private DemoAdapter demoAdapter;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        demoAdapter = new DemoAdapter();
        recyclerView.setAdapter(demoAdapter);

        recyclerControl = new RecyclerControl(swipeRefreshLayout, linearLayoutManager, this);//初始化
        recyclerControl.initEmptyControl(swipeRefreshLayout, this);//初始化无数据页面
        recyclerControl.setSwipeRefreshLayoutEnable(true);
        recyclerView.addOnScrollListener(recyclerControl.getOnScrollListener());//滚动监听
        swipeRefreshLayout.setOnRefreshListener(this);

        handler = new Handler(Looper.myLooper());

        onRefresh();
    }

    @Override
    public void onControlGetDataList(final boolean isRef) {
        //todo get data
        handler.postDelayed(new Runnable() {//模拟刷新数据
            @Override
            public void run() {
                List<DemoAdapter.Model> list = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    DemoAdapter.Model model = new DemoAdapter.Model();
//                    model.text = String.valueOf(i);
//                    list.add(model);
//                }

                demoAdapter.setDataList(list, isRef);
                demoAdapter.notifyDataSetChanged();

                recyclerControl.getDataComplete(isRef);//刷新结束
                recyclerControl.getDataEmpty(demoAdapter, 0);
            }
        }, 1000);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    @Override
    public void onRefresh() {
        recyclerControl.onRefresh();//调用刷新球
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerControl.destory();
        recyclerControl = null;
    }

    @Override
    public void emptyClickListener() {
        onRefresh();
    }
}
