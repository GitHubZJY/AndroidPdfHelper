package com.zjy.pdfview.widget;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Date: 2021/1/27
 * Author: Yang
 * Describe:
 */
public class PdfRecyclerView extends RecyclerView {
    public PdfRecyclerView(@NonNull Context context) {
        super(context);
    }

    public PdfRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PdfRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return false;
    }
}
