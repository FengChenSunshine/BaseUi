package com.duanlu.baseui.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.duanlu.baseui.BaseConstants;
import com.duanlu.baseui.R;
import com.duanlu.utils.ActivityHelp;

/********************************
 * @name ShellActivity
 * @author 段露
 * @createDate 2019/02/12 15:25.
 * @updateDate 2019/02/12 15:25.
 * @version V1.0.0
 * @describe 作为一个壳装载其他Fragment.
 ********************************/
public class ShellActivity extends AppCompatActivity {

    private Fragment mFragmentInstance;
    private String mExtraFragmentClassName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mExtraFragmentClassName = bundle.getString(BaseConstants.EXTRA_FRAGMENT_CLASS_NAME);
            int themeResId = bundle.getInt(BaseConstants.EXTRA_THEME_RES_ID);
            if (themeResId > 0) {
                setTheme(themeResId);
            }
            int orientation = bundle.getInt(BaseConstants.EXTRA_ORIENTATION, Integer.MAX_VALUE);
            if (Integer.MAX_VALUE != orientation) {
                //设置屏幕方向.
                setRequestedOrientation(orientation);
            }
        }
        //设置视图.
        FrameLayout flContainer = new FrameLayout(this);
        flContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT));
        flContainer.setId(R.id.fl_shell_container);

        this.setContentView(flContainer);

        Fragment fragment = getFragment();
        fragment.setArguments(getIntent().getExtras());
        attachFragment(fragment);
    }

    /**
     * 谷歌在安卓8.0版本时为了支持全面屏，增加了一个限制：
     * 如果是透明的Activity，则不能固定它的方向，因为它的方向其实是依赖其父Activity的(因为透明).
     * 然而这个bug只有在8.0中有，8.1中已经修复.
     * 具体crash有两种:
     * 1.Activity的风格为透明，在manifest文件中指定了一个方向，则在onCreate中crash.
     * 2.Activity的风格为透明，如果调用setRequestedOrientation方法固定方向，则crash.
     * 参考：https://blog.csdn.net/starry_eve/article/details/82777160
     */
    @Override
    public void setRequestedOrientation(int orientation) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O
                && !ActivityHelp.isTranslucentOrFloating(this)) {
            super.setRequestedOrientation(orientation);
        }
    }

    private Fragment getFragment() {
        if (TextUtils.isEmpty(mExtraFragmentClassName)) {
            throw new IllegalArgumentException("fragment name not specified");
        }
        try {
            Class cls = Class.forName(mExtraFragmentClassName);
            return (Fragment) cls.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("ClassNotFoundException fragment not found with name " + mExtraFragmentClassName);
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("InstantiationException fragment not found with name " + mExtraFragmentClassName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("IllegalAccessException fragment not found with name " + mExtraFragmentClassName);
        }
    }

    /**
     * 加载Fragment
     *
     * @param fragment Fragment
     */
    private void attachFragment(Fragment fragment) {
        if (null == fragment) {
            throw new RuntimeException("fragment not found exception");
        }
        mFragmentInstance = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_shell_container, fragment, mExtraFragmentClassName)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mFragmentInstance) {
            mFragmentInstance.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (null == mFragmentInstance
                || (mFragmentInstance instanceof RootFragment && !((RootFragment) mFragmentInstance).dispatchOnBackPressed())
                || (mFragmentInstance instanceof BaseLazyFragment && !((BaseLazyFragment) mFragmentInstance).dispatchOnBackPressed())
        ) {
            super.onBackPressed();
        }
    }

    @Override
    public void finishAfterTransition() {
        if (mFragmentInstance instanceof RootFragment) {
            ((RootFragment) mFragmentInstance).finishAfterTransition();
        }
        super.finishAfterTransition();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if (mFragmentInstance instanceof RootFragment) {
            ((RootFragment) mFragmentInstance).onActivityReenter(resultCode, data);
        }
    }

    public String getExtraFragmentClassName() {
        return mExtraFragmentClassName;
    }

}