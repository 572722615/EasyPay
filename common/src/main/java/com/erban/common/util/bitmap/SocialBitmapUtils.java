package com.erban.common.util.bitmap;

import android.graphics.BitmapFactory;

public class SocialBitmapUtils {

    public static SocialBitmapSize calcSuitableSize(
            SocialBitmapSize bitmapSize, SocialBitmapSize containerSize) {

        if (!SocialBitmapSize.isStrictValid(bitmapSize) ||
                !SocialBitmapSize.isStrictValid(containerSize)) {

            return null;
        }

        SocialBitmapSize size = new SocialBitmapSize(0, 0);

        float bitmapRatio = 1.0f * bitmapSize.expWidth / bitmapSize.expHeight;
        float containerRatio = 1.0f * containerSize.expWidth / containerSize.expHeight;

        if (bitmapRatio > containerRatio) {
            size.expWidth = containerSize.expWidth;
            size.expHeight = (int) (1.0f * size.expWidth / bitmapSize.expWidth * bitmapSize.expHeight);
        } else {
            size.expHeight = containerSize.expHeight;
            size.expWidth = (int) (1.0f * size.expHeight / bitmapSize.expHeight * bitmapSize.expWidth);
        }

        return size;
    }

    public static int calcBitmapInSampleSize(
            BitmapFactory.Options opt, SocialBitmapSize size) {

        if (opt == null || !SocialBitmapSize.isValid(size)) {
            return 1;
        }

        int width = opt.outWidth;
        int height = opt.outHeight;

        int inSampleSize = 1;
        if (width > size.expWidth || height > size.expHeight) {
            int widthRatio = size.expWidth > 0 ?
                    Math.round(width * 1.0f / size.expWidth) : -1;
            int heightRatio = size.expHeight > 0 ?
                    Math.round(height * 1.0f / size.expHeight) : -1;

            if (widthRatio > 0 && heightRatio > 0) {
                inSampleSize = Math.min(widthRatio, heightRatio);
            } else if (widthRatio > 0) {
                inSampleSize = widthRatio;
            } else {
                inSampleSize = heightRatio;
            }
        }
        return inSampleSize;
    }

}
