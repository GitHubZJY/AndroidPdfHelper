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

        //PdfPreviewUtils.previewPdf(this, "https://www.citicsf.com/e-futures/attached/20210126/07/52/6/131f0932a252e357a846bff3ec2ea3e0.pdf");
    }

}