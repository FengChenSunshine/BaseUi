package com.duanlu.baseui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.duanlu.baseui.R;

import java.lang.reflect.Method;

/********************************
 * @name CommonToolbar
 * @author 段露
 * @createDate 2019/02/12 16:31.
 * @updateDate 2019/02/12 16:31.
 * @version V1.0.0
 * @describe 统一样式标题栏.
 ********************************/
public class CommonToolbar extends Toolbar {

    private TextView mTvCenterTitle;

    private boolean isUseCenterTitle = true;

    public CommonToolbar(Context context) {

        this(context, null, androidx.appcompat.R.attr.toolbarStyle);
    }

    public CommonToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, androidx.appcompat.R.attr.toolbarStyle);
    }

    public CommonToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray;
        int[] attrsArrayIcon = {R.attr.resourceNavigationIcon};
        typedArray = context.obtainStyledAttributes(attrsArrayIcon);
        int navigationIconResId = typedArray.getResourceId(0, 0);
        typedArray.recycle();

        mTvCenterTitle = createCenterTitleView();
        addView(mTvCenterTitle);

        //设置全局默认返回键图标.
        if (navigationIconResId > 0) {
            setNavigationIcon(navigationIconResId);
        }
        //设置导航键点击事件.
        setNavigationOnClickListener(view -> ((Activity) getContext()).finish());
        //设置Menu Popup弹窗图标文字同时显示.
        setOptionalIconsVisible2(true);

        //可以配置公共的默认的Menu.
//        inflateMenu(R.menu.toolbar_menu);
    }

    @Override
    public void setTitle(int resId) {
        this.setTitle(this.getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        if (isUseCenterTitle) {
            if (null == mTvCenterTitle) {
                mTvCenterTitle = createCenterTitleView();
            }

            if (null != mTvCenterTitle) {
                mTvCenterTitle.setText(title);
            }
        } else {
            super.setTitle(title);
        }
    }

    private TextView createCenterTitleView() {
        TextView textView = new AppCompatTextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.Toolbar_Title);
        } else {
            textView.setTextAppearance(getContext(), R.style.Toolbar_Title);
        }

        return textView;
    }

    public void setTitleTextStyle(boolean isBold) {
        if (null != mTvCenterTitle) mTvCenterTitle.getPaint().setFakeBoldText(isBold);
    }

    private void createCustomNavigationView() {
        ImageView navigation = new ImageView(getContext());
//        if (navigationIconResId > 0) {
//            setNavigationIcon(navigationIconResId);
//        } else {
//            setNavigationIcon(R.drawable.base_ui_ic_back);
//        }
        setNavigationIcon(null);
    }

    public void setTitleTextSize(float titleTextSize) {
        if (null != mTvCenterTitle) {
            mTvCenterTitle.setTextSize(titleTextSize);
        }
    }

    @Override
    public void setTitleTextColor(int color) {
        if (mTvCenterTitle != null) {
            mTvCenterTitle.setTextColor(color);
        }
        super.setTitleTextColor(color);
    }

    /**
     * 设置menu菜单.
     */
    @Override
    public void inflateMenu(int resId) {
        super.inflateMenu(resId);
    }

    /**
     * 设置溢出菜单按钮图标.
     */
    @Override
    public void setOverflowIcon(@Nullable Drawable icon) {
        super.setOverflowIcon(icon);
    }

    /**
     * 设置溢出菜单按钮图标.
     */
    public void setOverflowIcon(@DrawableRes int iconResId) {
        super.setOverflowIcon(ContextCompat.getDrawable(getContext(), iconResId));
    }

    /**
     * 设置menu item点击事件.
     */
    @Override
    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        super.setOnMenuItemClickListener(listener);
    }

    /**
     * 设置Menu里某个item显示隐藏状态.
     *
     * @param menuItemId item id.
     * @param visible    true显示,false隐藏.
     */
    public void setMenuItemVisible(@IdRes int menuItemId, boolean visible) {
        MenuItem menuItem = getMenu().findItem(menuItemId);
        if (null != menuItem) menuItem.setVisible(visible);
    }

    /**
     * 设置Menu Popup弹窗图标显示状态.
     */
    @SuppressLint("RestrictedApi")
    public void setOptionalIconsVisible(boolean visible) {
        ((MenuBuilder) getMenu()).setOptionalIconsVisible(visible);
    }

    /**
     * 采用反射的方法设置Menu Popup弹窗图标显示状态.
     */
    public void setOptionalIconsVisible2(boolean visible) {
        Menu menu = getMenu();
        if (null != menu) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, visible);
                } catch (Exception e) {
                    //nothing.
                }
            }
        }
    }

}