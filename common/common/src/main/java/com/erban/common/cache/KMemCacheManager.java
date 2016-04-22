package com.erban.common.cache;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.erban.common.util.MD5Util;

import java.io.Serializable;


public class KMemCacheManager implements IKCacheManager {

    private static final long DEFAULT_MEM_THREADHOLD = 8 * 1024 * 1024;

    private static KMemCacheManager mInstance = null;
    private LruCache<String, Object> lruCache = null;

    public static synchronized KMemCacheManager getInstance() {
        if (mInstance == null)
            mInstance = new KMemCacheManager();
        return mInstance;
    }

    @SuppressLint("NewApi")
    private KMemCacheManager() {
        long memCacheSize = Runtime.getRuntime().maxMemory() / 8;
        memCacheSize = Math.min(memCacheSize, DEFAULT_MEM_THREADHOLD);
        lruCache = new LruCache<String, Object>(memCacheSize) {
            @Override
            protected long sizeOf(String key,
                                  Object value) {
                if (value instanceof Bitmap) {
                    Bitmap bt = ((Bitmap) value);
                    if (android.os.Build.VERSION.SDK_INT >= 12) {
                        return ((Bitmap) value).getByteCount();
                    } else {
                        return bt.getRowBytes() * bt.getHeight();
                    }
                } else if (value instanceof Serializable) {

                }
                return super.sizeOf(key, value);
            }

            protected void entryRemoved(boolean evicted,
                                        String key,
                                        Object oldValue,
                                        Object newValue) {
                if (evicted && oldValue != null) {
                    oldValue = null;
                }
            }
        };
    }

    @Override
    public Object get(String key) {
        if (key == null)
            return null;
        synchronized (lruCache) {
            String md5Key = MD5Util.getMD5String(key);
            Object o = lruCache.get(md5Key);
            if (o instanceof Bitmap && ((Bitmap) o).isRecycled()) {
                lruCache.remove(md5Key);
                o = null;
                return null;
            }
            return o;
        }
    }

    @Override
    public boolean put(String key,
                       Object o) {
        return put(key, o, -1);
    }

    @Override
    public boolean put(String key,
                       Object o,
                       long expired) {
        if (key != null && o != null) {
            synchronized (lruCache) {
                lruCache.put(MD5Util.getMD5String(key), o);
            }
            return true;
        } else
            return false;
    }

    @Override
    public boolean delete(String key) {
        if (key == null)
            return false;
        synchronized (lruCache) {
            lruCache.remove(MD5Util.getMD5String(key));
        }
        return true;
    }

    @Override
    public boolean clear() {
        synchronized (lruCache) {
            lruCache.evictAll();
        }
        return true;
    }

    @Override
    public boolean contains(String key) {
        if (key == null) {
            return false;
        }
        synchronized (lruCache) {
            return lruCache.contains(MD5Util.getMD5String(key));
        }
    }

    @Override
    public boolean isExpired(String key) {
        return false;
    }

    @Override
    public void destroy() {
        //        synchronized (lruCache) {
        //          lruCache.evictAll();
        //        }
        mInstance = null;
    }

    @Override
    public byte[] getByteArray(String key) {
        // TODO Auto-generated method stub
        return null;
    }

}
