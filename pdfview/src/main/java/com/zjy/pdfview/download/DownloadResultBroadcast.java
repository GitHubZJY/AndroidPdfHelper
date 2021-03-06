package com.zjy.pdfview.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.zjy.pdfview.constants.Constants.DOWNLOAD_RESULT;
import static com.zjy.pdfview.constants.Constants.DOWNLOAD_STATE;
import static com.zjy.pdfview.constants.Constants.DownloadState.COMPLETE;
import static com.zjy.pdfview.constants.Constants.DownloadState.FAIL;
import static com.zjy.pdfview.constants.Constants.DownloadState.SUCCESS;

/**
 * Date: 2021/1/26
 * Author: Yang
 * Describe:
 */
public class DownloadResultBroadcast extends BroadcastReceiver {

    IDownloadCallback mCallback;

    public void setResultCallback(IDownloadCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int state = intent.getIntExtra(DOWNLOAD_STATE, COMPLETE);
        String resultPath = intent.getStringExtra(DOWNLOAD_RESULT);
        if (mCallback != null) {
           switch (state) {
               case SUCCESS:
                   mCallback.downloadSuccess(resultPath);
                   break;
               case FAIL:
                   mCallback.downloadFail();
                   break;
               case COMPLETE:
                   mCallback.downloadComplete(resultPath);
                   break;
           }
        }
    }

}
