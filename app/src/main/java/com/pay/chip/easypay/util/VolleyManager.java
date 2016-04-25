package com.pay.chip.easypay.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/3/17.
 */
public class VolleyManager {

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context context;

    public static VolleyManager instance = null;

    private VolleyManager(Context context) {
        this.context = context;
        mRequestQueue = getRequestQueue();


    }


    public static VolleyManager getInstance(Context context) {
        if (null == instance) {
            synchronized (VolleyManager.class) {
                if (null == instance) {
                    instance = new VolleyManager(context);
                }
            }
        }
        return instance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
