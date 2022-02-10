package com.zjy.androidpdfhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.zjy.pdfview.PdfView;

/**
 * Date: 2022/2/10
 * Author: Yang
 * Describe: 自定义PDF视图
 */
public class CustomPdfViewActivity extends Activity {

    private static final String PDF_URL_KEY = "PDF_URL_KEY";

    PdfView pdfView;
    ImageView backIv;

    public static void startPreview(Context context, String url) {
        Intent intent = new Intent(context, CustomPdfViewActivity.class);
        intent.putExtra(PDF_URL_KEY, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_pdf_view);

        backIv = findViewById(com.zjy.pdfview.R.id.back_iv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String url = getIntent().getStringExtra(PDF_URL_KEY);
        pdfView = findViewById(com.zjy.pdfview.R.id.pdf_view);
        pdfView.loadPdf(url);
        CustomControllerBar controllerBar = new CustomControllerBar(this);
        pdfView.setPDFController(controllerBar);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        pdfView.release();
    }
}
