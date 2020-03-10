package com.duanlu.baseui.sample;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;

import com.duanlu.baseui.BaseConstants;
import com.duanlu.baseui.fragment.BaseFragment;
import com.duanlu.baseui.fragment.ShellActivity;
import com.fengchen.uistatus.annotation.UiStatus;

/********************************
 * @name TestFragment.
 * @author 段露.
 * @createDate 2020/3/10 9:55.
 * @updateDate 2020/3/10 9:55.
 * @version V1.0.0
 * @describe 测试.
 ********************************/
public class TestFragment extends BaseFragment {

    public static void navigation(Context context) {
        Intent intent = new Intent(context, ShellActivity.class);
        intent.putExtra(BaseConstants.EXTRA_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        intent.putExtra(BaseConstants.EXTRA_FRAGMENT_CLASS_NAME, TestFragment.class.getName());
        context.startActivity(intent);
    }

    @Override
    public int getTitleResId() {
        return R.string.app_name;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        Log.i("--", "初始化：" + "---------------------");
    }

    @Override
    public void getHttpData() {
        setHttpData();
    }

    @Override
    public void setHttpData() {
        mUiStatusController.changeUiStatus(UiStatus.CONTENT);
    }

}
