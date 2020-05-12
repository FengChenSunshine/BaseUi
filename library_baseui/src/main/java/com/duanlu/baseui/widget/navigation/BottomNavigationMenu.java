package com.duanlu.baseui.widget.navigation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duanlu.baseui.R;

/********************************
 * @name BottomNavigationMenu
 * @author 段露
 * @createDate 2019/3/14  17:30.
 * @updateDate 2019/3/14  17:30.
 * @version V1.0.0
 * @describe APP底部导航菜单栏子菜单.
 ********************************/
public class BottomNavigationMenu extends LinearLayout implements View.OnClickListener {

    private ImageView mIvMenuIcon;
    private TextView mTvMenuName;

    private int mMenuSelectedColor;
    private int mMenuUnselectedColor;
    private float mMenuSelectedSize;
    private float mMenuUnselectedSize;
    private int mMenuSelectedIconResId;
    private int mMenuUnselectedIconResId;

    private boolean isSelectedMenu;//是否选中当前Menu.

    private OnMenuChildClickListener mOnMenuChildClickListener;

    public BottomNavigationMenu(Context context) {
        this(context, null);
    }

    public BottomNavigationMenu(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationMenu(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomNavigationMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public void initView(Context context, @Nullable AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.baseui_bottom_navigation_menu, this);
        mIvMenuIcon = findViewById(R.id.iv_menu_icon);
        mTvMenuName = findViewById(R.id.tv_menu_name);

        mMenuSelectedSize = mTvMenuName.getTextSize();
        mMenuUnselectedSize = mTvMenuName.getTextSize();

        parseTypeArray(context, attrs);

        setOnClickListener(this);
    }

    public void parseTypeArray(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationMenu);
        int count = typedArray.getIndexCount();
        int index;
        for (int i = 0; i < count; i++) {
            index = typedArray.getIndex(i);
            if (R.styleable.BottomNavigationMenu_menu_name == index) {
                setName(typedArray.getString(index));
            } else if (R.styleable.BottomNavigationMenu_menu_selected_color == index) {
                mMenuSelectedColor = typedArray.getColor(index, mMenuSelectedColor);
            } else if (R.styleable.BottomNavigationMenu_menu_unselected_color == index) {
                mMenuUnselectedColor = typedArray.getColor(index, mMenuUnselectedColor);
            } else if (R.styleable.BottomNavigationMenu_menu_selected_size == index) {
                mMenuSelectedSize = typedArray.getDimension(index, mMenuSelectedSize);
            } else if (R.styleable.BottomNavigationMenu_menu_unselected_size == index) {
                mMenuUnselectedSize = typedArray.getDimension(index, mMenuUnselectedSize);
            } else if (R.styleable.BottomNavigationMenu_menu_selected_icon == index) {
                mMenuSelectedIconResId = typedArray.getResourceId(index, mMenuSelectedIconResId);
            } else if (R.styleable.BottomNavigationMenu_menu_unselected_icon == index) {
                mMenuUnselectedIconResId = typedArray.getResourceId(index, mMenuUnselectedIconResId);
            }
        }
        typedArray.recycle();

        updateMenu();
    }

    public void setMenuSelected(boolean isSelectedMenu, boolean isAnimation) {
        if (isSelectedMenu) {//选中action.
            if (isSelectedMenu()) {//再次选中.
                //FIXM 再次选中.
            } else {//未选中——>选中.
                updateSelected();
                if (isAnimation) {
                    playAnimation();
                }
            }
        } else if (isSelectedMenu()) {//未选中action.
            updateSelected();
        }
    }

    /**
     * 播放动画.
     */
    protected void playAnimation() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mIvMenuIcon
                , "scaleX", 1.0f, 0.9f, 1.1f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mIvMenuIcon
                , "scaleY", 1.0f, 0.9f, 1.1f, 1.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    public void setOnMenuChildClickListener(OnMenuChildClickListener listener) {
        this.mOnMenuChildClickListener = listener;
    }

    private void updateSelected() {
        this.isSelectedMenu = !isSelectedMenu;
        updateMenu();
    }

    public void setName(CharSequence name) {
        mTvMenuName.setText(name);
    }

    public String getName() {
        return mTvMenuName.getText().toString();
    }

    public void setMenu(@ColorInt int selectedColor, @ColorInt int unselectedColor, @DrawableRes int selectedIconResId, @DrawableRes int unselectedIconResId) {
        setMenuColor(selectedColor, unselectedColor);
        setMenuIcon(selectedIconResId, unselectedIconResId);
    }

    public void setMenuColor(@ColorInt int selectedColor, @ColorInt int unselectedColor) {
        this.mMenuSelectedColor = selectedColor;
        this.mMenuUnselectedColor = unselectedColor;

        updateMenuColor();
    }

    public void setMenuIcon(@DrawableRes int selectedIconResId, @DrawableRes int unselectedIconResId) {
        this.mMenuSelectedIconResId = selectedIconResId;
        this.mMenuUnselectedIconResId = unselectedIconResId;

        updateMenuIcon();
    }

    public void setMenuSize(@DrawableRes int selectedSize, @DrawableRes int unselectedSize) {
        this.mMenuSelectedSize = selectedSize;
        this.mMenuUnselectedSize = unselectedSize;

        updateMenuSize();
    }

    public boolean isSelectedMenu() {
        return isSelectedMenu;
    }

    private void updateMenu() {
        updateMenuIcon();
        updateMenuColor();
        updateMenuSize();
    }

    private void updateMenuColor() {
        mTvMenuName.setTextColor(isSelectedMenu() ? mMenuSelectedColor : mMenuUnselectedColor);
    }

    private void updateMenuIcon() {
        mIvMenuIcon.setImageResource(isSelectedMenu() ? mMenuSelectedIconResId : mMenuUnselectedIconResId);
    }

    private void updateMenuSize() {
        mTvMenuName.setTextSize(TypedValue.COMPLEX_UNIT_PX, isSelectedMenu() ? mMenuSelectedSize : mMenuUnselectedSize);
    }

    @Override
    public void onClick(View v) {
        if (null != mOnMenuChildClickListener) {
            mOnMenuChildClickListener.onMenuChildClick((Integer) v.getTag());
        }
    }

}