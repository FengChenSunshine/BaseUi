package com.duanlu.baseui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.duanlu.baseui.BaseConstants;
import com.duanlu.baseui.R;
import com.duanlu.baseui.dialog.HintDialog;
import com.duanlu.baseui.provider.DataProvider;
import com.duanlu.baseui.provider.LayoutProvider;
import com.duanlu.baseui.provider.ToastProvider;
import com.duanlu.baseui.provider.UiStatusProvider;
import com.duanlu.utils.ActivityHelp;
import com.duanlu.utils.ToastUtils;
import com.fengchen.uistatus.UiStatusController;
import com.fengchen.uistatus.UiStatusNetworkStatusProvider;
import com.fengchen.uistatus.annotation.UiStatus;
import com.fengchen.uistatus.controller.IUiStatusController;
import com.fengchen.uistatus.listener.OnCompatRetryListener;

/********************************
 * @name RootActivity
 * @author 段露
 * @createDate 2019/01/23 20:05.
 * @updateDate 2019/01/23 20:05.
 * @version V1.0.0
 * @describe RootActivity.
 ********************************/
abstract class RootActivity extends AppCompatActivity
        implements LayoutProvider, DataProvider, UiStatusProvider, OnCompatRetryListener, ToastProvider {

    protected String TAG = getClass().getSimpleName();

    protected Context mContext;

    protected UiStatusController mUiStatusController;

    private HintDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        //解析Bundle参数.
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            int orientation = bundle.getInt(BaseConstants.EXTRA_ORIENTATION, Integer.MAX_VALUE);
            if (Integer.MAX_VALUE != orientation) {
                //设置屏幕方向.
                setRequestedOrientation(orientation);
            }
            this.parseIntentBundle(bundle);
        }
        //设置视图.
        this.setContentView(getLayoutResId());
        //初始化试图控制器.
        getUiStatusController();
        //子类初始化视图.
        initView(savedInstanceState);
        //绑定试图控制器.
        mUiStatusController.bind(getUiStatusControllerTarget());
        //加载数据.
        getHttpData();
    }

    @Override
    public void setContentView(int layoutResID) {
        if (useDefaultToolbar()) {
            super.setContentView(getRootLayoutResId());
            getLayoutInflater().inflate(layoutResID, findViewById(R.id.fl_container));
            //设置标题.
            Toolbar toolbar = findViewById(R.id.toolbar);
            if (null != toolbar) {
                Bundle bundle = getIntent().getExtras();
                if (null != bundle) {
                    String title = bundle.getString(BaseConstants.EXTRA_TITLE);
                    toolbar.setTitle(null != title ? title : mContext.getText(getTitleResId()));
                }
            }
        } else {
            super.setContentView(layoutResID);
        }
    }

    /**
     * 谷歌在安卓8.0版本时为了支持全面屏，增加了一个限制：
     * 如果是透明的Activity，则不能固定它的方向，因为它的方向其实是依赖其父Activity的(因为透明).
     * 然而这个bug只有在8.0中有，8.1中已经修复.
     * 具体crash有两种:
     * 1.Activity的风格为透明，在manifest文件中指定了一个方向，则在onCreate中crash.
     * 2.Activity的风格为透明，如果调用setRequestedOrientation方法固定方向，则crash.
     * 参考：https://blog.csdn.net/starry_eve/article/details/82777160
     */
    @Override
    public void setRequestedOrientation(int orientation) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O
                && !ActivityHelp.isTranslucentOrFloating(this)) {
            super.setRequestedOrientation(orientation);
        }
    }

    @Override
    public void onBackPressed() {
        if (!isFinishing()) super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        dismissLoadingDialog();
        mContext = null;
        super.onDestroy();
    }

    @NonNull
    @Override
    public UiStatusController getUiStatusController() {
        if (null == mUiStatusController) {
            mUiStatusController = UiStatusController.get();
            mUiStatusController.setOnCompatRetryListener(this);
        }
        return mUiStatusController;
    }

    @Override
    public boolean useDefaultToolbar() {
        return false;
    }

    @Override
    public void showLoadingDialog() {
        if (null == mLoadingDialog) {
            mLoadingDialog = new HintDialog.Builder(mContext)
                    .setIconType(HintDialog.Builder.ICON_TYPE_LOADING)
                    .setMessage("加载中...")
                    .create();
        }
        if (!mLoadingDialog.isShowing()) mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    /**
     * 重试.
     *
     * @param uiStatus   UiStatus.
     * @param target     bind Object.
     * @param controller 当前视图状态控制器.
     * @param trigger    重试触发控件.
     */
    @Override
    public void onUiStatusRetry(@UiStatus int uiStatus, @NonNull Object target, @NonNull IUiStatusController controller, @NonNull View trigger) {
        switch (uiStatus) {
            case UiStatus.EMPTY:
            case UiStatus.LOAD_ERROR:
                controller.changeUiStatus(UiStatus.LOADING);
                getHttpData();
                break;
            case UiStatus.NETWORK_ERROR:
                if (UiStatusNetworkStatusProvider.getInstance().isConnection(mContext)) {
                    controller.changeUiStatus(UiStatus.LOADING);
                    getHttpData();
                }
                break;
        }
    }

    @Override
    public void toast(CharSequence message) {
        Thread.currentThread();
        ToastUtils.showToastOnce(mContext, message.toString());
    }

    @Override
    public void toast(CharSequence message, @IntRange(from = 0, to = 1) int duration) {
        ToastUtils.showToastOnce(mContext, message.toString());
    }

}