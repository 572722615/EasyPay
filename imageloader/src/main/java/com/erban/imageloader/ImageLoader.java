package com.erban.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.erban.common.cache.KCacheConfig;
import com.erban.common.cache.KFileCacheManager;
import com.erban.common.cache.KMemCacheManager;

import java.io.File;
import java.util.HashMap;

public class ImageLoader {

    private static final int MAX_IMAGE_WIDTH = 600;
    private static final int MAX_IMAGE_HEIGHT = 600;
    private static ImageLoader mInstance = null;
    private Context mApplicationContext = null;
    private IImageLoadTask mLoadTask = null;
    private KMemCacheManager mKMemCacheManager = null;
    private KFileCacheManager mKFileCacheManager = null;
    private KCacheConfig mKCacheSettings = null;
    private HashMap<String, Integer> mTaskMap = null;

    private ImageLoader(Context context) {
        if (mApplicationContext == null) {
            mApplicationContext = context.getApplicationContext();
        }

        if (mLoadTask == null) {
            mLoadTask = ImageLoadTaskFactory.createHomeNetwork();
        }

        if (mKMemCacheManager == null) {
            mKMemCacheManager = KMemCacheManager.getInstance();
        }

        if (mKFileCacheManager == null) {
            mKFileCacheManager = KFileCacheManager.getInstance();
            //			mKFileCacheManager = new KFileCacheManager(context, "ImageLoader");
        }

        if (mKCacheSettings == null) {
            mKCacheSettings = new KCacheConfig();
        }

        if (mTaskMap == null) {
            mTaskMap = new HashMap<String, Integer>();
        }
    }

    public static synchronized ImageLoader getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ImageLoader(context);
        }
        return mInstance;
    }

    /**
     * 解析图片数据流
     *
     * @param imagedata
     * @param isFit     :是否要fit设定的宽度和高度
     * @return
     */
    public static Bitmap decodeBytesArray(byte[] imagedata,
                                          boolean isFit,
                                          int fitWidth,
                                          int fitHeight) {
        if (null == imagedata) {
            return null;
        }

        Bitmap bmp = null;
        if (!isFit) {
            try {
                // 如果不需要适配屏幕，则直接获取原图
                bmp = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length);
                return bmp;
            } catch (OutOfMemoryError ex) {
                // 获取失败，则按照屏幕获取
                ex.printStackTrace();
            }
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;

        try {
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length, opts);
            if (opts.mCancel || opts.outWidth == -1 || opts.outHeight == -1) {
                return null;
            }
            opts.inSampleSize = computeSampleSize(opts, -1, fitWidth * fitHeight);
            opts.inJustDecodeBounds = false;
            opts.inScaled = false;

            bmp = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length, opts);
        } catch (OutOfMemoryError ex2) {
            try {
                opts.inSampleSize += 1;
                opts.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length, opts);
            } catch (OutOfMemoryError e) {
                try {
                    opts.inSampleSize += 1;
                    opts.inJustDecodeBounds = false;
                    bmp = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length, opts);
                } catch (OutOfMemoryError ex) {
                    return null;
                }
            }
        }

        return bmp;
    }

    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength,
                                         int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength,
                                                int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(
                Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(
                        h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static synchronized void destroy() {
        if (mInstance != null) {
            mInstance.destroyInternal();
        }
    }

    public Bitmap loadImageFromMem(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        if (mKMemCacheManager.contains(url)) {
            Bitmap bitmap = (Bitmap) mKMemCacheManager.get(url);
            return bitmap;
        }
        return null;
    }

    public void loadImage(final KLoadContext<Bitmap> loadContext,
                          final LoaderListener<Bitmap> listener) {

        if (loadContext == null) {
            return;
        }
        final String imageURL = loadContext.getImageUrl();

        if (mKMemCacheManager.contains(imageURL)) {
            Bitmap bitmap = (Bitmap) mKMemCacheManager.get(imageURL);
            if (bitmap != null && !bitmap.isRecycled()) {
                loadContext.setResult(bitmap);
                if (listener != null) {
                    listener.onLoadSuccess(loadContext);
                }
                return;
            }
        }

        Runnable r = new Runnable() {
            public void run() {
                if (mKFileCacheManager.contains(imageURL)) {
                    Bitmap bitmap = (Bitmap) mKFileCacheManager.get(imageURL);
                    if (bitmap != null && !bitmap.isRecycled()) {
                        mKMemCacheManager.put(imageURL, bitmap);

                        loadContext.setResult(bitmap);
                        if (listener != null) {
                            listener.onLoadSuccess(loadContext);
                        }
                    } else {
                        executeLoad(loadContext, listener);
                    }
                } else {
                    executeLoad(loadContext, listener);
                }
            }
        };

        LoaderTaskPool.execute(imageURL, r, false);
    }

    public void loadAvatorImage(
            final KLoadContext<Bitmap> loadContext,
            final LoaderListener<Bitmap> listener, final int maxSpace) {

        final String memKey = loadContext.getImageUrl();

        if (mKMemCacheManager.contains(memKey)) {
            Bitmap bitmap = (Bitmap) mKMemCacheManager.get(memKey);
            if (bitmap != null && listener != null) {
                loadContext.setResult(bitmap);
                listener.onLoadSuccess(loadContext);
                return;
            }
        }
        Runnable runnable = new Runnable() {
            public void run() {
                if (mKFileCacheManager.contains(memKey)) {
                    Bitmap bitmap = mKFileCacheManager.decodeAvatorBitmap(memKey, maxSpace);
                    if (bitmap != null) {
                        mKMemCacheManager.put(memKey, bitmap);
                        if (listener != null) {
                            loadContext.setResult(bitmap);
                            listener.onLoadSuccess(loadContext);
                        }
                    } else if (bitmap == null) {
                        executeLoad(loadContext, listener);
                    }
                } else {
                    executeLoad(loadContext, listener);
                }
            }
        };

        LoaderTaskPool.execute(memKey, runnable, false);
    }

    public void loadLocalImage(final KLoadContext<Bitmap> loadContext,
                               final LoaderListener<Bitmap> listener) {
        final String path = loadContext.getImageUrl();
        if (TextUtils.isEmpty(path)) {
            listener.onLoadFail(loadContext, new Exception("loadLocalImage:invalid image path"));
            return;
        }

        if (mKMemCacheManager.contains(path)) {
            Bitmap bitmap = (Bitmap) mKMemCacheManager.get(path);
            if (bitmap != null && listener != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                loadContext.setResult(bitmap);
                listener.onLoadSuccess(loadContext);
                return;
            }
        }
        Runnable runnable = new Runnable() {
            public void run() {
                File file = new File(path);
                if (file.exists()) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    } catch (OutOfMemoryError oom) {
                        oom.printStackTrace();
                        KMemCacheManager.getInstance().clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (bitmap != null) {
                        mKMemCacheManager.put(path, bitmap);
                        if (listener != null) {
                            loadContext.setResult(bitmap);
                            listener.onLoadSuccess(loadContext);
                        }
                    } else {
                        listener.onLoadFail(loadContext, new Exception(
                                "loadLocalImage:BitmapFactory.decodeFile failed:" + path));
                    }
                } else {
                    listener.onLoadFail(loadContext, new Exception(
                            "loadLocalImage:file is not existed" + path));
                }
            }
        };

        LoaderTaskPool.execute(path, runnable, false);
    }

    private void executeLoad(final KLoadContext<Bitmap> loadContext,
                             final LoaderListener<Bitmap> listener) {
        executeHttpLoad(loadContext, listener);
    }

    private void executeHttpLoad(final KLoadContext<Bitmap> loadContext,
                                 final LoaderListener<Bitmap> listener) {
        if (loadContext == null) {
            return;
        }
        final String url = loadContext.getImageUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }

        int taskId = mLoadTask.getImage(url, new KLoadListener<byte[]>() {
            @Override
            public void startLoad() {
            }

            @Override
            public void onSucc(byte[] result) {
                try {
                    // Bitmap bitmap =
                    // BitmapFactory.decodeByteArray(result, 0,
                    // result.length);

                    Bitmap bitmap = decodeBytesArray(result, true, MAX_IMAGE_WIDTH,
                            MAX_IMAGE_HEIGHT);

                    if (bitmap != null && listener != null && result.length > 50) {
                        // i just believe bitmap is bigger than 50
                        // bytes.

                        loadContext.setResult(bitmap);
                        listener.onLoadSuccess(loadContext);
                        mKFileCacheManager.put(url, bitmap);
                        mKMemCacheManager.put(url, bitmap);
                    } else {
                        listener.onLoadFail(loadContext, new Exception("Decode failed"));
                    }
                } catch (OutOfMemoryError oom) {
                    mKMemCacheManager.clear();
                }
                synchronized (mTaskMap) {
                    mTaskMap.remove(url);
                }
            }

            @Override
            public void onFail(Exception exception) {
                listener.onLoadFail(loadContext, exception);
                synchronized (mTaskMap) {
                    mTaskMap.remove(url);
                }
            }

            @Override
            public void onSocketTimeOut() {
            }

            @Override
            public void endLoad() {
            }
        });

        synchronized (mTaskMap) {
            mTaskMap.put(url, taskId);
        }
    }

    public void removeTask(String imageUrl) {
        if (!LoaderTaskPool.removeTask(imageUrl)) {
            synchronized (mTaskMap) {
                Integer intager = mTaskMap.get(imageUrl);
                if (null != intager) {
                    mLoadTask.cancelTask(intager);
                }
                mTaskMap.remove(imageUrl);
            }
        }
    }

    public void destroyInternal() {
        if (mLoadTask != null) {
            mLoadTask.destroy();
        }

        if (mKMemCacheManager != null) {
            mKMemCacheManager.destroy();
        }

        if (mKFileCacheManager != null) {
            mKFileCacheManager.destroy();
        }

        if (mTaskMap != null) {
            mTaskMap.clear();
        }
        mApplicationContext = null;
        mInstance = null;
    }
}
