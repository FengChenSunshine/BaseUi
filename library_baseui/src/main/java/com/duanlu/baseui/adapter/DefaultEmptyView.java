package com.duanlu.baseui.adapter;

import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duanlu.baseui.R;

/********************************
 * @name DefaultEmptyView
 * @author 段露
 * @createDate 2019/3/6  17:19.
 * @updateDate 2019/3/6  17:19.
 * @version V1.0.0
 * @describe 默认空布局.
 ********************************/
public class DefaultEmptyView extends FrameLayout {
    private FrameLayout mFlEmptyLayout;
    private LinearLayout mLlEmptyContent;
    private ImageView mIvEmptyIcon;
    private TextView mTvEmptyHint;
    private int mMaxHeight;

    public DefaultEmptyView(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public DefaultEmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public DefaultEmptyView(@NonNull Context context, @DrawableRes int iconResId, @StringRes int hintTextResId) {
        this(context, (AttributeSet) null);
        this.setIcon(iconResId);
        this.setHint(hintTextResId);
    }

    private void init() {
        inflate(this.getContext(), R.layout.baseui_default_empty_layout, this);
        this.mIvEmptyIcon = (ImageView) this.findViewById(R.id.iv_empty_icon);
        this.mTvEmptyHint = (TextView) this.findViewById(R.id.tv_empty_hint);
        this.mLlEmptyContent = (LinearLayout) this.findViewById(R.id.ll_empty_content);
        this.mFlEmptyLayout = (FrameLayout) this.findViewById(R.id.fl_empty_layout);
    }

    public ImageView setIcon(@DrawableRes int iconResId) {
        this.mIvEmptyIcon.setImageResource(iconResId);
        this.mIvEmptyIcon.setVisibility(View.VISIBLE);
        return this.mIvEmptyIcon;
    }

    public TextView setHint(String hintText) {
        this.mTvEmptyHint.setText(hintText);
        return this.mTvEmptyHint;
    }

    public TextView setHint(@StringRes int hintTextResId) {
        this.mTvEmptyHint.setText(hintTextResId);
        return this.mTvEmptyHint;
    }

    public TextView setHintTextColor(@ColorRes int hintTextColorResId) {
        this.mTvEmptyHint.setTextColor(this.getContext().getResources().getColorStateList(hintTextColorResId));
        return this.mTvEmptyHint;
    }

    public void setEmptyLayoutBackground(@DrawableRes int backgroundColor) {
        this.mFlEmptyLayout.setBackgroundResource(backgroundColor);
    }

    public LinearLayout setGravity(int gravity) {
        LayoutParams params = (LayoutParams) this.mLlEmptyContent.getLayoutParams();
        params.gravity = gravity;
        return this.mLlEmptyContent;
    }

    public void setMaxHeight(int maxHeight) {
        if (-2 == maxHeight) {
            android.view.ViewGroup.LayoutParams params = this.getLayoutParams();
            if (null != params) {
                params.height = -2;
            }
        } else {
            this.mMaxHeight = maxHeight;
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mMaxHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (heightMode == MeasureSpec.EXACTLY) {
                heightSize = heightSize <= this.mMaxHeight ? heightSize : this.mMaxHeight;
            }

            if (heightMode == 0) {
                heightSize = heightSize <= this.mMaxHeight ? heightSize : this.mMaxHeight;
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                heightSize = heightSize <= this.mMaxHeight ? heightSize : this.mMaxHeight;
            }

            int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
            super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
        }
    }
}