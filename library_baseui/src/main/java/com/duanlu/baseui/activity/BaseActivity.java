package com.duanlu.baseui.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.view.View;

import com.duanlu.baseui.R;

/********************************
 * @name BaseActivity
 * @author 段露
 * @createDate 2019/01/23 20:05.
 * @updateDate 2019/01/23 20:05.
 * @version V1.0.0
 * @describe 带默认Toolbar的Activity基类.
 ********************************/
public abstract class BaseActivity extends RootActivity {

    @CallSuper
    @Override
    public void parseIntentBundle(@NonNull Bundle bundle) {

    }

    @Override
    public int getRootLayoutResId() {
        return R.layout.baseui_base_activity_and_fragment;
    }

    protected void setToolbarDividerVisibility(int visibility) {
        View divider = findViewById(R.id.view_toolbar_divider);
        if (null != divider) divider.setVisibility(visibility);
    }

    @Override
    public Object getUiStatusControllerTarget() {
        return useDefaultToolbar() ? findViewById(R.id.fl_container) : this;
    }

    @Override
    public boolean useDefaultToolbar() {
        return true;
    }

}