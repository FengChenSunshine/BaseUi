package com.duanlu.baseui.provider;

import android.os.Bundle;
import android.support.annotation.NonNull;

/********************************
 * @name DataProvider
 * @author 段露
 * @createDate 2019/3/6  13:39.
 * @updateDate 2019/3/6  13:39.
 * @version V1.0.0
 * @describe 数据提供者.
 ********************************/
public interface DataProvider {

    /**
     * 解析传入的参数.
     */
    void parseIntentBundle(@NonNull Bundle bundle);

    /**
     * 获取数据
     */
    void getHttpData();

    /**
     * 设置数据
     */
    void setHttpData();

    /**
     * 显示加载数据Loading
     */
    void showLoadingDialog();

    /**
     * 取消加载数据Loading
     */
    void dismissLoadingDialog();

}
