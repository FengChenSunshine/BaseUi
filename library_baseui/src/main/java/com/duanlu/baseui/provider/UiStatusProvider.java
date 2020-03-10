package com.duanlu.baseui.provider;

import androidx.annotation.NonNull;

import com.fengchen.uistatus.UiStatusController;

/********************************
 * @name UiStatusProvider
 * @author 段露
 * @createDate 2019/3/6  13:45.
 * @updateDate 2019/3/6  13:45.
 * @version V1.0.0
 * @describe 视图控制器相关.
 ********************************/
public interface UiStatusProvider {

    /**
     * 获取试图控制器.
     *
     * @return 试图控制器.
     */
    @NonNull
    UiStatusController getUiStatusController();

    /**
     * 获取需要试图控制器接管的View or Activity.
     *
     * @return 试图控制器接管的View or Activity.
     */
    Object getUiStatusControllerTarget();

}
