package com.duanlu.baseui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

/********************************
 * @name BaseBottomSheetDialog
 * @author 段露
 * @createDate 2019/3/21  12:01.
 * @updateDate 2019/3/21  12:01.
 * @version V1.0.0
 * @describe 一个底部弹起的对话框.
 ********************************/
public abstract class BaseBottomSheetDialog extends BaseDialog {

    private static final String TAG = BaseBottomSheetDialog.class.getSimpleName();

    public BaseBottomSheetDialog(Context context) {
        super(context);
    }

    @Override
    protected Animation makeShowAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 1.0f
                , Animation.RELATIVE_TO_SELF, 0f);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(200);
        animationSet.setFillAfter(true);
        return translateAnimation;
    }

    @Override
    protected Animation makeDismissAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 0f
                , Animation.RELATIVE_TO_SELF, 1.0f);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setDuration(200);
        animationSet.setFillAfter(true);

        return animationSet;
    }

    @Override
    protected int getGravity() {
        return Gravity.BOTTOM | Gravity.CENTER;
    }

}
