package com.erban.common.util.bitmap;

public final class SocialBitmapSize {

    public static final boolean isValid(SocialBitmapSize size) {
        return size != null && size.isValid();
    }

    public static final boolean isStrictValid(SocialBitmapSize size) {
        return size != null && size.isStrictValid();
    }

    public int expWidth = -1;
    public int expHeight = -1;

    public SocialBitmapSize(int expWidth, int expHeight) {
        this.expWidth = expWidth;
        this.expHeight = expHeight;
    }

    private final boolean isValid() {
        return expWidth > 0 || expHeight > 0;
    }

    private final boolean isStrictValid() {
        return expWidth > 0 && expHeight > 0;
    }

}
