package com.erban.common.run;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * 一个后台线程的封装类。次线程用来执行本地IO相关任务的线程。
 * 但是这个线程池不会是静态常驻，只会在第一次需要使用的时候才会被创建。
 */
public class IOThreadHandler {
    private static final String TAG = "InitializeThread";

    private static IOThreadHandler sInstance;

    private HandlerThread mIOThread;
    private IOHandler mIOHandler;

    /**
     * IO子任务线程的Looper的Handler。
     *
     * @date 2015.2.13
     */
    private class IOHandler extends Handler {
        private IOHandler(Looper looper) {
            super(looper);
        }
    }

    private IOThreadHandler() {
        mIOThread = new HandlerThread(TAG);
        mIOThread.start();
        mIOHandler = new IOHandler(mIOThread.getLooper());
    }

    public static IOThreadHandler getInstance() {
        synchronized (IOThreadHandler.class) {
            if (sInstance == null) {
                sInstance = new IOThreadHandler();
            }
        }
        return sInstance;
    }

    /**
     * 请求执行IO子任务。
     *
     * @date 2015.2.13
     */
    public static boolean postIOTask(Runnable task) {
        return getInstance().mIOHandler.post(task);
    }

    /**
     * 请求执行IO子任务。
     *
     * @date 2015.2.13
     */
    public static boolean postIOTaskDelay(Runnable task,
                                          long delay) {
        return getInstance().mIOHandler.postDelayed(task, delay);
    }

    /**
     * 请求执行IO子任务。并且将此任务放到任务队列的第一个。
     *
     * @date 2015.2.13
     */
    public static void postAtFrontOfQueue(Runnable r) {
        getInstance().mIOHandler.postAtFrontOfQueue(r);
    }
}
