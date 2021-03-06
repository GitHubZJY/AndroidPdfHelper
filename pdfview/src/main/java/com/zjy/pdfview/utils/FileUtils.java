package com.zjy.pdfview.utils;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static File writeAssetsToFile(Context context, String fileName) throws IOException {
        String localPath = context.getFilesDir().getAbsolutePath() + "/pdf/" + fileName;
        File resultFile = new File(localPath);
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
}
