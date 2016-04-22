package com.erban.common.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.erban.common.io.FileUtils;
import com.erban.common.io.IOUtils;
import com.erban.common.runtime.ApplicationDelegate;
import com.erban.common.util.KLog;
import com.erban.common.util.MD5Util;
import com.erban.common.util.bitmap.SocialBitmapSize;
import com.erban.common.util.bitmap.SocialBitmapUtils;

import java.io.*;

public class KFileCacheManager implements IKCacheManager {

    public static final String FILE_CACHE_DIR = "/wifi510";

    public static enum Type {
        USER_LOCATE,
        AUTO_LOCATE,
        NEWS_LIST,
        READED_NEWS,
        LIKED_NEWS,
        STEPED_NEWS,
        VIEWED_NEWS,
        PRAISE_JOKE,
        VIEWED_NEWS2,
        CLICK_REPORT,
        SHOW_REPORT,
        DETAIL_TIME_REPORT,
        LAST_SHOWN_REPORT
    }

    private static final long DEFAULT_EXPIRE_MILLLON_SECOND = 2L * 24 * 60 * 60 * 1000;

    private long mDefaultExpireMillionSecond = DEFAULT_EXPIRE_MILLLON_SECOND;
    private File mDefaultDataDir = null;
    private File mDefaultImgDir = null;

    private static KFileCacheManager mInstance = null;

    /**
     * 公共缓存管理器，比如图片等资源可以统一使用这个来存储。
     *
     * @return
     */
    public static synchronized KFileCacheManager getInstance() {
        if (mInstance == null) {
            mInstance = new KFileCacheManager();
            mInstance.checkExpired();
        }
        return mInstance;
    }

    private KFileCacheManager() {
        String path = getCacheRoot(ApplicationDelegate.getApplicationContext());
        if (!TextUtils.isEmpty(path)) {
            mDefaultDataDir = new File(path + FILE_CACHE_DIR + "/.data");
            mDefaultImgDir = new File(path + FILE_CACHE_DIR + "/.image");

            if (!mDefaultDataDir.exists()) {
                mDefaultDataDir.mkdirs();
            }

            if (!mDefaultImgDir.exists()) {
                mDefaultImgDir.mkdirs();
            }
        }
    }

    /**
     * 对于新闻等数据，不同的session，我们希望放在不同的缓存目录中，防止文件相互覆盖。
     *
     * @param context
     * @param folderName 　存放session数据的目录
     */
    /*public KFileCacheManager(Context context,
                             String folderName) {
        String path = getFileRoot(context);
        if (!TextUtils.isEmpty(path)) {
            mDefaultDataDir = new File(path + FILE_CACHE_DIR + "_" + folderName + "/.data");
            mDefaultImgDir = new File(path + FILE_CACHE_DIR + "_" + folderName + "/.image");

            if (!mDefaultDataDir.exists()) {
                mDefaultDataDir.mkdirs();
            }

            if (!mDefaultImgDir.exists()) {
                mDefaultImgDir.mkdirs();
            }
        }
    }*/

    /**
     * 这个目下我们放置新闻内容资源。优先放置在外置卡上。
     *
     * @param context
     * @return
     */
    public String getFileRoot(Context context) {
        if (context == null) {
            return null;
        }
        String filePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) != null) {
            File file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            filePath = file.getPath();
        } else {
            filePath = context.getFilesDir().getPath();
        }
        return filePath;
    }

    /**
     * 这个目录下我们希望放置缓存的图片资源。优先放置在外置卡上。
     *
     * @param context
     * @return
     */
    public String getCacheRoot(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File root = Environment.getExternalStorageDirectory();
            if (root != null && root.canWrite()) {
                // 外置存储卡根目录，优先放置在这里是因为希望多个新闻客户端可以共享图片资源。
                cachePath = root.getPath();
            } else {
                // 外置存储卡App缓存目录
                cachePath = context.getExternalCacheDir().getPath();
            }
        } else {
            // 内置存储卡缓存目录
            cachePath = context.getCacheDir().getPath();
        }

        return cachePath;
    }

    /**
     * @return not InternalSdcard and ExternalSdcard will return null
     */
    public String getImgDirPath() {
        if (mDefaultImgDir != null) {
            return mDefaultImgDir.getAbsolutePath();
        }
        return null;
    }

    private void checkExpired() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (mDefaultImgDir != null && mDefaultImgDir.exists()) {
                    File[] files = mDefaultImgDir.listFiles();
                    if (files != null) {
                        for (File f : files) {
                            if (System.currentTimeMillis() - f.lastModified() > mDefaultExpireMillionSecond) {
                                f.delete();
                            }
                        }
                    }
                }
            }

        }).start();
    }

    public File getFile(String key) {
        if (TextUtils.isEmpty(key) || !isParentDirAvailable()) {
            return null;
        }

        String fileName;
        try {
            fileName = MD5Util.getMD5String(key.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
        File file = new File(mDefaultImgDir, fileName);
        if (file.exists()) {
            return file;
        }
        return null;

    }

    public Bitmap decodeAvatorBitmap(String key, int maxSpace) {
        if (TextUtils.isEmpty(key) || !isParentDirAvailable()) {
            return null;
        }

        String fileName;
        try {
            fileName = MD5Util.getMD5String(key.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        try {
            File file = new File(mDefaultImgDir, fileName);
            if (file.exists()) {
                BitmapFactory.Options opt1 = new BitmapFactory.Options();
                opt1.inJustDecodeBounds = true;

                FileInputStream stream1 = new FileInputStream(file);
                BitmapFactory.decodeStream(stream1, null, opt1);
                stream1.close();

                if (opt1.outWidth > maxSpace || opt1.outHeight > maxSpace) {
                    BitmapFactory.Options opt2 = new BitmapFactory.Options();
                    opt2.inJustDecodeBounds = false;
                    opt2.inSampleSize = SocialBitmapUtils.calcBitmapInSampleSize(
                            opt1, new SocialBitmapSize(maxSpace, maxSpace));

                    FileInputStream stream2 = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, opt2);
                    stream2.close();

                    return bitmap;
                } else {
                    return BitmapFactory.decodeFile(file.getAbsolutePath());
                }
            }
        } catch (OutOfMemoryError oom) {
            KMemCacheManager.getInstance().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object get(String key) {
        if (TextUtils.isEmpty(key) || !isParentDirAvailable()) {
            return null;
        }

        String fileName;
        try {
            fileName = MD5Util.getMD5String(key.getBytes("utf-8"));
            KLog.d("sdk_cache", "in get>> fileName = " + fileName);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
        if (isDataFile(key)) {
            KLog.d("sdk_cache", "in get>> isDataFile");
            Object o = FileUtils.deserializeFromFile(new File(mDefaultDataDir, fileName));
            KLog.d("sdk_cache", "in get o>> o");
            return o;
        } else {
            KLog.d("sdk_cache", "in get>> else");
            try {
                File file = new File(mDefaultImgDir, fileName);
                if (file.exists()) {
                    return BitmapFactory.decodeFile(file.getAbsolutePath());
                }
            } catch (OutOfMemoryError oom) {
                KMemCacheManager.getInstance().clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean put(String key,
                       Object o) throws IllegalArgumentException {
        return put(key, o, -1);
    }

    @Override
    public boolean put(String key,
                       Object o,
                       long expired) {
        KLog.d("sdk_cache", "in put");
        if (TextUtils.isEmpty(key) || o == null || !isParentDirAvailable()) {
            KLog.d("sdk_cache", "return false");
            return false;
        }

        String fileName;
        try {
            fileName = MD5Util.getMD5String(key.getBytes("utf-8"));
            KLog.d("sdk_cache", "in put <<<><>fileName = " + fileName);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return false;
        }
        if (o instanceof byte[]) {
            KLog.d("sdk_cache", "o instanceof byte[] ");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(mDefaultDataDir, fileName));
                KLog.d("sdk_cache", " key=" + key + " filename=" + mDefaultDataDir + fileName);
                fos.write((byte[]) o);
                fos.close();
                KLog.d("sdk_cache", "byte[] write success");
                return true;
            } catch (FileNotFoundException e) {
                KLog.d("sdk_cache", "FileNotFoundException e = " + e);
                return false;
            } catch (IllegalStateException e) {
                KLog.d("sdk_cache", "IllegalStateException e = " + e);
                return false;
            } catch (IOException e) {
                KLog.d("sdk_cache", "IOException e = " + e);
                return false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                    fos = null;
                }
            }
        } else if (o instanceof Serializable) {
            KLog.d("sdk_cache", "o instanceof Serializable");
            return FileUtils.serializeToFile((Serializable) o, new File(mDefaultDataDir, fileName));
        } else if (o instanceof Bitmap && !((Bitmap) o).isRecycled()) {
            KLog.d("sdk_cache", "o instanceof Bitmap && !((Bitmap) o).isRecycled() ");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File(mDefaultImgDir, fileName));
                return ((Bitmap) o).compress(CompressFormat.PNG, 100, fos);
            } catch (FileNotFoundException e) {
                return false;
            } catch (IllegalStateException e) {
                return false;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                    fos = null;
                }
            }
        } else {
            return false;
        }
    }

    public byte[] saveImageFile(String filePath,
                                byte[] byteData) {
        boolean bSucc = true;

        Bitmap bitmap = getBitmapByBytes(byteData);
        if (bitmap == null) {
            return null;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filePath));
            bSucc = bitmap.compress(CompressFormat.PNG, 80, fos);
        } catch (FileNotFoundException e) {
            bSucc = false;
        } catch (IllegalStateException e) {
            bSucc = false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    bSucc = false;
                }
                fos = null;
            }
        }

        // 如果文件保存失败，删掉
        if (!bSucc) {
            File f = new File(filePath);
            f.delete();
        }

        return Bitmap2Bytes(bitmap);
    }

    private byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 80, baos);
        return baos.toByteArray();
    }

    private final static int IMAGE_MAX_WIDTH = 200;
    private final static int IMAGE_MAX_HEIGHT = 140;

    /**
     * 根据图片字节数组，对图片可能进行二次采样，不致于加载过大图片出现内存溢出
     *
     * @param bytes
     * @return
     */
    public static Bitmap getBitmapByBytes(byte[] bytes) {

        // 对于图片的二次采样,主要得到图片的宽与高
        int width = 0;
        int height = 0;
        int sampleSize = 1; // 默认缩放为1
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 仅仅解码边缘区域
        // 如果指定了inJustDecodeBounds，decodeByteArray将返回为空
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        // 得到宽与高
        height = options.outHeight;
        width = options.outWidth;
        // 图片实际的宽与高，根据默认最大大小值，得到图片实际的缩放比例
        while ((height / sampleSize > IMAGE_MAX_HEIGHT) || (width / sampleSize > IMAGE_MAX_WIDTH)) {
            sampleSize *= 2;
        }

        // 不再只加载图片实际边缘
        options.inJustDecodeBounds = false;
        // 并且制定缩放比例
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public long getLength(String key) {
        if (TextUtils.isEmpty(key) || !isParentDirAvailable()) {
            return -1;
        }

        String fileName;
        try {
            fileName = MD5Util.getMD5String(key.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return -1;
        }
        File file = new File(mDefaultDataDir, fileName);
        if (!file.exists()) {
            return -1;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            int ava = fileInputStream.available();
            return ava;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
        return -1;
    }

    @Override
    public boolean delete(String key) {
        if (TextUtils.isEmpty(key) || !isParentDirAvailable()) {
            return false;
        }

        String fileName;
        try {
            fileName = MD5Util.getMD5String(key.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return false;
        }

        if (isDataFile(key)) {
            return new File(mDefaultDataDir, fileName).delete();
        } else {
            return new File(mDefaultImgDir, fileName).delete();
        }
    }

    @Override
    public boolean clear() {
        if (!isParentDirAvailable()) {
            return false;
        }

        FileUtils.deleteDir(mDefaultDataDir);
        FileUtils.deleteDir(mDefaultImgDir);
        return true;
    }

    @Override
    public boolean contains(String key) {
        if (TextUtils.isEmpty(key) || !isParentDirAvailable()) {
            return false;
        }

        String fileName;
        try {
            fileName = MD5Util.getMD5String(key.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return false;
        }
        File file = null;
        if (isDataFile(key)) {
            file = new File(mDefaultDataDir, fileName);
        } else {
            file = new File(mDefaultImgDir, fileName);
        }

        return file.exists();
    }

    @Override
    public boolean isExpired(String key) {
        return false;
    }

    @Override
    public void destroy() {

    }

    public static final String CACHE_FILE_NAME = "disk_cache_for_tabs";

    private boolean isDataFile(String key) {
        if (CACHE_FILE_NAME.equals(key)) {
            return true;
        }

        Type[] values = Type.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].name().equals(key)) {
                return true;
            } else if (key.startsWith(values[i].name())) {
                return true;
            }
        }
        return false;

    }

    private boolean isParentDirAvailable() {
        boolean available = mDefaultDataDir != null
                && (mDefaultDataDir.exists() || mDefaultDataDir.mkdirs())
                && mDefaultDataDir.isDirectory();
        available = available
                && (mDefaultImgDir != null && (mDefaultImgDir.exists() || mDefaultImgDir.mkdirs()) && mDefaultImgDir
                .isDirectory());
        return available;
    }

    @Override
    public byte[] getByteArray(String key) {
        if (TextUtils.isEmpty(key) || !isParentDirAvailable()) {
            return null;
        }

        String fileName;
        try {
            fileName = MD5Util.getMD5String(key.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return null;
        }
        ByteArrayOutputStream bos = null;
        FileInputStream fis = null;
        try {
            File file = new File(mDefaultDataDir, fileName);
            if (file.exists()) {
                bos = new ByteArrayOutputStream();
                fis = new FileInputStream(file);
                byte[] buffer = new byte[1024 * 10];

                while (true) {
                    int len = fis.read(buffer);
                    if (len <= 0) {
                        break;
                    }
                    bos.write(buffer, 0, len);
                }
                return bos.toByteArray();
            }
        } catch (OutOfMemoryError oom) {
            KMemCacheManager.getInstance().clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(bos);
        }
        return null;
    }

}
