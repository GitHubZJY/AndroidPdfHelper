package com.zjy.androidpdfhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.zjy.pdfview.PdfPreviewUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CustomPdfViewActivity.startPreview(this, "file:///android_asset/test.pdf");

        PdfPreviewUtils.previewPdf(this, "file:///android_asset/test.pdf");
    }

}