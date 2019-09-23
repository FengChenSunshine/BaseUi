package com.duanlu.baseui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;

/********************************
 * @name OptionsAdapter
 * @author 段露
 * @createDate 2019/3/12  15:32.
 * @updateDate 2019/3/12  15:32.
 * @version V1.0.0
 * @describe 选项列表适配器基类.
 ********************************/
public abstract class OptionsAdapter<T> extends RvBaseAdapter<T> implements ListenerWithPosition.OnClickWithPositionListener {

    private RecyclerView mRecyclerView;
    private boolean isSingle;
    private int mSelectedPosition;
    private SparseBooleanArray mMultiplleSelectedOption;

    private OnOptionStatusChangedListener mListener;

    public OptionsAdapter(@NonNull Context context, boolean isSingle) {
        super(context);
        setOnItemClickListener(this);
        this.isSingle = isSingle;
    }

    public void setOnOptionStatusChangedListener(OnOptionStatusChangedListener listener) {
        this.mListener = listener;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.mSelectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void convert(RvBaseViewHolder holder, T model) {
        convertCommon(holder, model);
        if (isSelected(holder.position)) {
            convertSelected(holder, model);
        } else {
            convertUnselected(holder, model);
        }
    }

    protected abstract void convertCommon(RvBaseViewHolder holder, T model);

    protected abstract void convertSelected(RvBaseViewHolder holder, T model);

    protected abstract void convertUnselected(RvBaseViewHolder holder, T model);

    public boolean isSelected(int position) {
        if (isSingle) {
            return mSelectedPosition == position;
        } else if (null != mMultiplleSelectedOption) {
            return mMultiplleSelectedOption.get(position);
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View view, int position, RvBaseViewHolder holder) {
        if (isSingle) {
            if (isSelected(position)) {//点击的是已经选中的条目.

            } else {
                //重置old Item.
                RvBaseViewHolder viewHolder = findViewHolderByPosition(mRecyclerView, mSelectedPosition);
                if (null != viewHolder) {
                    convertUnselected(viewHolder, mData.get(position));
                }

                int old = mSelectedPosition;
                mSelectedPosition = position;

                //刷新new Item.
                convertSelected(holder, mData.get(position));

                if (null != mListener) {
                    mListener.onOptionStatusChanged(old, mSelectedPosition, true);
                }
            }
        }
    }

    public interface OnOptionStatusChangedListener {
        void onOptionStatusChanged(int oldPosition, int newPosition, boolean isSelected);
    }

}