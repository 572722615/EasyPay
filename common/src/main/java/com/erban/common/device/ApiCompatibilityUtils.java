// Copyright 2013 The Chromium Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package com.erban.common.device;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.HandlerThread;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Utility class to use new APIs
 */
public class ApiCompatibilityUtils {

    private ApiCompatibilityUtils() {
    }

    /**
     * @see android.view.View#setBackground(android.graphics.drawable.Drawable)
     */
    @SuppressLint("NewApi")
    public static void setBackgroundForView(View view,
                                            Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeGlobalOnLayoutListener(
            View view, ViewTreeObserver.OnGlobalLayoutListener listener) {

        if (view == null || listener == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void quitSafely(HandlerThread handlerThread) {
        if (handlerThread == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            handlerThread.quitSafely();
        } else {
            handlerThread.quit();
        }
    }

}
