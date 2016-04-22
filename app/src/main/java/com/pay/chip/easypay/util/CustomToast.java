package com.pay.chip.easypay.util;

import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.pay.chip.easypay.application.PayApplication;


public class CustomToast {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    private static void showToast(String text, int duration) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(PayApplication.getAppContext(), text, duration);
//        mHandler.postDelayed(r, 1000);

        mToast.show();
    }

    public static void showToast(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        showToast(text, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Toast.makeText(PayApplication.getAppContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void showToast(int id) {
        showToast(PayApplication.getAppContext().getString(id), Toast.LENGTH_SHORT);
    }

}

