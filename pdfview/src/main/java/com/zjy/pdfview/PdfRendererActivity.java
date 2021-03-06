package com.zjy.pdfview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

/**
 * Date: 2021/1/26
 * Author: Yang
 * Describe:
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PdfRendererActivity extends Activity {

    private static final String PDF_URL_KEY = "PDF_URL_KEY";

    PdfView pdfView;
    ImageView backIv;

    public static void startPreview(Context context, String url) {
        Intent intent = new Intent(context, PdfRendererActivity.class);
        intent.putExtra(PDF_URL_KEY, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfrenderer);

        backIv = findViewById(R.id.back_iv);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String url = getIntent().getStringExtra(PDF_URL_KEY);
        pdfView = findViewById(R.id.pdf_view);
        pdfView.loadPdf(url);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        pdfView.release();
    }
}
