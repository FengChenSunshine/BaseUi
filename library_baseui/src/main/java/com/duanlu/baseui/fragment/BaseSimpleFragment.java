package com.duanlu.baseui.fragment;

import android.support.annotation.CallSuper;

import com.fengchen.uistatus.annotation.UiStatus;

/********************************
 * @name BaseSimpleFragment
 * @author 段露
 * @createDate 2019/02/13 10:23.
 * @updateDate 22019/02/13 10:23.
 * @version V1.0.0
 * @describe 一个简单的不需要网络请求的界面，可以直接显示内容视图.
 ********************************/
public abstract class BaseSimpleFragment extends BaseFragment {

    @CallSuper
    @Override
    public void getHttpData() {
        setHttpData();
    }

    @CallSuper
    @Override
    public void setHttpData() {
        mUiStatusController.changeUiStatus(UiStatus.CONTENT);
    }

}
