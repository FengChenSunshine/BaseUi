package com.duanlu.baseui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/********************************
 * @name LoadHelp
 * @author 段露
 * @createDate 2019/3/23  17:19.
 * @updateDate 2019/3/23  17:19.
 * @version V1.0.0
 * @describe 加载更多帮助类.
 ********************************/
public class LoadHelp {

    public static final int STATUS_IDLE = 0;//空闲.
    public static final int STATUS_EMPTY = 1;//空数据.
    public static final int STATUS_WAIT_LOAD = 2;//不自动加载时等待加载状态.
    public static final int STATUS_LOADING = 3;//加载更多数据中.
    public static final int STATUS_ERROR = 4;//加载失败.
    public static final int STATUS_LAST_PAGE = 5;//已经是最后一页了.

    private RecyclerView mRecyclerView;

    private int mScrollState = -1;

    private int mStatus;
    private boolean isAutoLoadMore = true;
    private int mStartPageNo;
    private int mNextPageNo;
    private int mPageSize;//分页大小.
    private int mThresholdValue;//阈值.

    private OnLoadMoreListener mOnLoadMoreListener;
    private OnLoadMoreStatusLayoutCallback mOnLoadMoreStatusLayoutCallback;

    private boolean isReverseMode;

    public LoadHelp(@NonNull RecyclerView recyclerView, int pageNo, int pageSize, @NonNull OnLoadMoreListener listener) {
        this.mRecyclerView = recyclerView;
        this.mOnLoadMoreListener = listener;

        this.mStartPageNo = pageNo;
        resetNextPageNo();
        this.mPageSize = pageSize;
        this.mThresholdValue = mPageSize / 2;

        addOnScrollListener();
    }

    public void setReverseMode(boolean reverseMode) {
        this.isReverseMode = reverseMode;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setLoadMoreSuccess(boolean isEmptyData, boolean isLastPage) {
        if (isEmptyData) {
            mStatus = STATUS_EMPTY;
            resetNextPageNo();
        } else if (isLastPage) {
            mStatus = STATUS_LAST_PAGE;
            if (null != mOnLoadMoreStatusLayoutCallback) {
                mOnLoadMoreStatusLayoutCallback.showLastPageLayout();
            }
        } else {
            mNextPageNo++;
            mStatus = STATUS_IDLE;
        }
    }

    public void resetNextPageNo() {
        this.mNextPageNo = mStartPageNo;
    }

    public int getNextPageNo() {
        return this.mNextPageNo;
    }

    public void setLoadMoreError() {
        mStatus = STATUS_ERROR;
        if (null != mOnLoadMoreStatusLayoutCallback) {
            mOnLoadMoreStatusLayoutCallback.showLoadErrorLayout();
        }
    }

    public void setOnLoadMoreStatusLayoutCallback(OnLoadMoreStatusLayoutCallback callback) {
        this.mOnLoadMoreStatusLayoutCallback = callback;
    }

    private void addOnScrollListener() {
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                mScrollState = newState;
            }

            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                if (STATUS_IDLE != mStatus) return;

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int itemCount = null == layoutManager ? 0 : layoutManager.getItemCount();
                if (itemCount <= 0) {
                    return;
                }

                if (isReverseMode) {//从下往上加载更多.
                    int firstVisiblePosition = findLastVisiblePosition(layoutManager);

                    if ((itemCount - firstVisiblePosition <= mThresholdValue) && (mScrollState == 1 || mScrollState == 2)) {
                        dispatchLoadMore();
                    }
                } else {//从上往下加载更多.
                    int lastVisiblePosition = findLastVisiblePosition(layoutManager);

                    if ((itemCount == lastVisiblePosition + mThresholdValue) && (mScrollState == 1 || mScrollState == 2)) {
                        dispatchLoadMore();
                    }
                }
            }
        });
    }

    /**
     * 获取最后一个完全可见的Item Position.
     */
    private int findLastVisiblePosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

            return linearLayoutManager.findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;

            int[] lastVisiblePositionArray = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
            return lastVisiblePositionArray[lastVisiblePositionArray.length - 1];
        }
        return 0;
    }

    /**
     * 获取第一个完全可见的Item Position.
     */
    private int findFirstVisiblePosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

            return linearLayoutManager.findFirstVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;

            int[] lastVisiblePositionArray = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(null);
            return lastVisiblePositionArray[0];
        }
        return 0;
    }

    public void requestLoadMore(int status) {
        if (STATUS_ERROR == status || STATUS_WAIT_LOAD == status) {
            if (STATUS_ERROR == mStatus || STATUS_WAIT_LOAD == mStatus) {
                startLoadMore();
            }
        }
    }


    private void dispatchLoadMore() {
        if (isAutoLoadMore) {//自动加载更多.
            startLoadMore();
        } else {//需要用户手动触发加载更多.
            mStatus = STATUS_WAIT_LOAD;
            if (null != mOnLoadMoreStatusLayoutCallback) {
                mOnLoadMoreStatusLayoutCallback.showWaitLoadLayout();
            }
        }
    }

    private void startLoadMore() {
        mStatus = STATUS_LOADING;
        if (null != mOnLoadMoreListener) {
            if (null != mOnLoadMoreStatusLayoutCallback) {
                mOnLoadMoreStatusLayoutCallback.showLoadingLayout();
            }
            mOnLoadMoreListener.onLoadMore(mNextPageNo);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int nextPageNo);
    }

    public interface OnLoadMoreStatusLayoutCallback {

        //显示加载更多中视图.
        void showLoadingLayout();

        //显示等待加载更多视图.
        void showWaitLoadLayout();

        //显示加载更多数据失败视图.
        void showLoadErrorLayout();

        //显示已经是最后一页视图.
        void showLastPageLayout();
    }

}