package com.duanlu.baseui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.duanlu.adapter.CommonLoadMoreLayout;
import com.duanlu.adapter.DefaultEmptyView;
import com.duanlu.adapter.LoadMoreHelp;
import com.duanlu.adapter.RvBaseAdapter;
import com.duanlu.baseui.R;

/********************************
 * @name BaseRvFragment
 * @author 段露
 * @createDate 2019/8/5 18:14
 * @updateDate 2019/8/5 18:14
 * @version V1.0.0
 * @describe 一个列表类型的Fragment基类.
 ********************************/
public abstract class BaseRvFragment<T> extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener
        , LoadMoreHelp.OnLoadMoreListener {

    protected SwipeRefreshLayout mRefreshLayout;
    protected LinearLayout mLlContainer;
    protected RecyclerView mRvContainer;

    protected RvBaseAdapter<T> mRvAdapter;
    protected View mEmptyView;
    protected CommonLoadMoreLayout mLoadMoreLayout;

    @Override
    public int getLayoutResId() {
        return R.layout.baseui_refresh_recyclerview_layout;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mLlContainer = findViewById(R.id.ll_container);
        mRvContainer = findViewById(R.id.rv_container);

        mRefreshLayout.setOnRefreshListener(this);

        //初始化设置RecyclerView布局管理器并返回适配器
        configLayoutManager();
        mRvAdapter = configAdapter();
        mRvContainer.setAdapter(mRvAdapter);

        //设置默认空布局
        configEmptyLayout();
        //设置默认加载更多布局
        configLoadMoreLayout();
    }

    @Override
    public final void getHttpData() {
        onRefresh();
    }

    public abstract void getHttpData(int pageNo);

    @Override
    public void setHttpData() {
        //列表一般在回调中直接设置列表数据.
    }

    /**
     * 配置设置RecyclerView布局管理器并返回适配器
     *
     * @return 适配器
     */
    @NonNull
    protected abstract RvBaseAdapter<T> configAdapter();

    /**
     * 默认配置LinearLayoutManager管理器.
     */
    protected void configLayoutManager() {
        mRvContainer.setLayoutManager(new LinearLayoutManager(mContext));
    }

    /**
     * 设置空布局.
     */
    protected void configEmptyLayout() {
        mEmptyView = mRvAdapter.getEmptyView();
        if (null == mEmptyView) {
            mEmptyView = new DefaultEmptyView(mContext);
            mRvAdapter.setEmptyView(mEmptyView);
        }
    }

    /**
     * 设置加载更多布局.
     */
    protected void configLoadMoreLayout() {
        mLoadMoreLayout = new CommonLoadMoreLayout(mRvContainer, getPageStartNo(), getPageSize(), this);
        mRvAdapter.setLoadMoreView(mLoadMoreLayout);
    }

    /**
     * 获取分页加载起始页
     *
     * @return 分页加载起始页
     */
    protected int getPageStartNo() {
        return 1;
    }

    protected int getPageSize() {
        return 10;
    }

    @Override
    public void onRefresh() {
        getHttpData(getPageStartNo());
    }

    @Override
    public void onLoadMore(int nextPageNo) {
        getHttpData(nextPageNo);
    }

}