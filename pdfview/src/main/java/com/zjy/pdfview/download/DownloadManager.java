package com.zjy.pdfview.download;

import android.content.Context;

import androidx.annotation.NonNull;

import com.zjy.pdfview.utils.FileUtils;
import com.zjy.pdfview.utils.PdfLog;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Date: 2021/1/26
 * Author: Yang
 * Describe:
 */
public class DownloadManager {

    private IDownloadCallback mCallback;

    public DownloadManager(IDownloadCallback callback) {
        mCallback = callback;
    }

    public void downloadFile(final Context context, final String url) {
        final long startTime = System.currentTimeMillis();
        PdfLog.logInfo("download url=" + url);
        PdfLog.logInfo("download startTime=" + startTime);

        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // 下载失败
                //e.printStackTrace();
                PdfLog.logError("download failed" + e.toString());
                if (mCallback != null) {
                    mCallback.downloadFail();
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                String resultPath = "";
                try {
                    File dest = FileUtils.writeNetToFile(context, url, response);
                    PdfLog.logInfo("download totalTime=" + (System.currentTimeMillis() - startTime));
                    resultPath = dest.getAbsolutePath();
                    if (mCallback != null) {
                        mCallback.downloadSuccess(resultPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PdfLog.logError("download failed: " + e.getMessage());
                    if (mCallback != null) {
                        mCallback.downloadFail();
                    }
                } finally {
                    if (mCallback != null) {
                        mCallback.downloadComplete(resultPath);
                    }
                }
            }
        });
    }

    public void cancel() {
        //mContext = null;

    }
}
