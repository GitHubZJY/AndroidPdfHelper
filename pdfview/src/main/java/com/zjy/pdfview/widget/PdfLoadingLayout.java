package com.zjy.pdfview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zjy.pdfview.R;
import com.zjy.pdfview.utils.PdfLog;

/**
 * Date: 2021/1/27
 * Author: Yang
 * Describe:
 */
public class PdfLoadingLayout extends FrameLayout {

    TextView waitTv;
    Button reloadTv;
    View waitLayout;
    LoadLayoutListener mListener;

    public PdfLoadingLayout(@NonNull Context context) {
        this(context, null);
    }

    public PdfLoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PdfLoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_pdf_loading, this);

        waitTv = findViewById(R.id.waiting_tv);
        reloadTv = findViewById(R.id.reload_tv);
        waitLayout = findViewById(R.id.waiting_layout);

        reloadTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.clickRetry();
                }
            }
        });
    }


    public void showLoading() {
        PdfLog.logDebug("showLoading");
        setVisibility(VISIBLE);
        waitTv.setText("加载中");
        reloadTv.setVisibility(GONE);
    }

    public void showContent() {
        PdfLog.logDebug("showContent");
        setVisibility(GONE);
    }

    public void showFail() {
        setVisibility(VISIBLE);
        waitTv.setText("加载失败");
        waitLayout.setVisibility(GONE);
        reloadTv.setVisibility(VISIBLE);
    }

    public void setLoadLayoutListener(LoadLayoutListener listener) {
        mListener = listener;
    }

    public interface LoadLayoutListener {
        void clickRetry();
    }
}
