package com.erban.imageloader;

import android.content.Context;
import android.net.ConnectivityManager;

import com.erban.common.http.HttpException;
import com.erban.common.http.HttpManager;
import com.erban.common.http.HttpMsg;
import com.erban.common.http.HttpMsg.AbstractHttpMsgListener;
import com.erban.common.http.HttpMsg.ResponseType;

import java.util.HashMap;

/**
 * 网络加载任务的执行接口的实现类。专供ImageLoader使用；
 * 实际上可以放进ImageLoader，作为其内部类；但是，可以在以后修改。
 */
public class ImageLoadTask implements IImageLoadTask {

    private HttpManager mHttp;

    public ImageLoadTask() {
        if (null == mHttp) {
            mHttp = HttpManager.getInstance();
        }
    }

    /**
     * 不知道写在这里是否合适，等做sdk 2.0的时候再rebuild吧。先把实现方法写在这里;
     * 检测当前网络是否可用。包括wifi, 3G, 2G
     */
    public static boolean isNetWorkAvailable(Context actCtx) {
        if (actCtx != null) {
            ConnectivityManager connManager = (ConnectivityManager) actCtx.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null) {
                return connManager.getActiveNetworkInfo().isAvailable();
            }
        }
        return false;
    }

    public int getImage(final String url,
                        final KLoadListener<byte[]> listener) {
        HttpMsg msg = new HttpMsg(url);
        msg.setType(ResponseType.BINARY);
        msg.setListener(new AbstractHttpMsgListener() {

            public void beforeSend() {
                if (listener != null) {
                    listener.startLoad();
                }
            }

            @Override
            public void onResponse(int responseCode,
                                   HashMap<String, String> headers,
                                   int responseLength,
                                   byte[] respData) {
                listener.onSucc(respData);
            }

            @Override
            public void onError(HttpException exception) {
                listener.onFail(exception);
            }

            @Override
            public void onSocketTimeOut() {
                //
            }

            public void afterSend() {
                if (listener != null) {
                    listener.endLoad();
                }
            }
        });

        if (mHttp != null) {
            return mHttp.send(msg);
        } else {
            return -1;
        }
    }

    public void cancelTask(int taskId) {
        if (mHttp != null) {
            mHttp.cancel(taskId);
        }
    }

    public void cancelQueuedTask(int taskId) { // 仅仅取消排队中的task，不取消已经开始下载的任务
        if (mHttp != null) {
            mHttp.cancelMsgInQuery(taskId);
        }
    }

    @Override
    public int initialize() {
        //        if (mHttp == null) {
        //            mHttp = new HttpManager();
        //        }
        //        mHttp.init();
        return 0;
    }

    @Override
    public int destroy() {
        //        if (mHttp != null) {
        //            mHttp.destroy();
        //        }
        //        mHttp = null;
        return 0;
    }

}
