package com.duanlu.baseui.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duanlu.baseui.widget.LoadingView;
import com.duanlu.baseui.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/********************************
 * @name HintDialog
 * @author 段露
 * @createDate 2019/3/6  14:05.
 * @updateDate 2019/3/6  14:05.
 * @version V1.0.0
 * @describe HintDialog.
 ********************************/
public class HintDialog extends BaseDialog {

    private LoadingView mLoadingView;
    private ImageView mIvIcon;
    private TextView mTvMessage;
    private boolean isAutoCancel;
    private static final long DELAY_CANCEL = 1500L;

    private HintDialog(@NonNull Context context) {
        super(context);
    }

    protected int getLayoutResId() {
        return R.layout.baseui_dialog_loading;
    }

    @Override
    protected boolean fullscreenEnabled() {
        return false;
    }

    protected void initView() {
        this.mLoadingView = this.findViewById(R.id.loading_view);
        this.mIvIcon = this.findViewById(R.id.iv_icon);
        this.mTvMessage = this.findViewById(R.id.tv_message);
        this.setCancelable(false);
    }

    private void setMessage(CharSequence message) {
        this.mTvMessage.setText(message);
    }

    private void setIcon(boolean isLoadingIcon, Drawable iconDrawable) {
        if (isLoadingIcon) {
            this.mLoadingView.setVisibility(View.VISIBLE);
        } else if (null != iconDrawable) {
            this.mIvIcon.setImageDrawable(iconDrawable);
        }
    }

    private void setAutoCancel(boolean autoCancel) {
        this.isAutoCancel = autoCancel;
    }

    public void show() {
        if (!this.isShowing()) {
            super.show();
            if (this.isAutoCancel) {
                delayedDismiss();
            }
        }
    }

    private void delayedDismiss() {
        this.mTvMessage.postDelayed(new Runnable() {
            public void run() {
                if (HintDialog.this.isShowing()) {
                    HintDialog.this.dismiss();
                }
            }
        }, DELAY_CANCEL);
    }

    public static class Builder {
        public static final int ICON_TYPE_NOTHING = 0;
        public static final int ICON_TYPE_LOADING = 1;
        public static final int ICON_TYPE_SUCCESS = 2;
        public static final int ICON_TYPE_FAIL = 3;
        public static final int ICON_TYPE_INFO = 4;
        private int mIconType = 0;
        private Context mContext;
        private CharSequence mMessage;
        private boolean isAutoCancel;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setIconType(int iconType) {
            this.mIconType = iconType;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.mMessage = message;
            return this;
        }

        public Builder setAutoCancel(boolean autoCancel) {
            this.isAutoCancel = autoCancel;
            return this;
        }

        public HintDialog create() {
            HintDialog dialog = new HintDialog(this.mContext);
            if (1 == this.mIconType) {
                dialog.setIcon(true, (Drawable) null);
            }

            dialog.setMessage(this.mMessage);
            dialog.setAutoCancel(this.isAutoCancel);
            return dialog;
        }

        public HintDialog show() {
            HintDialog dialog = this.create();
            dialog.show();
            return dialog;
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface IconType {
        }
    }

}