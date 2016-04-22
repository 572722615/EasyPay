package com.erban.common.crash;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFileStorage {

    private static final String TAG = "EBLog";

    public static final String LOG_SUFFIX = ".log";
    public static final String FILE_HEAD = "wfddCrash_";
    public static final String FILE_HEAD_LOG = "wfddLog_";
    private static final String CHARSET = "UTF-8";

    private static LogFileStorage sInstance;

    private Context mContext;

    private LogFileStorage(Context ctx) {
        mContext = ctx.getApplicationContext();
    }

    public static synchronized LogFileStorage getInstance(Context ctx) {
        if (ctx == null) {
            LogHelper.e(TAG, "Context is null");
            return null;
        }
        if (sInstance == null) {
            sInstance = new LogFileStorage(ctx);
        }
        return sInstance;
    }

    public File getUploadLogFile() {
        File dir = mContext.getFilesDir();
        File logFile = new File(dir, LogCollectorUtility.getMid(mContext)
                + LOG_SUFFIX);
        if (logFile.exists()) {
            return logFile;
        } else {
            return null;
        }
    }

    public boolean deleteUploadLogFile() {
        File dir = mContext.getFilesDir();
        File logFile = new File(dir, LogCollectorUtility.getMid(mContext)
                + LOG_SUFFIX);
        return logFile.delete();
    }

    public boolean saveExceptionFile2Internal(String logString) {
        try {
            File dir = mContext.getFilesDir();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File logFile = new File(dir, LogCollectorUtility.getMid(mContext)
                    + LOG_SUFFIX);
            FileOutputStream fos = new FileOutputStream(logFile, true);
            fos.write(logString.getBytes(CHARSET));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveExceptionFile2SDcard(String logString, boolean isAppend) {
        if (!LogCollectorUtility.isSDcardExsit()) {
            return false;
        }
        try {
            File logDir = getExternalLogDir("Crash");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);

            File logFile = new File(logDir, FILE_HEAD + str
                    + LOG_SUFFIX);
            FileOutputStream fos = new FileOutputStream(logFile, isAppend);
            fos.write(logString.getBytes(CHARSET));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean saveAppLogFile2SDcard(String tag, String logString, boolean isAppend) {
        if (!LogCollectorUtility.isSDcardExsit()) {
            LogHelper.e(TAG, "sdcard not exist");
            return false;
        }
        try {
            File logDir = getExternalLogDir("Log");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            File logFile = new File(logDir, FILE_HEAD_LOG + str + LOG_SUFFIX);
            Log.d(tag, logString);
            FileOutputStream fos = new FileOutputStream(logFile, isAppend);
            fos.write(logString.getBytes(CHARSET));
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private File getExternalLogDir(String tailName) {
        File logDir = LogCollectorUtility.getExternalDir(mContext, tailName);
        return logDir;
    }
}
