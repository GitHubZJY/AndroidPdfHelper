package com.zjy.pdfview.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.zjy.pdfview.R;

import static android.view.Gravity.CENTER;

public class ScrollSlider extends LinearLayout {

    private float slideDownY;
    private ScrollSlideListener listener;

    public ScrollSlider(Context context) {
        this(context, null);
    }

    public ScrollSlider(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setBackgroundColor(Color.parseColor("#7D000000"));
        setGravity(CENTER);
        ImageView topArrow = new ImageView(getContext());
        topArrow.setImageResource(R.drawable.ic_top_arrow);
        addView(topArrow);

        ImageView bottomArrow = new ImageView(getContext());
        bottomArrow.setImageResource(R.drawable.ic_top_arrow);
        bottomArrow.setRotation(180f);
        addView(bottomArrow);

        ((LayoutParams)bottomArrow.getLayoutParams()).topMargin = 20;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (int i=0; i<getChildCount(); i++) {
            View childView = getChildAt(i);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)childView.getLayoutParams();
            params.width = w/2;
            params.height = h/2;
            childView.setLayoutParams(params);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                slideDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int scrollY = (int) (getY() + (event.getY() - slideDownY));
                if (listener != null) {
                    boolean canScroll = listener.scrolling(scrollY);
                    if (canScroll) {
                        //setTranslationY(scrollY);
                    }
                } else {
                    setTranslationY(scrollY);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public void setScrollSlideListener(ScrollSlideListener l) {
        this.listener = l;
    }

    public interface ScrollSlideListener {
        boolean scrolling(int scrollY);
    }
}
