package com.duanlu.baseui.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

/********************************
 * @name ListenerWithPosition
 * @author 段露
 * @createDate 2019/3/6  16:57.
 * @updateDate 2019/3/6  16:57.
 * @version V1.0.0
 * @describe RecyclerView视图内使用的一些监听器.
 ********************************/
public class ListenerWithPosition implements CompoundButton.OnCheckedChangeListener, View.OnLongClickListener, View.OnClickListener, TextWatcher {
    private int mPosition;
    private RvBaseViewHolder mHolder;
    private OnClickWithPositionListener mOnClickListener;
    private OnLongClickWithPositionListener mOnLongClickListener;
    private OnCheckedChangeWithPositionListener mCheckChangeListener;
    private OnTextChangWithPosition mOnTextChange;
    private OnItemClickInterceptor mOnItemClickInterceptor;

    public ListenerWithPosition(int position, RvBaseViewHolder holder) {
        this.mPosition = position;
        this.mHolder = holder;
    }

    public ListenerWithPosition(int position, RvBaseViewHolder holder, OnItemClickInterceptor onItemClickInterceptor) {
        this.mPosition = position;
        this.mHolder = holder;
        this.mOnItemClickInterceptor = onItemClickInterceptor;
    }

    public void onClick(View v) {
        if (this.mOnClickListener != null && !this.checkIsInterceptor(v)) {
            this.mOnClickListener.onClick(v, this.mPosition, this.mHolder);
        }
    }

    public void setOnClickListener(OnClickWithPositionListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public boolean onLongClick(View v) {
        return this.mOnLongClickListener != null && !this.checkIsInterceptor(v) ? this.mOnLongClickListener.onLongClick(v, this.mPosition, this.mHolder) : false;
    }

    public void setOnLongClickListener(OnLongClickWithPositionListener onLongClickListener) {
        this.mOnLongClickListener = onLongClickListener;
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (this.mCheckChangeListener != null) {
            this.mCheckChangeListener.onCheckedChanged(buttonView, isChecked, this.mPosition, this.mHolder);
        }
    }

    public void setCheckChangeListener(OnCheckedChangeWithPositionListener mCheckChangeListener) {
        this.mCheckChangeListener = mCheckChangeListener;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (this.mOnTextChange != null) {
            this.mOnTextChange.beforeTextChanged(s, start, count, after, this.mPosition, this.mHolder);
        }
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (this.mOnTextChange != null) {
            this.mOnTextChange.onTextChanged(s, start, before, count, this.mPosition, this.mHolder);
        }
    }

    public void afterTextChanged(Editable s) {
        if (this.mOnTextChange != null) {
            this.mOnTextChange.afterTextChanged(s, this.mPosition, this.mHolder);
        }

    }

    public void addTextChangedListener(OnTextChangWithPosition mOnTextChange) {
        this.mOnTextChange = mOnTextChange;
    }

    private boolean checkIsInterceptor(View v) {
        return null != this.mOnItemClickInterceptor && this.mOnItemClickInterceptor.onItemClickInterceptor(v, this.mPosition, this.mHolder);
    }

    public interface OnItemClickInterceptor {
        boolean onItemClickInterceptor(View var1, int position, RvBaseViewHolder holder);
    }

    public interface OnTextChangWithPosition {
        void beforeTextChanged(CharSequence var1, int var2, int var3, int var4, int var5, RvBaseViewHolder holder);

        void onTextChanged(CharSequence var1, int var2, int var3, int var4, int var5, RvBaseViewHolder holder);

        void afterTextChanged(Editable var1, int var2, RvBaseViewHolder holder);
    }

    public interface OnCheckedChangeWithPositionListener {
        void onCheckedChanged(CompoundButton view, boolean checked, int position, RvBaseViewHolder holder);
    }

    public interface OnLongClickWithPositionListener {
        boolean onLongClick(View view, int position, RvBaseViewHolder holder);
    }

    public interface OnClickWithPositionListener {
        void onClick(View view, int position, RvBaseViewHolder holder);
    }
}

