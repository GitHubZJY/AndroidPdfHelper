package com.zjy.pdfview.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;

public class FileUtils {

    public static File writeAssetsToFile(Context context, String assetPath) throws IOException {
        String fileName = assetPath.substring(assetPath.lastIndexOf("/") + 1);
        File resultFile = checkCacheSize(context, assetPath);
        if (!resultFile.exists()) {
            resultFile.createNewFile();
        }
        InputStream is = context.getAssets().open(fileName);
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(resultFile);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception ex) {
            PdfLog.logError("Exception: " + ex);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return resultFile;
    }

    public static File writeNetToFile(Context context, String url, Response response) throws IOException{
        Sink sink;
        BufferedSink bufferedSink = null;
        File dest = null;
        try {
            dest = checkCacheSize(context, url);
            if (dest.exists()) {
                PdfLog.logError("download cache exist");
                return dest;
            }
            sink = Okio.sink(dest);
            bufferedSink = Okio.buffer(sink);
            bufferedSink.writeAll(response.body().source());

            bufferedSink.close();
            PdfLog.logInfo("download success");

        } catch (Exception e) {
            e.printStackTrace();
            PdfLog.logError("download failed: " + e.getMessage());
        } finally {
            if (bufferedSink != null) {
                bufferedSink.close();
            }
        }
        return dest;
    }

    private static File checkCacheSize(Context context, String url) {
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
        return new File(folderPath, url.substring(url.lastIndexOf("/") + 1));
    }
}
