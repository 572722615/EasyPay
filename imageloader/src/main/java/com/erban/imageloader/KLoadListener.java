
package com.erban.imageloader;

/**
 * 异步加载的网络回调接口。
 */
public interface KLoadListener<T> {
    public void startLoad();

    public void onSucc(T result);

    public void onFail(Exception exception);

    public void onSocketTimeOut();

    public void endLoad();
}
