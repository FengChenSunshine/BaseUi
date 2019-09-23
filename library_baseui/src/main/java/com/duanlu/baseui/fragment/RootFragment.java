package com.duanlu.baseui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.duanlu.baseui.BaseConstants;
import com.duanlu.baseui.widget.CommonToolbar;
import com.duanlu.baseui.R;
import com.duanlu.baseui.dialog.HintDialog;
import com.duanlu.baseui.provider.DataProvider;
import com.duanlu.baseui.provider.LayoutProvider;
import com.duanlu.baseui.provider.ToastProvider;
import com.duanlu.baseui.provider.UiStatusProvider;
import com.duanlu.utils.ToastUtils;
import com.fengchen.uistatus.UiStatusController;
import com.fengchen.uistatus.UiStatusNetworkStatusProvider;
import com.fengchen.uistatus.annotation.UiStatus;
import com.fengchen.uistatus.controller.IUiStatusController;
import com.fengchen.uistatus.listener.OnCompatRetryListener;

import java.util.List;

/********************************
 * @name RootFragment
 * @author 段露
 * @createDate 2019/01/23 20:12.
 * @updateDate 2019/01/23 20:12.
 * @version V1.0.0
 * @describe 项目中所有Fragment必须直接(不建议)或者间接继承该类.
 ********************************/
abstract class RootFragment extends Fragment
        implements LayoutProvider, DataProvider, UiStatusProvider, OnCompatRetryListener, ToastProvider {

    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;

    protected UiStatusController mUiStatusController;

    private HintDialog mLoadingDialog;

    protected View mRootView;

    private CharSequence mTitle;

    @Override
    public boolean useDefaultToolbar() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        //解析Bundle参数.
        if (null != getArguments()) this.parseIntentBundle(getArguments());
    }

    @CallSuper
    @Override
    public void parseIntentBundle(@NonNull Bundle bundle) {
        mTitle = bundle.getString(BaseConstants.EXTRA_TITLE);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (null != mRootView) return mRootView;

        if (getLayoutResId() <= 0) {
            TextView textView = new TextView(getActivity());
            mRootView = textView;
            textView.setText("未添加视图");
            textView.setTextSize(30);
            textView.setGravity(Gravity.CENTER);
        } else if (useDefaultToolbar()) {
            mRootView = inflater.inflate(getRootLayoutResId(), container, false);
            ViewGroup containerView = mRootView.findViewById(R.id.fl_container);
            inflater.inflate(getLayoutResId(), containerView);

            //设置标题.
            setTitle(null != mTitle ? mTitle : mContext.getText(getTitleResId()));

            //由于项目内使用的是白色背景Toolbar,所以默认显示分割线.
            setToolbarDividerVisibility(View.VISIBLE);
        } else {
            mRootView = inflater.inflate(getLayoutResId(), container, false);
        }
        //初始化试图控制器.
        getUiStatusController();
        //子类初始化视图.
        initView(savedInstanceState);
        //绑定试图控制器.
        if (useDefaultToolbar()) {
            mUiStatusController.bind(getUiStatusControllerTarget());
            return mRootView;
        } else {//不使用默认Toolbar时必须将整个视图作为参数.
            return mUiStatusController.bindFragment(mRootView);
        }
    }

    protected CommonToolbar getDefaultToolbar() {
        return mRootView.findViewById(R.id.toolbar);
    }

    protected void setTitle(CharSequence title) {
        Toolbar toolbar = getDefaultToolbar();
        if (null != toolbar) {
            toolbar.setTitle(title);
        }
    }

    protected void setToolbarDividerVisibility(int visibility) {
        View divider = findViewById(R.id.view_toolbar_divider);
        if (null != divider) divider.setVisibility(visibility);
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V findViewById(int id) {
        return (V) mRootView.findViewById(id);
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
    public Object getUiStatusControllerTarget() {
        if (useDefaultToolbar()) {
            return mRootView.findViewById(R.id.fl_container);
        } else {//不使用默认Toolbar时必须将整个视图作为参数.
            return mRootView;
        }
    }

    /**
     * 设置键盘模式.
     *
     * @param softInputMode {@link WindowManager.LayoutParams#softInputMode
     *                      WindowManager.LayoutParams.softInputMode}
     */
    protected void setSoftInputMode(int softInputMode) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).getWindow().setSoftInputMode(softInputMode);
        }
    }

    /**
     * 返回键拦截
     *
     * @return true响应返回键, false不响应
     */
    public boolean dispatchOnBackPressed() {
        return false;
    }

    /**
     * finish承载当前Fragment的Activity
     */
    protected void finish() {
        if (mContext instanceof Activity) {
            ActivityCompat.finishAfterTransition((Activity) mContext);
        }
    }

    protected void runOnUiThread(Runnable runnable) {
        Activity activity = getActivity();
        if (null != activity) {
            activity.runOnUiThread(runnable);
        }
    }

    protected void setResultOK() {
        setResult(Activity.RESULT_OK, null);
    }

    protected void setResult(int resultCode) {
        setResult(resultCode, null);
    }

    protected void setResult(int resultCode, Intent data) {
        if (mContext instanceof Activity) {
            ((Activity) mContext).setResult(resultCode, data);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //解决多层嵌套Fragment时内层Fragment无法回调onActivityResult方法问题.
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (null == fragments || fragments.size() <= 0) return;
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
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

    @Override
    public void toast(CharSequence message) {
        ToastUtils.showToastOnce(mContext, message.toString());
    }

    @Override
    public void toast(CharSequence message, @IntRange(from = 0, to = 1) int duration) {
        ToastUtils.showToastOnce(mContext, message.toString());
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

    public void finishAfterTransition() {

    }

    public void onActivityReenter(int resultCode, Intent data) {

    }

}