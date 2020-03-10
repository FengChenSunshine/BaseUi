package com.duanlu.baseui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

/********************************
 * @name RvBaseAdapter
 * @author 段露
 * @createDate 2019/3/6  16:51.
 * @updateDate 2019/3/6  16:51.
 * @version V1.0.0
 * @describe 列表适配器基类.
 ********************************/
public abstract class RvBaseAdapter<T> extends RecyclerView.Adapter<RvBaseViewHolder> {

    protected final String TAG = getClass().getSimpleName();

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_EMPTY = 200000;
    private static final int BASE_ITEM_TYPE_FOOTER = 300000;
    private CustomAdapterDataObserver mCustomAdapterDataObserver;
    protected LayoutInflater mInflater;
    protected List<T> mData;
    protected View mView;
    protected Context mContext;
    private int mLastPosition;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mEmptyViews = new SparseArrayCompat<>();
    protected ListenerWithPosition.OnClickWithPositionListener mOnItemClickListener;
    protected ListenerWithPosition.OnLongClickWithPositionListener mOnLongClickWithPositionListener;
    protected ListenerWithPosition.OnItemClickInterceptor mOnItemClickInterceptor;

    public RvBaseAdapter(@NonNull Context context) {
        this.init(context, null, null);
    }

    public RvBaseAdapter(@NonNull Context context, List<T> data) {
        this.init(context, data, null);
    }

    public RvBaseAdapter(@NonNull Context context, List<T> data, ListenerWithPosition.OnItemClickInterceptor onItemClickInterceptor) {
        this.init(context, data, onItemClickInterceptor);
    }

    public RvBaseAdapter(@NonNull Context context, ListenerWithPosition.OnItemClickInterceptor onItemClickInterceptor) {
        this.init(context, null, onItemClickInterceptor);
    }

    private void init(@NonNull Context context, List<T> data, ListenerWithPosition.OnItemClickInterceptor onItemClickInterceptor) {
        this.mContext = context;
        this.setData(data);
        this.mInflater = LayoutInflater.from(context);
        this.mCustomAdapterDataObserver = new CustomAdapterDataObserver();
        this.registerAdapterDataObserver(this.mCustomAdapterDataObserver);
        this.setEmptyView(new DefaultEmptyView(this.mContext));
        this.mOnItemClickInterceptor = onItemClickInterceptor;
    }

    @NonNull
    public RvBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null != this.mHeaderViews.get(viewType)) {
            return new RvBaseViewHolder(this.mHeaderViews.get(viewType), this.mOnItemClickInterceptor);
        } else {
            View footView;
            if (null != this.mEmptyViews.get(viewType)) {
                footView = this.mEmptyViews.get(viewType);
                footView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
                return new RvBaseViewHolder(footView, this.mOnItemClickInterceptor);
            } else if (null != this.mFootViews.get(viewType)) {
                footView = this.mFootViews.get(viewType);
                footView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
                return new RvBaseViewHolder(footView, this.mOnItemClickInterceptor);
            } else {
                return onCreateContentViewHolder(parent, viewType);
            }
        }
    }

    /**
     * 创建内容ViewHolder.
     */
    protected RvBaseViewHolder onCreateContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mView = this.mInflater.inflate(this.getLayoutResId(viewType), parent, false);
        return new RvBaseViewHolder(this.mView, this.mOnItemClickInterceptor);
    }

    public void onBindViewHolder(@NonNull RvBaseViewHolder holder, int position) {
        if (!this.isHeaderView(position) && !this.isFooterView(position) && !this.isEmptyView(position)) {
            holder.position = holder.getLayoutPosition() - this.getHeaderItemCount() - this.getEmptyItemCount();
            ListenerWithPosition listener;
            if (null != this.mOnItemClickListener) {
                listener = new ListenerWithPosition(holder.position, holder, this.mOnItemClickInterceptor);
                listener.setOnClickListener(this.mOnItemClickListener);
                holder.itemView.setOnClickListener(listener);
            }

            if (null != this.mOnLongClickWithPositionListener) {
                listener = new ListenerWithPosition(holder.position, holder);
                listener.setOnLongClickListener(this.mOnLongClickWithPositionListener);
                holder.itemView.setOnLongClickListener(listener);
            }

            this.convert(holder, this.mData.get(holder.position));
            this.setItemAnimation(holder, holder.position, this.mLastPosition);
            this.mLastPosition = holder.position;
        }
    }

    protected void setItemAnimation(@NonNull RvBaseViewHolder holder, int position, int lastPosition) {
    }

    public void setOnItemClickListener(ListenerWithPosition.OnClickWithPositionListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(ListenerWithPosition.OnLongClickWithPositionListener onItemLongClickListener) {
        this.mOnLongClickWithPositionListener = onItemLongClickListener;
    }

    public void onBindViewHolder(@NonNull RvBaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!this.isHeaderView(position) && !this.isFooterView(position) && !this.isEmptyView(position)) {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    protected abstract void convert(RvBaseViewHolder holder, T model);

    public int getItemViewType(int position) {
        if (this.isHeaderView(position)) {
            return this.mHeaderViews.keyAt(position);
        } else if (this.isEmptyView(position)) {
            return this.mEmptyViews.keyAt(position - this.getHeaderItemCount());
        } else if (this.isFooterView(position)) {
            return this.mFootViews.keyAt(position - this.getHeaderItemCount() - this.getEmptyItemCount() - this.getContentItemCount());
        } else {
            return getContentItemViewType(position - this.getHeaderItemCount() - this.getEmptyItemCount());
        }
    }

    /**
     * 获取内容item类型.
     *
     * @param contentItemPosition 内容item真实位置索引.
     * @return 内容item类型.
     */
    protected int getContentItemViewType(int contentItemPosition) {
        return super.getItemViewType(contentItemPosition);
    }

    @Override
    public int getItemCount() {
        int count = this.getHeaderItemCount() + this.getContentItemCount() + this.getFooterItemCount() + this.getEmptyItemCount();
        return count;
    }

    public void onViewAttachedToWindow(@NonNull RvBaseViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (!this.isHeaderView(position) && !this.isFooterView(position) && !this.isEmptyView(position)) {
            super.onViewAttachedToWindow(holder);
        } else {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams slp = (StaggeredGridLayoutManager.LayoutParams) lp;
                slp.setFullSpan(true);
            }
        }
    }

    public void onViewRecycled(@NonNull RvBaseViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (!this.isHeaderView(position) && !this.isFooterView(position) && !this.isEmptyView(position)) {
            super.onViewRecycled(holder);
        }
    }

    public boolean onFailedToRecycleView(@NonNull RvBaseViewHolder holder) {
        int position = holder.getAdapterPosition();
        return !this.isHeaderView(position) && !this.isFooterView(position) && !this.isEmptyView(position) ? super.onFailedToRecycleView(holder) : false;
    }

    public void onViewDetachedFromWindow(@NonNull RvBaseViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (!this.isHeaderView(position) && !this.isFooterView(position) && !this.isEmptyView(position)) {
            super.onViewDetachedFromWindow(holder);
        }

    }

    public boolean isHeaderView(int position) {
        return 0 < this.getHeaderItemCount() && position >= 0 && position < this.getHeaderItemCount();
    }

    public boolean isFooterView(int position) {
        return 0 < this.getFooterItemCount() && position >= this.getHeaderItemCount() + this.getContentItemCount() + this.getEmptyItemCount();
    }

    public boolean isEmptyView(int position) {
        return 0 < this.getEmptyItemCount() && position >= this.getHeaderItemCount() && position < this.getHeaderItemCount() + this.getEmptyItemCount();
    }

    public void setData(List<T> mData) {
        this.mData = mData;
    }

    public List<T> getData() {
        return this.mData;
    }

    public void setEmptyView(View view) {
        this.mEmptyViews.clear();
        if (null != view) {
            this.mEmptyViews.put(BASE_ITEM_TYPE_EMPTY, view);
        } else {
            this.mEmptyViews.remove(BASE_ITEM_TYPE_EMPTY);
        }

    }

    public View getEmptyView() {
        return (View) this.mEmptyViews.get(BASE_ITEM_TYPE_EMPTY);
    }

    public void addHeaderView(View view) {
        if (null != view) {
            this.mHeaderViews.put(BASE_ITEM_TYPE_HEADER + this.getHeaderItemCount(), view);
        }

    }

    public void addHeaderViewAndNotify(View view) {
        this.mHeaderViews.put(BASE_ITEM_TYPE_HEADER + this.getHeaderItemCount(), view);
        this.notifyItemInserted(this.getHeaderItemCount() - 1);
    }

    public void removeHeaderViewAndNotify(View view) {
        int headerIndex = this.mHeaderViews.indexOfValue(view);
        this.mHeaderViews.removeAt(headerIndex);
        this.notifyItemRemoved(headerIndex);
    }

    public void addFooterView(View view) {
        if (null != view) {
            this.mFootViews.put(BASE_ITEM_TYPE_FOOTER + this.getFooterItemCount(), view);
        }

    }

    public void addFooterViewAndNotify(View view) {
        this.mFootViews.put(BASE_ITEM_TYPE_FOOTER + this.getFooterItemCount(), view);
        this.notifyItemInserted(this.getHeaderItemCount() + this.getContentItemCount() + this.getEmptyItemCount() + this.getFooterItemCount() - 1);
    }

    public void removeFooterViewAndNotify(View view) {
        int footerIndex = this.mFootViews.indexOfValue(view);
        this.mFootViews.removeAt(footerIndex);
        int tempPosition = this.getHeaderItemCount() + this.getContentItemCount() + this.getEmptyItemCount() + footerIndex;
        if (tempPosition < this.getItemCount()) {
            this.notifyItemRemoved(tempPosition);
        }
    }

    public void setLoadMoreView(View view) {
        if (null != view) {
            this.mFootViews.put(BASE_ITEM_TYPE_FOOTER + this.getFooterItemCount(), view);
        }
    }

    public int getHeaderItemCount() {
        return this.mHeaderViews.size();
    }

    public int getFooterItemCount() {
        return this.mFootViews.size();
    }

    public int getEmptyItemCount() {
        return 0 == this.getContentItemCount() && this.mEmptyViews.size() > 0 ? this.mEmptyViews.size() : 0;
    }

    public int getContentItemCount() {
        return null == this.mData ? 0 : this.mData.size();
    }

    @LayoutRes
    public abstract int getLayoutResId(int viewType);

    protected RvBaseViewHolder findViewHolderByPosition(RecyclerView recyclerView, int position) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (null != layoutManager) {
            View child;
            RecyclerView.ViewHolder childViewHolder;
            int childCount = layoutManager.getChildCount();
            for (int i = 0; i < childCount; i++) {
                child = layoutManager.getChildAt(i);
                if (null != child) {
                    childViewHolder = recyclerView.getChildViewHolder(child);
                    if (childViewHolder.getAdapterPosition() == position) {
                        return (RvBaseViewHolder) childViewHolder;
                    }
                }
            }
        }
        return null;
    }

    private class CustomAdapterDataObserver extends RecyclerView.AdapterDataObserver {
        private CustomAdapterDataObserver() {
        }

        public void onChanged() {
            super.onChanged();
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            positionStart += RvBaseAdapter.this.getHeaderItemCount() + RvBaseAdapter.this.getEmptyItemCount();
            super.onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            positionStart += RvBaseAdapter.this.getHeaderItemCount() + RvBaseAdapter.this.getEmptyItemCount();
            super.onItemRangeChanged(positionStart, itemCount, payload);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            positionStart += RvBaseAdapter.this.getHeaderItemCount() + RvBaseAdapter.this.getEmptyItemCount();
            if (RvBaseAdapter.this.getContentItemCount() == 1) {
                RvBaseAdapter.this.notifyDataSetChanged();
            } else {
                super.onItemRangeInserted(positionStart, itemCount);
            }

        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            positionStart += RvBaseAdapter.this.getHeaderItemCount() + RvBaseAdapter.this.getEmptyItemCount();
            super.onItemRangeRemoved(positionStart, itemCount);
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            fromPosition += RvBaseAdapter.this.getHeaderItemCount() + RvBaseAdapter.this.getEmptyItemCount();
            toPosition += RvBaseAdapter.this.getHeaderItemCount() + RvBaseAdapter.this.getEmptyItemCount();
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }
    }
}
