package com.duanlu.baseui.fragment;

import com.duanlu.baseui.R;

/********************************
 * @name BaseFragment
 * @author 段露
 * @createDate 2019/02/13 9:25.
 * @updateDate 2019/02/13 9:25.
 * @version V1.0.0
 * @describe Fragment基类, 项目中所有Fragment必须直接或间接继承自该类.
 ********************************/
public abstract class BaseFragment extends BaseLazyFragment {

    @Override
    public int getRootLayoutResId() {
        return R.layout.baseui_base_activity_and_fragment;
    }

}
