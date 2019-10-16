package com.duanlu.baseui.navigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/********************************
 * @name BottomNavigationLayout
 * @author 段露
 * @createDate 2019/3/14  17:21.
 * @updateDate 2019/3/14  17:21.
 * @version V1.0.0
 * @describe APP底部导航菜单栏.
 ********************************/
public class BottomNavigationLayout extends LinearLayout implements OnMenuChildClickListener {

    private int mMenuTextColorSelected;//Menu选中文字颜色.
    private int mMenuTextColorUnselected;//Menu未选中文字颜色.
    private int mMenuTextSizeSelected;//Menu选中文字大小.
    private int mMenuTextSizeUnselected;//Menu未选中文字大小.

    private ViewPager mVpContainer;
    private ViewPager.SimpleOnPageChangeListener mOnPageChangeListener;
    private int mCurrentPosition;//当前页面位置.

    private int mFeatureMenuPosition = -1;//设置了需要使用android:clipChildren="false"的menu索引，点击时跳转新界面，而不是切换界面.
    private OnFeatureMenuClickListener mOnFeatureMenuClickListener;

    public BottomNavigationLayout(Context context) {
        this(context, null);
    }

    public BottomNavigationLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomNavigationLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public void initView(Context context, @Nullable AttributeSet attrs) {
        parseTypeArray(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View view = getChildAt(0);
        if (null != view) {
            ((BottomNavigationMenu) view).setMenuSelected(true, false);
        }
    }

    public void parseTypeArray(Context context, @Nullable AttributeSet attrs) {

    }

    public void bindViewPager(ViewPager viewPager) {
        this.mVpContainer = viewPager;

        if (null == mOnPageChangeListener) {
            mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    changeChildStatus(position, false);
                }
            };
        }
        mVpContainer.addOnPageChangeListener(mOnPageChangeListener);
    }

    public void setFeatureMenu(int featureMenuPosition, OnFeatureMenuClickListener listener) {
        this.mFeatureMenuPosition = featureMenuPosition;
        this.mOnFeatureMenuClickListener = listener;
    }

    @Override
    public void onMenuChildClick(int position) {
        if (mFeatureMenuPosition == position) {
            if (null != mOnFeatureMenuClickListener) {
                ((BottomNavigationMenu) getChildAt(position)).playAnimation();
                mOnFeatureMenuClickListener.onFeatureMenuClick();
            }
        } else {
            mVpContainer.removeOnPageChangeListener(mOnPageChangeListener);
            changeChildStatus(position, true);
            mVpContainer.addOnPageChangeListener(mOnPageChangeListener);
        }
    }

    /**
     * @param position       位置.
     * @param isClickTrigger 是否是点击触发，true是，false不是.
     */
    private void changeChildStatus(int position, boolean isClickTrigger) {
        if (position != mCurrentPosition) {

            if (isClickTrigger) {
                if (mFeatureMenuPosition > -1 && position > mFeatureMenuPosition) {
                    mVpContainer.setCurrentItem(position - 1);
                } else {
                    mVpContainer.setCurrentItem(position);
                }
            }

            ((BottomNavigationMenu) getChildAt(mCurrentPosition)).setMenuSelected(false, false);
            mCurrentPosition = position;
        }
        ((BottomNavigationMenu) getChildAt(mCurrentPosition)).setMenuSelected(true, true);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!(child instanceof BottomNavigationMenu)) {
            throw new RuntimeException("BottomNavigationLayout child must is BottomNavigationMenu");
        }
        super.addView(child, index, params);
        int count = getChildCount();
        BottomNavigationMenu menu;
        for (int i = 0; i < count; i++) {
            menu = (BottomNavigationMenu) getChildAt(i);
            menu.setTag(i);
            menu.setOnMenuChildClickListener(this);
            //因为默认是选中第一项的，如果添加的位置正好是第一项时，需要重新设置选中项.
            if (0 == index) {
                menu.setMenuSelected(0 == i, false);
            }
        }
    }

    public int getPositionByName(String name) {
        if (null != name && name.length() > 0) {
            int count = getChildCount();
            BottomNavigationMenu menu;
            for (int i = 0; i < count; i++) {
                menu = (BottomNavigationMenu) getChildAt(i);
                if (name.equals(menu.getName())) {
                    return i;
                }
            }
        }
        return -1;
    }

}