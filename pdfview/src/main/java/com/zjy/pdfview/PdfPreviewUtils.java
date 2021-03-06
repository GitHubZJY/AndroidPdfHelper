package com.zjy.pdfview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.zjy.pdfview.download.DownloadManager;
import com.zjy.pdfview.download.IDownloadCallback;

/**
 * Date: 2021/1/28
 * Author: Yang
 * Describe:
 */
public class PdfPreviewUtils {

    public static void previewPdf(final Context context, String url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PdfRendererActivity.startPreview(context, url);
        } else {
            DownloadManager downloadManager = new DownloadManager(new IDownloadCallback() {
                @Override
                public void downloadSuccess(String path) {

                }

                @Override
                public void downloadFail() {

                }

                @Override
                public void downloadComplete(String path) {
                    Uri uri = Uri.parse("file://" + path);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(Intent.createChooser(intent, "请选择浏览器打开"));
                    } else {
                        Toast.makeText(context,
                                "没有可用的浏览器",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            downloadManager.downloadFile(context, url);

        }
    }
}
