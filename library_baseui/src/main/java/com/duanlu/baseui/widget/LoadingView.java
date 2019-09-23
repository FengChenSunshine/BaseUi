package com.duanlu.baseui.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.duanlu.baseui.R;
import com.duanlu.utils.DisplayUtils;

/********************************
 * @name LoadingView
 * @author 段露
 * @createDate 2019/3/6  14:12.
 * @updateDate 2019/3/6  14:12.
 * @version V1.0.0
 * @describe 加载菊花Loading.
 ********************************/
public class LoadingView extends View {
    private int mSize;
    private int mPaintColor;
    private int mAnimateValue;
    private ValueAnimator mAnimator;
    private Paint mPaint;
    private static final int LINE_COUNT = 12;
    private static final int DEGREE_PER_LINE = 30;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;

    public LoadingView(Context context) {
        this(context, (AttributeSet) null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.LoadingStyle);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mAnimateValue = 0;
        this.mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                LoadingView.this.mAnimateValue = (Integer) animation.getAnimatedValue();
                LoadingView.this.invalidate();
            }
        };
        TypedArray array = this.getContext().obtainStyledAttributes(attrs, R.styleable.LoadingView, defStyleAttr, 0);
        this.mSize = array.getDimensionPixelSize(R.styleable.LoadingView_loading_view_size, DisplayUtils.dp2px(context, 32.0F));
        this.mPaintColor = array.getInt(R.styleable.LoadingView_android_color, -1);
        array.recycle();
        this.initPaint();
    }

    public LoadingView(Context context, int size, int color) {
        super(context);
        this.mAnimateValue = 0;
        this.mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                LoadingView.this.mAnimateValue = (Integer) animation.getAnimatedValue();
                LoadingView.this.invalidate();
            }
        };
        this.mSize = size;
        this.mPaintColor = color;
        this.initPaint();
    }

    private void initPaint() {
        this.mPaint = new Paint();
        this.mPaint.setColor(this.mPaintColor);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setColor(int color) {
        this.mPaintColor = color;
        this.mPaint.setColor(color);
        this.invalidate();
    }

    public void setSize(int size) {
        this.mSize = size;
        this.requestLayout();
    }

    public void start() {
        if (this.mAnimator == null) {
            this.mAnimator = ValueAnimator.ofInt(new int[]{0, 11});
            this.mAnimator.addUpdateListener(this.mUpdateListener);
            this.mAnimator.setDuration(600L);
            this.mAnimator.setRepeatMode(ValueAnimator.RESTART);
            this.mAnimator.setRepeatCount(-1);
            this.mAnimator.setInterpolator(new LinearInterpolator());
            this.mAnimator.start();
        } else if (!this.mAnimator.isStarted()) {
            this.mAnimator.start();
        }

    }

    public void stop() {
        if (this.mAnimator != null) {
            this.mAnimator.removeUpdateListener(this.mUpdateListener);
            this.mAnimator.removeAllUpdateListeners();
            this.mAnimator.cancel();
            this.mAnimator = null;
        }

    }

    private void drawLoading(Canvas canvas, int rotateDegrees) {
        int width = this.mSize / 12;
        int height = this.mSize / 6;
        this.mPaint.setStrokeWidth((float) width);
        canvas.rotate((float) rotateDegrees, (float) (this.mSize / 2), (float) (this.mSize / 2));
        canvas.translate((float) (this.mSize / 2), (float) (this.mSize / 2));

        for (int i = 0; i < 12; ++i) {
            canvas.rotate(30.0F);
            this.mPaint.setAlpha((int) (255.0F * (float) (i + 1) / 12.0F));
            canvas.translate(0.0F, (float) (-this.mSize / 2 + width / 2));
            canvas.drawLine(0.0F, 0.0F, 0.0F, (float) height, this.mPaint);
            canvas.translate(0.0F, (float) (this.mSize / 2 - width / 2));
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(this.mSize, this.mSize);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saveCount = canvas.saveLayer(0.0F, 0.0F, (float) this.getWidth(), (float) this.getHeight(), (Paint) null, Canvas.ALL_SAVE_FLAG);
        this.drawLoading(canvas, this.mAnimateValue * 30);
        canvas.restoreToCount(saveCount);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.start();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.stop();
    }

    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == 0) {
            this.start();
        } else {
            this.stop();
        }
    }

}