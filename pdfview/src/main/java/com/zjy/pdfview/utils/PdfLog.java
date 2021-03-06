package com.zjy.pdfview.utils;

import android.util.Log;

import com.zjy.pdfview.BuildConfig;

/**
 * Date: 2021/1/26
 * Author: Yang
 * Describe: 组件日志
 */
public class PdfLog {

    private static final String LOG_TAG = "PdfLog";

    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static void logInfo(String message) {
        if (!isDebug())
            return;
        if (message == null) {
            Log.i(LOG_TAG, "null");
            return;
        }
        Log.i(LOG_TAG, message);
    }

    public static void logDebug(String message) {
        if (!isDebug())
            return;
        if (message == null) {
            Log.d(LOG_TAG, "null");
            return;
        }
        Log.d(LOG_TAG, message);
    }

    public static void logWarn(String message) {
        if (!isDebug())
            return;
        if (message == null) {
            Log.w(LOG_TAG, "null");
            return;
        }
        Log.w(LOG_TAG, message);
    }

    public static void logError(String message) {
        if (!isDebug())
            return;
        if (message == null) {
            Log.e(LOG_TAG, "null");
            return;
        }
        Log.e(LOG_TAG, message);
    }

}
