package com.pay.chip.easypay.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.erban.common.common.DimenUtils;
import com.erban.common.run.MainThreadHandler;
import com.erban.imageloader.ImageLoader;
import com.erban.imageloader.KLoadContext;
import com.erban.imageloader.LoaderListener;
import com.pay.chip.easypay.R;

public class AsyncCircleImageView extends ImageView {

    public static final String TAG = AsyncCircleImageView.class.getSimpleName();
    private String mUrl;
    private static final byte NONE = 0;
    public Bitmap bmp;

    public interface ImageDownloadListener {
        void success(Bitmap bitmap);

        void fail();
    }

    private Animation mAnimAlpha;
    private int mMaxSpace;

    private boolean haveBackgroundRing;
    private int backgroundRingColor;
    private float backgroundThickness;

    public AsyncCircleImageView(Context context) {
        this(context, null);
    }

    public AsyncCircleImageView(
            Context context,
            AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public AsyncCircleImageView(
            Context context,
            AttributeSet attrs,
            int defStyle) {

        super(context, attrs, defStyle);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,
                                                                          R.styleable.AsyncCircleImageView,
                                                                          0, 0);
        haveBackgroundRing = attributes.getBoolean(R.styleable.AsyncCircleImageView_have_background_ring, true);
        backgroundRingColor = attributes.getColor(R.styleable.AsyncCircleImageView_background_ring_color, Color.WHITE);
        backgroundThickness = attributes.getDimension(R.styleable.AsyncCircleImageView_background_ring_thickness, 6);
        attributes.recycle();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm == null) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int width = bm.getWidth();
        int height = bm.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(width / 2, height / 2, width > height ? height / 2 : width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bm, 0, 0, paint);

        if (haveBackgroundRing) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(backgroundRingColor);
            paint.setStrokeWidth(backgroundThickness);
            canvas.drawCircle(width / 2, height / 2,
                              (width > height ? (height / 2 - backgroundThickness / 2) : (width / 2 - backgroundThickness / 2)),
                              paint);
        }

        super.setImageBitmap(bitmap);
    }

    public void setImageURL(
            final String url,
            final boolean anim) {

        setImageURL(url, anim, R.drawable.default_icon);
    }

    public void setImageUrl(
            String url,
            ImageDownloadListener listener) {

        setImageURL(url, listener, false, NONE);
    }

    public void setImageURL(
            final String url,
            final boolean anim,
            final int defaultImgRes) {

        setImageURL(url, null, anim, defaultImgRes);
    }

    private void setImageURL(
            final String url,
            final ImageDownloadListener listener,
            final boolean anim,
            final int defaultImgRes) {

        if (isInEditMode()) {
            if (defaultImgRes != NONE) {
                setImageResource(defaultImgRes);
            } else {
                setImageBitmap(null);
            }

            if (listener != null) {
                listener.fail();
            }
            return;
        }

        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(mUrl) && !url.equalsIgnoreCase(mUrl)) {
            setImageResource(defaultImgRes);
        }

        mUrl = url;
        if (TextUtils.isEmpty(url)) {
            MainThreadHandler.postOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (defaultImgRes != NONE) {
                        setImageResource(defaultImgRes);
                    } else {
                        setImageBitmap(null);
                    }
                }
            });

            if (listener != null) {
                listener.fail();
            }
            return;
        }


        KLoadContext<Bitmap> context = new KLoadContext<>(url);
        ImageLoader.getInstance(getContext()).loadAvatorImage(context, new LoaderListener<Bitmap>() {
            @Override
            public void onLoadSuccess(KLoadContext<Bitmap> loadContext) {
                bmp = loadContext.getResult();
                if (null != bmp && url.equals(mUrl)) {
                    MainThreadHandler.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != bmp && url.equals(mUrl)) {
                                if (anim) {
                                    Animation anim = getAnimAlpha();
                                    if (anim.hasStarted()) {
                                        anim.cancel();
                                    }
                                    anim.start();
                                }
                                setImageBitmap(bmp);
                            }
                        }
                    });
                }

                if (listener != null) {
                    listener.success(bmp);
                }
            }

            @Override
            public void onLoadFail(
                    KLoadContext<Bitmap> loadContext,
                    Exception exception) {

                MainThreadHandler.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (defaultImgRes != NONE) {
                            setImageResource(defaultImgRes);
                        } else {
                            setImageBitmap(null);
                        }
                    }
                });

                if (listener != null) {
                    listener.fail();
                }
            }
        }, getMaxSpace());
    }

    private Animation getAnimAlpha() {
        if (mAnimAlpha == null) {
            mAnimAlpha = AnimationUtils.loadAnimation(
                    getContext(), com.erban.imageloader.R.anim.alpha_anim);
        }
        return mAnimAlpha;
    }

    private int getMaxSpace() {
        if (mMaxSpace <= 0) {
            mMaxSpace = DimenUtils.dp2px(getContext(), 50.0f);
        }
        return mMaxSpace;
    }

    public Bitmap getImageBitmap(){
        return bmp;
    }

}
