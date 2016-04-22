package com.erban.imageloader;

import android.os.Process;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 网络加载任务的线程池。专供ImageLoader使用；
 * 实际上可以放进ImageLoader，作为其内部类；但是，可以在以后修改。
 */
public class LoaderTaskPool {

    private static final Executor sSingleThreadExecutor = Executors.newSingleThreadExecutor();
    private static final SerialExecutor sSeiralExecutor = new SerialExecutor();
    private static ArrayList<InnerRunnable> arrayDeque = new ArrayList<InnerRunnable>();

    public static void execute(String key,
                               Runnable runnable,
                               boolean offerFirst) {
        sSeiralExecutor.execute(key, runnable, offerFirst);
    }

    public static boolean removeTask(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        synchronized (arrayDeque) {
            Iterator<InnerRunnable> it = arrayDeque.iterator();
            while (it.hasNext()) {
                InnerRunnable r = it.next();
                if (key.equals(r.getKey())) {
                    arrayDeque.remove(r);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containsTask(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        synchronized (arrayDeque) {
            Iterator<InnerRunnable> it = arrayDeque.iterator();
            while (it.hasNext()) {
                InnerRunnable r = it.next();
                if (key.equals(r.getKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static class SerialExecutor {
        private Runnable mRunning = null;

        public synchronized void execute(String key,
                                         final Runnable r,
                                         boolean offerFirst) {
            if (r == null || key == null) {
                return;
            }
            InnerRunnable runnable = new InnerRunnable(key) {
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    try {
                        r.run();
                    } finally {
                        mRunning = null;
                        schedualNext();
                    }
                }
            };
            synchronized (arrayDeque) {

                if (offerFirst) {
                    arrayDeque.add(0, runnable);
                }
                //                    arrayDeque.offerFirst(runnable);
                else {
                    arrayDeque.add(0, runnable);
                }
            }

            schedualNext();
        }

        private synchronized void schedualNext() {
            if (mRunning == null && arrayDeque.size() > 0) {
                synchronized (arrayDeque) {
                    mRunning = arrayDeque.get(0);
                    arrayDeque.remove(mRunning);
                }
                if (mRunning != null) {
                    sSingleThreadExecutor.execute(mRunning);
                }
            }
        }
    }

    private static class InnerRunnable implements Runnable {

        private String mKey = null;

        public InnerRunnable(String key) {
            mKey = key;
        }

        public String getKey() {
            return mKey;
        }

        @Override
        public void run() {
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof InnerRunnable) {
                if (this.mKey != null && this.mKey.equals(((InnerRunnable) o).mKey)) {
                    return true;
                }
            }
            return false;
        }

    }
}
