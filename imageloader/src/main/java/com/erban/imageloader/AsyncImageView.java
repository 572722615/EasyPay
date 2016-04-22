package com.erban.imageloader;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.erban.common.run.MainThreadHandler;
import com.erban.common.util.KLog;
import com.erban.common.util.bitmap.BitmapUtil;


public class AsyncImageView extends ImageView {
    public static final String TAG = "AsyncImageView";

    /**
     * 调试打印开关
     */
    private static final boolean VERBOSE = true;
    Animation mAlphaAnimator;
    private String mImageUrl;
    private Context mContext;

    private LoaderListener<Bitmap> mListener;
    private KLoadContext<Bitmap> mLoadFinishedCtx;
    private boolean mDownloaded = false;

    public AsyncImageView(Context context) {
        super(context);

        init(context);
    }

    public AsyncImageView(Context context,
                          AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public AsyncImageView(Context context,
                          AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    private void init(Context context) {
        mContext = context;
//        mAlphaAnimator = AnimationUtils.loadAnimation(context, R.anim.alpha_anim);
    }

    /**
     * 加载某个URL的图片
     *
     * @param url          要加载的图片url
     * @param defaultImgId 默认图片的资源id
     */
    public void setImageURL(String url,
                            int defaultImgId) {
        mListener = null;
        setImageURL(url, defaultImgId, false, false);
    }

    /**
     * 加载某个URL的图片
     *
     * @param url          要加载的图片url
     * @param defaultImgId 默认图片的资源id
     * @param listener     加载图片的结果回调
     */
    public void setmImageUrl(String url,
                             int defaultImgId,
                             LoaderListener<Bitmap> listener) {
        mListener = listener;
        setImageURL(url, defaultImgId, false, false);
    }

    /**
     * @param url
     * @param defaultImgId
     * @param isBackground 是否是把默认图用背景表示
     */
    public void setImageURL(String url,
                            int defaultImgId,
                            boolean isBackground) {
        mListener = null;
        setImageURL(url, defaultImgId, false, isBackground);
    }

    /**
     * 加载某个URL的图片
     *
     * @param url                    要加载的图片url
     * @param defaultImgId           默认图片的资源id
     * @param setDefaultIfURLInvalid true代表URL为空时是否设置默认图片，by caisenchuan
     */
    private void setImageURL(String url,
                             int defaultImgId,
                             final boolean setDefaultIfURLInvalid,
                             final boolean isBackground) {
        Bitmap b = ImageLoader.getInstance(getContext()).loadImageFromMem(url);
        if (b != null) {
            mImageUrl = url;
            setImageBitmap(b);
            return;
        }

        if (TextUtils.isEmpty(url)) {
            if (setDefaultIfURLInvalid) {
                if (isBackground) {
                    setBackgroundResource(defaultImgId);
                } else {
                    setImageResource(defaultImgId);
                }
            }
            mImageUrl = url;
            return;
        }

        if (url.equals(mImageUrl)) {
            return;
        }

        setImageResource(defaultImgId);
        //setImageResource(R.drawable.default_bmp);
        mImageUrl = url;

        if (VERBOSE) {
        }

        KLoadContext<Bitmap> context = new KLoadContext<Bitmap>(url);
        ImageLoader.getInstance(getContext()).loadImage(context, new LoaderListener<Bitmap>() {
            @Override
            public void onLoadSuccess(KLoadContext<Bitmap> loadContext) {
                if (VERBOSE) {
                }

                mLoadFinishedCtx = loadContext;
                final Bitmap bmp = loadContext.getResult();
                if (mImageUrl != null &&
                        loadContext.getImageUrl() != null &&
                        loadContext.getImageUrl().equals(mImageUrl)) {
                    if (null != bmp) {
                        MainThreadHandler.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mListener == null) {
                                    AsyncImageView.this.setImageBitmap(bmp);
                                }
                                if (isBackground) {
                                    AsyncImageView.this.setBackgroundResource(
                                            R.drawable.transparent_drawable);
                                }
                            }

                        });
                        if (mListener != null) {
                            mListener.onLoadSuccess(loadContext);
                        }
                    }
                } else {
                    if (VERBOSE) {
                        KLog.d(TAG, "setImageURL onLoadSuccess, url not match.");
                    }
                }
            }

            @Override
            public void onLoadFail(KLoadContext<Bitmap> loadContext,
                                   Exception exception) {
                // TODO:loadContext是否会为空
                KLog.d(TAG,
                        "setImageURL onLoadFail url: " + loadContext.getImageUrl() + ", exception: " + exception.toString());
                exception.printStackTrace();
                mLoadFinishedCtx = loadContext;
            }
        });
    }

    public void setImageUrlWithBlur(final String url,
                                    int bgColor) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (bgColor != 0) {
            setImageDrawable(new ColorDrawable(bgColor));
        } else {
            setImageResource(R.drawable.default_bmp);
        }

        mImageUrl = url;
        KLoadContext<Bitmap> context = new KLoadContext<Bitmap>(url);
        ImageLoader.getInstance(getContext()).loadImage(context, new LoaderListener<Bitmap>() {
            @Override
            public void onLoadSuccess(KLoadContext<Bitmap> loadContext) {
                final Bitmap bmp = loadContext.getResult();
                if (bmp != null && mImageUrl != null && mImageUrl.equals(url)) {
                    try {
                        final Bitmap bitmap;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            bitmap = BitmapUtil.blurBitmap(bmp, BitmapUtil.BlurType.BLUR_SMALL);
                        } else {
                            bitmap = bmp;
                        }

                        MainThreadHandler.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AsyncImageView.this.setImageBitmap(bitmap);
                            }
                        });
                    } catch (Exception e) {
                        MainThreadHandler.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AsyncImageView.this.setImageBitmap(bmp);
                            }
                        });
                    }
                }
            }

            @Override
            public void onLoadFail(KLoadContext<Bitmap> loadContext,
                                   Exception exception) {
            }
        });
    }

    /**
     * @date 2015.7.10
     */
    public void setImageURL(final String url,
                            final boolean anim) {
        mListener = null;
        setImageURL(url, anim, R.drawable.default_bmp);
    }

    /**
     * @date 2015.7.10
     */
    public void setImageURL(final String url,
                            final boolean anim,
                            LoaderListener<Bitmap> listener) {
        mListener = listener;
        setImageURL(url, anim, R.drawable.default_bmp);
    }

    /**
     * @date 2015.7.10
     */
    private void setImageURL(final String url,
                             final boolean anim,
                             int defaultImgRes) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        setImageResource(defaultImgRes);
        mImageUrl = url;

        if (VERBOSE) {
            KLog.d(TAG, "setImageURL url: " + url);
        }

        KLoadContext<Bitmap> context = new KLoadContext<Bitmap>(url);
        ImageLoader.getInstance(getContext()).loadImage(context, new LoaderListener<Bitmap>() {
            @Override
            public void onLoadSuccess(KLoadContext<Bitmap> loadContext) {
                if (VERBOSE) {
                    KLog.d(TAG, "setImageURL onLoadSuccess, url: " + loadContext.getImageUrl());
                }
                final Bitmap bmp = loadContext.getResult();
                mLoadFinishedCtx = loadContext;

                if (null != bmp) {
                    if (mImageUrl != null &&
                            loadContext.getImageUrl() != null &&
                            loadContext.getImageUrl().equals(mImageUrl)) {
                        MainThreadHandler.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (anim) {
                                    if (mAlphaAnimator.hasStarted()) {
                                        mAlphaAnimator.cancel();
                                    }
                                    mAlphaAnimator.start();
                                }
                                if (mListener == null) {
                                    AsyncImageView.this.setImageBitmap(bmp);
                                }
                            }

                        });
                        if (mListener != null) {
                            mListener.onLoadSuccess(loadContext);
                        }
                    } else {
                        if (VERBOSE) {
                            KLog.d(TAG, "setImageURL onLoadSuccess, url not match.");
                        }
                    }
                }
            }

            @Override
            public void onLoadFail(KLoadContext<Bitmap> loadContext,
                                   Exception exception) {
                KLog.d(TAG,
                        "setImageURL onLoadFail url: " + loadContext.getImageUrl() + ", exception: " + exception.toString());
                exception.printStackTrace();
                mLoadFinishedCtx = loadContext;
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //TODO:Gaomiao提供detach接口
        //        if(!TextUtils.isEmpty(mImageUrl)){
        //        	ImageLoader.getInstance(mContext).removeTask(mImageUrl);
        //        	if ((getDrawable() instanceof BitmapDrawable) && ((BitmapDrawable) getDrawable()).getBitmap() == null) {
        //        	    mImageUrl = "";
        //        	}
        //        }
    }

    public boolean setImageUrl(final String url,
                               LoaderListener<Bitmap> listener) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }

        KLoadContext<Bitmap> context = new KLoadContext<Bitmap>(url);
        ImageLoader.getInstance(mContext).loadImage(context, listener);

        return true;
    }


    /**
     * check the url pic if was downloaded.
     */
    public boolean isDownloaded(String url) {
        if (TextUtils.isEmpty(url) || mLoadFinishedCtx == null) {
            return false;
        }

        if (mLoadFinishedCtx.getImageUrl() != null &&
                mLoadFinishedCtx.getImageUrl().equals(url)) {
            return true;
        }
        return false;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}