package com.duanlu.baseui.provider;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/********************************
 * @name LayoutProvider
 * @author 段露
 * @createDate 2019/3/6  13:40.
 * @updateDate 2019/3/6  13:40.
 * @version V1.0.0
 * @describe 视图提供者.
 ********************************/
public interface LayoutProvider {

    /**
     * 使用默认标题栏.
     *
     * @return true使用，false不使用.
     */
    boolean useDefaultToolbar();

    /**
     * 获取标题.
     *
     * @return 标题资源id.
     */
    @StringRes
    int getTitleResId();

    /**
     * 获取视图(通常为Toolbar以下视图)文件资源id.
     *
     * @return 视图文件资源id.
     */
    int getLayoutResId();

    /**
     * 获取包含Toolbar的视图id.
     *
     * @return 包含Toolbar的视图id.
     */
    int getRootLayoutResId();

    /**
     * 初始化视图.
     */
    void initView(@Nullable Bundle savedInstanceState);

}
