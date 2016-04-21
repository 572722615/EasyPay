package com.pay.chip.easypay.util;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

public class SharePreferenceUtil {

    private static boolean mDebug = false;

    @SuppressLint("NewApi")
    public static void applyToEditor(SharedPreferences.Editor editor) {
        if (mDebug)
            Log.d("erbang_wifi", "SDK_INT  = " + android.os.Build.VERSION.SDK_INT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static void commitToEditor(SharedPreferences.Editor editor) {
        if (mDebug)
            Log.d("erbang_wifi", "SDK_INT  = " + android.os.Build.VERSION.SDK_INT);

        editor.commit();
    }
}
