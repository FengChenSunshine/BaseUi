package com.duanlu.baseui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;

import com.duanlu.baseui.R;
import com.duanlu.utils.LogUtils;

/********************************
 * @name BaseDialog
 * @author 段露
 * @createDate 2019/3/21  10:03.
 * @updateDate 2019/3/21  10:03.
 * @version V1.0.0
 * @describe AppCompatDialog基类.
 ********************************/
public abstract class BaseDialog extends AppCompatDialog {

    private static final String TAG = BaseDialog.class.getSimpleName();

    protected Context mContext;
    private View mContentView;

    private boolean isAnimationIng = false;

    public BaseDialog(Context context) {
        this(context, R.style.Resource_AppCompatDialog_Common);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        initDialog(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fullscreenEnabled()) fullscreen();
    }

    private void initDialog(Context context) {
        this.mContext = context;

        setContentView(getLayoutResId());
        initView();
    }

    @Override
    public void setContentView(int layoutResID) {
        this.mContentView = LayoutInflater.from(mContext).inflate(layoutResID, null);
        super.setContentView(mContentView);
    }

    @Override
    public void setContentView(@NonNull View contentView) {
        this.mContentView = contentView;
        super.setContentView(mContentView);
    }

    @Override
    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
        this.mContentView = view;
        super.setContentView(mContentView, params);
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract void initView();

    public View getContentView() {
        return this.mContentView;
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
            Animation animation;
            if (null != mContentView && null != (animation = makeShowAnimation())) {
                mContentView.startAnimation(animation);
            }
        }
    }

    @Override
    public void dismiss() {
        if (isAnimationIng) return;

        Animation animation;
        if (null != mContentView && null != (animation = makeDismissAnimation())) {
            wrapperDismissAnimation(animation);
            mContentView.startAnimation(animation);
        } else {
            super.dismiss();
        }
    }

    /**
     * 宽度充满屏幕.
     */
    private void fullscreen() {
        //noinspection ConstantConditions
        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        //默认在底部，宽度撑满
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = getGravity();

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        params.width = screenWidth < screenHeight ? screenWidth : screenHeight;
        getWindow().setAttributes(params);
    }

    protected boolean fullscreenEnabled() {
        return true;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

    protected Animation makeShowAnimation() {
        return null;
    }

    protected Animation makeDismissAnimation() {
        return null;
    }

    private void wrapperDismissAnimation(@NonNull Animation animation) {
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimationIng = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationIng = false;
                mContentView.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BaseDialog.super.dismiss();
                        } catch (Exception e) {
                            LogUtils.e(TAG, "关闭对话框异常：" + Log.getStackTraceString(e));
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}