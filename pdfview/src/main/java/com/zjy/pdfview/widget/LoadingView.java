package com.zjy.pdfview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class LoadingView extends View {

    private int strokeWidth;
    private int startColor;
    private int endColor;
    private int duration;
    private Paint paint;
    private float cx;
    private float cy;
    private float radius;
    private SweepGradient sweepGradient;

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int dp(int dp) {
        return (int) ((float) dp * this.getResources().getDisplayMetrics().density + 0.5F);
    }

    private void init() {
        strokeWidth = dp(2);
        startColor = Color.TRANSPARENT;
        endColor = Color.parseColor("#d8d5d8");
        duration = 1200;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        cx = getMeasuredWidth() / 2f;
        cy = getMeasuredHeight() / 2f;
        radius = this.getMeasuredWidth() / 2f - this.strokeWidth / 2f;
        sweepGradient = new SweepGradient(cx, cy, new int[]{startColor, endColor}, null);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setShader(sweepGradient);
        canvas.drawCircle(cx, cy, radius, paint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        RotateAnimation animation = new RotateAnimation(0.0F, 359.0F, 1, 0.5F, 1, 0.5F);
        animation.setDuration(duration);
        animation.setRepeatCount(-1);
        animation.setInterpolator(new LinearInterpolator());
        startAnimation(animation);
    }

    @Override
    protected void onDetachedFromWindow() {
        clearAnimation();
        super.onDetachedFromWindow();
    }
}