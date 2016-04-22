package com.erban.imageloader;

/**
 * 网络图片加载器（ImageLoader）的回调接口。
 */
public interface LoaderListener<T> {

    public void onLoadSuccess(KLoadContext<T> loadContext);

    public void onLoadFail(KLoadContext<T> loadContext,
                           Exception exception);

}
