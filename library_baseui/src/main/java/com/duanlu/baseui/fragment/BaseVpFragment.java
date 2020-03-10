package com.duanlu.baseui.fragment;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.duanlu.baseui.R;
import com.duanlu.baseui.adapter.CommonVpAdapter;
import com.fengchen.uistatus.annotation.UiStatus;
import com.google.android.material.tabs.TabLayout;

/********************************
 * @name BaseVpFragment
 * @author 段露
 * @createDate 2019/02/13 15:50.
 * @updateDate 2019/02/13 15:50.
 * @version V1.0.0
 * @describe 包含ViewPager and TabLayout的基类Fragment.
 ********************************/
public abstract class BaseVpFragment extends BaseFragment {

    protected LinearLayout mLlContainer;

    @Override
    public boolean useDefaultToolbar() {
        return false;
    }

    @Override
    public int getTitleResId() {
        return 0;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.baseui_viewpager_layout;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mLlContainer = findViewById(R.id.ll_container);

        ViewPager viewPager = findViewById(R.id.vp_container);
        CommonVpAdapter vpAdapter = new CommonVpAdapter(getChildFragmentManager(), getTitles());
        configFragment(vpAdapter);
        viewPager.setAdapter(vpAdapter);
        viewPager.setOffscreenPageLimit(vpAdapter.getCount());

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        configTabLayout(tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    protected abstract void configFragment(CommonVpAdapter vpAdapter);

    public void configTabLayout(TabLayout tabLayout) {
        //nothing.
    }

    public abstract String[] getTitles();

    @Override
    public void getHttpData() {
        setHttpData();
    }

    @Override
    public void setHttpData() {
        mUiStatusController.changeUiStatus(UiStatus.CONTENT);
    }

}
