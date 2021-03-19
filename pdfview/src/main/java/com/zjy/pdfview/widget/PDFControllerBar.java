package com.zjy.pdfview.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zjy.pdfview.R;

import static android.view.Gravity.CENTER;

/**
 * Date: 2021/3/19
 * Author: Yang
 * Describe: PDF控制栏视图
 */
public class PDFControllerBar extends LinearLayout implements IPDFController, View.OnClickListener {

    private Button previousBtn, nextBtn;
    private TextView pageIndexTv;
    private OperateListener mListener;

    public PDFControllerBar(Context context) {
        this(context, null);
    }

    public PDFControllerBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PDFControllerBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        setOrientation(HORIZONTAL);
        setGravity(CENTER);
        setBackgroundColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(dip2px(context, 8));
        }

        previousBtn = new Button(context);
        previousBtn.setBackgroundResource(R.drawable.bg_operate_btn);
        previousBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        previousBtn.setText("上一页");
        addView(previousBtn, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dip2px(context, 36)));

        pageIndexTv = new TextView(context);
        pageIndexTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        pageIndexTv.setPadding(dip2px(context, 16), 0, dip2px(context, 16), 0);
        addView(pageIndexTv, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        pageIndexTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        pageIndexTv.setText("1/1");

        nextBtn = new Button(context);
        nextBtn.setBackgroundResource(R.drawable.bg_operate_btn);
        nextBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        nextBtn.setText("下一页");
        addView(nextBtn, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dip2px(context, 36)));

        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == previousBtn) {
            if (mListener != null) {
                mListener.clickPrevious();
            }
        } else if (view == nextBtn) {
            if (mListener != null) {
                mListener.clickNext();
            }
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void setOperateListener(OperateListener listener) {
        mListener = listener;
    }

    @Override
    public void setPageIndexText(String text) {
        if (pageIndexTv != null && text != null) {
            pageIndexTv.setText(text);
        }
    }
}
