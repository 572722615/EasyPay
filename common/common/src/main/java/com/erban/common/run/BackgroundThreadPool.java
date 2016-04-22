/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.erban.common.run;

import android.os.HandlerThread;

/**
 * 静态常驻一个没有任务数限制的ThreadPool，作为接收任务的执行单位。
 */
public class BackgroundThreadPool {

    static private HandlerThread sLooperThread;
//    static private ExecutorService mThreadPool;

    static {
        sLooperThread = new HandlerThread("BackgroundHandler", HandlerThread.NORM_PRIORITY);
        sLooperThread.start();
//        mThreadPool = Executors.newCachedThreadPool();
    }

    /**
     * 在没有任务个数限制的Executor中运行runnable
     *
     * @param runnable
     */
    public static void executeOnFullTaskExecutor(final Runnable runnable) {
//        mThreadPool.execute(runnable);

        Thread t = new Thread() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        t.start();
    }

    /**
     * 得到没有任务个数限制的Executor
     *
     * @return
     */
//    public static ExecutorService getFullTaskExecutor() {
//        return mThreadPool;
//    }

    //    /**
    //     * 获得后台线程的Looper。
    //     * */
    //    public static Looper getLooper() {
    //        if (!sLooperThread.isAlive()) {
    //            KLog.w("BackgroundHandler", "sLooperThread has died, renew a HandlerThread instance");
    //
    //            HandlerThread t = sLooperThread;
    //            t.interrupt();
    //            t = null;
    //
    //            sLooperThread = new HandlerThread("BackgroundHandler", HandlerThread.MIN_PRIORITY);
    //            sLooperThread.start();
    //        }
    //
    //        return sLooperThread.getLooper();
    //    }
    private BackgroundThreadPool() {
    }
}
