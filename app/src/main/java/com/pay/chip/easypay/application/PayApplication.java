package com.pay.chip.easypay.application;

import android.app.Application;
import android.content.Context;

import com.pay.chip.easypay.util.LoginDataHelper;

import java.io.File;

public class PayApplication extends Application {

    private static PayApplication instance;
    private static Context appContext;
    private String mUserIconDir = null;

    public synchronized static PayApplication getInstance() {
        if (instance == null) {
            instance = new PayApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appContext = this.getApplicationContext();
        LoginDataHelper.getInstance().initBaiduSDK(appContext);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public String getUserIconDir() {
        if (mUserIconDir == null) {
            mUserIconDir = this.getFilesDir().getAbsolutePath() + File.separatorChar + "usericon" + File.separatorChar;
            File f = new File(mUserIconDir);
            if (!f.exists() || !f.isDirectory()) {
                f.mkdirs();
            }
        }
        return mUserIconDir;
    }
}
