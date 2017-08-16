package com.z7dream.android_recycler_control;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * 非侵入式的recyclerView 控制器。
 * noninvasive's recyclerview control.
 * 可以对带有SwipeRefreshLayout 和 recyclerView 页面进行下拉刷新和上拉加载，及无数据时候的控制
 */
public class RecyclerControl {
    private boolean isRefComplete = true, isLoadMoreComplete = true;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private OnControlGetDataListListener onControlGetDataListListener;
    private OnScrollListener onScrollListener;
    private Disposable delay1Disposable, delay2Disposable;
    private GestureDetector mGestureDetector;
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener;
    private boolean isSwipeRefreshLayoutEnable = true;
    private LOAD_STATE load_state;
    private EmptyViewControl emptyViewControl;

    public static final int[] COLORS = {0xff2196F3};


    /**
     * 初始化
     *
     * @param swipeRefreshLayout           刷新控件
     * @param layoutManager                线性布局管理
     * @param onControlGetDataListListener 数据获取监听
     */
    public RecyclerControl(SwipeRefreshLayout swipeRefreshLayout,
                           LinearLayoutManager layoutManager, OnControlGetDataListListener onControlGetDataListListener) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.linearLayoutManager = layoutManager;
        this.onControlGetDataListListener = onControlGetDataListListener;

        onScrollListener = new OnScrollListener();
        swipeRefreshLayout.setColorSchemeColors(COLORS);
        load_state = LOAD_STATE.scrolling;
    }

    /**
     * 初始化
     *
     * @param swipeRefreshLayout           刷新控件
     * @param gridLayoutManager            网格布局管理
     * @param onControlGetDataListListener 数据获取监听
     */
    public RecyclerControl(SwipeRefreshLayout swipeRefreshLayout,
                           GridLayoutManager gridLayoutManager, OnControlGetDataListListener onControlGetDataListListener) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.gridLayoutManager = gridLayoutManager;
        this.onControlGetDataListListener = onControlGetDataListListener;

        onScrollListener = new OnScrollListener();
        swipeRefreshLayout.setColorSchemeColors(COLORS);
        load_state = LOAD_STATE.scrolling;
    }

    /**
     * 初始化 空白页面
     *
     * @param viewGroup 可以设置为swipeRefreshLayout
     */
    public void initEmptyControl(ViewGroup viewGroup) {
        emptyViewControl = new EmptyViewControl<>(viewGroup);
    }

    /**
     * 初始化 空白页面
     *
     * @param viewGroup          可以设置为swipeRefreshLayout
     * @param errorClickListener 错误点击
     */
    public void initEmptyControl(ViewGroup viewGroup, EmptyViewControl.ErrorClickListener errorClickListener) {
        emptyViewControl = new EmptyViewControl<>(viewGroup);
        emptyViewControl.setOnErrorClickListener(errorClickListener);
    }

    /**
     * 设置是否可以下拉刷新
     *
     * @param b 是否下拉刷新
     */
    public void setSwipeRefreshLayoutEnable(boolean b) {
        isSwipeRefreshLayoutEnable = b;
        swipeRefreshLayout.setEnabled(isSwipeRefreshLayoutEnable);
    }

    /**
     * 设置什么时候加载
     *
     * @param loadState {@link LOAD_STATE}
     */
    public void setLoadState(LOAD_STATE loadState) {
        this.load_state = loadState;
    }

    /**
     * 调用刷新
     */
    public void onRefresh() {
        if (isRefComplete) {
            isRefComplete = false;
            delayGetData(true);
        }
    }

    /**
     * 获取数据完成时调用
     *
     * @param isRef 是否为下拉刷新
     */
    public void getDataComplete(boolean isRef) {
        if (isRef) {
            isRefComplete = true;
        } else {
            isLoadMoreComplete = true;
        }
        delayComplete();
    }

    public void getDataEmpty(RecyclerView.Adapter adapter, int minAdapterCount) {
        if (emptyViewControl == null) return;
        if (adapter.getItemCount() <= minAdapterCount) {
            emptyViewControl.displayErrorEmpty();
        } else {
            emptyViewControl.unDisplayError();
        }
    }

    public void getDataError(RecyclerView.Adapter adapter, int minAdapterCount) {
        if (emptyViewControl == null) return;
        if (adapter.getItemCount() <= minAdapterCount) {
            emptyViewControl.displayErrorNet();
        } else {
            emptyViewControl.unDisplayError();
        }
    }

    /**
     * 判断是否刷新/加载结束
     *
     * @return 是否刷新/加载结束
     */
    public boolean isRefComplete() {
        return isRefComplete;
    }

    /**
     * 如果希望有上拉加载时使用
     *
     * @return 滚动监听
     */
    public OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    /**
     * 销毁，在onDestory中调用
     */
    public void destory() {
        if (delay1Disposable != null && !delay1Disposable.isDisposed()) {
            delay1Disposable.dispose();
        }
        if (delay2Disposable != null && !delay2Disposable.isDisposed()) {
            delay2Disposable.dispose();
        }

        swipeRefreshLayout = null;
        linearLayoutManager = null;
        gridLayoutManager = null;
        onControlGetDataListListener = null;
        onScrollListener = null;

        if (emptyViewControl != null) {
            emptyViewControl.destory();
            emptyViewControl = null;
        }
    }

    private void delayGetData(final boolean isRef) {
        delay1Disposable = Observable.just(1).delay(100, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Integer, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(Integer integer) throws Exception {
                        if (isSwipeRefreshLayoutEnable) {
                            swipeRefreshLayout.setEnabled(false);
                        }
                        swipeRefreshLayout.setRefreshing(true);
                        return Observable.just(1).delay(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        onControlGetDataListListener.onControlGetDataList(isRef);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable o) throws Exception {

                    }
                });

    }

    private void delayComplete() {
        delay2Disposable = Observable.just(1).delay(300, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (swipeRefreshLayout == null) return;
                        if (isSwipeRefreshLayoutEnable) {
                            swipeRefreshLayout.setEnabled(true);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private class OnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (isLoadMoreComplete && newState == (load_state == LOAD_STATE.scrolling ? RecyclerView.SCROLL_STATE_DRAGGING : RecyclerView.SCROLL_STATE_IDLE)) {
                int lastViewPos = -1;
                if (linearLayoutManager != null)
                    lastViewPos = linearLayoutManager.findLastVisibleItemPosition();
                if (gridLayoutManager != null)
                    lastViewPos = gridLayoutManager.findLastVisibleItemPosition();

                //当最后一个item的pos小于总item数量-1
                if (lastViewPos >= 0 && lastViewPos >= (recyclerView.getAdapter().getItemCount() - 1)) {
                    if (isLoadMoreComplete) {
                        isLoadMoreComplete = false;
                        delayGetData(false);
                    }
                }
            }
            if (onControlGetDataListListener != null)
                onControlGetDataListListener.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (onControlGetDataListListener != null)
                onControlGetDataListListener.onScrolled(recyclerView, dx, dy);
        }
    }

    /**
     * 手势
     *
     * @param recyclerView recyclerView
     * @param listener     手势监听
     */
    public void openGestureDetector(RecyclerView recyclerView, final GestureListener listener) {
        if (simpleOnGestureListener == null) {
            simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return listener.onFling(e1, e2, velocityX, velocityY);
                }
            };
        }

        if (mGestureDetector == null) {
            mGestureDetector = new GestureDetector(recyclerView.getContext(), simpleOnGestureListener);
        }
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mGestureDetector != null) {
                    return mGestureDetector.onTouchEvent(motionEvent);
                }
                return false;
            }
        });
    }

    public interface GestureListener {
        boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);
    }

    public interface OnControlGetDataListListener {
        /**
         * 调用网路数据方法
         *
         * @param isRef 是否是刷新
         */
        void onControlGetDataList(boolean isRef);

        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    /**
     * 加载状态
     */
    public enum LOAD_STATE {
        scrolling,//滑动中
        scrollEnd//滑动结束
    }
}
