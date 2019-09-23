package com.duanlu.baseui.provider;

import android.support.annotation.IntRange;

/********************************
 * @name ToastProvider
 * @author 段露
 * @createDate 2019/3/6  13:44.
 * @updateDate 2019/3/6  13:44.
 * @version V1.0.0
 * @describe Toast提供者.
 ********************************/
public interface ToastProvider {

    void toast(CharSequence message);

    void toast(CharSequence message, @IntRange(from = 0, to = 1) int duration);

}
