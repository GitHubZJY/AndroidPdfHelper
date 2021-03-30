package com.zjy.pdfview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2021/3/29
 * Author: Yang
 * Describe:
 */
public abstract class AbsControllerBar extends FrameLayout implements IPDFController {


    protected List<OperateListener> mListener;

    public AbsControllerBar(@NonNull Context context) {
        this(context, null);
    }

    public AbsControllerBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsControllerBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    protected abstract View getView();

    private void initView(Context context) {
        View view = getView();
        if (view == null) {
            return;
        }
        addView(view);
        mListener = new ArrayList<>();
    }

    protected void clickPrevious() {
        for (OperateListener listener : mListener) {
            if (listener != null) {
                listener.clickPrevious();
            }
        }
    }

    protected void clickNext() {
        for (OperateListener listener : mListener) {
            if (listener != null) {
                listener.clickNext();
            }
        }
    }

    @Override
    public void addOperateListener(OperateListener listener) {
        if (mListener != null && listener != null) {
            mListener.add(listener);
        }
    }

    @Override
    public void setPageIndexText(String text) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mListener != null) {
            mListener.clear();
        }
    }
}
