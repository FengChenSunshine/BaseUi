package com.duanlu.baseui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.duanlu.utils.CheckUtils;

import java.util.List;

/********************************
 * @name BaseLazyFragment
 * @author 段露
 * @createDate 2019/02/12 15:52.
 * @updateDate 2019/02/12 15:52.
 * @version V1.0.0
 * @describe 懒加载Fragment基类.
 ********************************/
abstract class BaseLazyFragment extends RootFragment {

    private boolean isViewCreate = false;//视图是否创建(这里调用过onActivityCreated分发后置为true)
    private boolean isCurrentVisibleState = false;//当前是否是显示状态
    private boolean isFirstVisible = true;//是否第一次对用户可见：true是,false不是.

    /**
     * 对用户第一次可见
     */
    protected void onFragmentFirstVisible() {
        getHttpData();
    }

    /**
     * 对用户可见
     */
    protected void onFragmentResume() {
        //Empty.
    }

    /**
     * 对用户不可见
     */
    protected void onFragmentPause() {
        //Empty.
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isViewCreate = true;
        if (!isHidden() && getUserVisibleHint()) {
            this.dispatchUserVisibleHint(true);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.dispatchUserVisibleHint(!hidden);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirstVisible) {
            if (!isHidden() && !isCurrentVisibleState && getUserVisibleHint()) {
                this.dispatchUserVisibleHint(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isCurrentVisibleState && getUserVisibleHint()) {
            this.dispatchUserVisibleHint(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreate = false;
        isFirstVisible = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isViewCreate) return;
        if (isVisibleToUser && !isCurrentVisibleState) {
            this.dispatchUserVisibleHint(true);
        } else if (!isVisibleToUser && isCurrentVisibleState) {
            this.dispatchUserVisibleHint(false);
        }
    }

    /**
     * 当前Fragment状态
     *
     * @return true可见, false不可见.
     */
    protected boolean isCurrentVisibleState() {
        return isCurrentVisibleState;
    }

    /**
     * 判断一个Fragment是否可见.
     *
     * @param fragment 需要判断的Fragment.
     * @return true可见, false不可见.
     */
    private boolean isFragmentVisible(Fragment fragment) {
        return !fragment.isHidden() && fragment.getUserVisibleHint();
    }

    /**
     * 判断父Fragment是否可见
     *
     * @return true可见, false不可见.
     */
    private boolean isParentVisible() {
        Fragment fragment = getParentFragment();
        return (!(fragment instanceof BaseLazyFragment)) || ((BaseLazyFragment) fragment).isCurrentVisibleState();
    }

    /**
     * 分发可见状态.
     *
     * @param isVisible true可见,false不可见.
     */
    private void dispatchUserVisibleHint(boolean isVisible) {
        if (isVisible && !isParentVisible()) return;
        if (isCurrentVisibleState == isVisible) return;
        isCurrentVisibleState = isVisible;
        if (isVisible) {
            if (isFirstVisible) {
                isFirstVisible = false;
                this.onFragmentFirstVisible();
            }
            this.onFragmentResume();
            this.dispatchChildVisibleState(true);
        } else {
            this.dispatchChildVisibleState(false);
            this.onFragmentPause();
        }
    }

    /**
     * 分发子Fragment可见状态
     *
     * @param isVisible true可见,false不可见.
     */
    private void dispatchChildVisibleState(boolean isVisible) {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> childFragmentList = fragmentManager.getFragments();
        if (CheckUtils.isNotEmpty(childFragmentList)) return;
        for (Fragment child : childFragmentList) {
            if (child instanceof BaseLazyFragment && !child.isHidden() && child.getUserVisibleHint()) {
                ((BaseLazyFragment) child).dispatchUserVisibleHint(isVisible);
            }
        }
    }

}