package com.erban.common.runtime;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

public class ApplicationDelegate {

    /**
     * keep instance of Application
     */
    private static Application application;
    private static final String protoVersionCode = "1.0";
    private static final String app = "shine";
    private static final String source = "common";

    public static String getApp() {
        return app;
    }

    public static String getSource() {
        return source;
    }

    public static String getProtoVersionCode() {
        return protoVersionCode;
    }


    /**
     * call this api in Application.onCreate()
     *
     * @param application
     */
    public static void setApplication(Application application) {
        ApplicationDelegate.application = application;
    }

    public static Application getApplication() {
        return application;
    }

    public static Context getApplicationContext() {
        return application.getApplicationContext();
    }

    public static WindowManager getWindowManager() {
        return (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
    }
}
