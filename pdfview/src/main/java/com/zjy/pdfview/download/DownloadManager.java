package com.zjy.pdfview.download;

import android.content.Context;

import androidx.annotation.NonNull;

import com.zjy.pdfview.utils.PdfLog;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

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
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Sink sink = null;
                String resultPath = "";
                BufferedSink bufferedSink = null;
                try {
                    String folderPath = context.getFilesDir().getAbsolutePath() + "/pdf/";
                    File folder = new File(folderPath);
                    if (!folder.exists()) {
                       folder.mkdir();
                    }
                    File[] cacheList = folder.listFiles();
                    if (cacheList != null && cacheList.length >= 10) {
                        for (int i = 0; i < cacheList.length; i++) {
                            File childFile = cacheList[i];
                            childFile.delete();
                        }
                    }
                    File dest = new File(folderPath, url.substring(url.lastIndexOf("/") + 1));
                    if (dest.exists()) {
                        resultPath = dest.getAbsolutePath();
                        PdfLog.logError("download cache exist");
                        return;
                    }
                    sink = Okio.sink(dest);
                    bufferedSink = Okio.buffer(sink);
                    bufferedSink.writeAll(response.body().source());

                    bufferedSink.close();
                    PdfLog.logInfo("download success");
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
                    if (bufferedSink != null) {
                        bufferedSink.close();
                    }
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
