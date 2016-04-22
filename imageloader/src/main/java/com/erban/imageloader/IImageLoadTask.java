/**
 * @description:
 * @create: Jun 4, 2014 4:35:43 PM
 */
package com.erban.imageloader;


/**
 * 网络加载任务的执行接口。专供ImageLoader使用；
 * 实际上可以放进ImageLoader，作为其内部类；但是，可以在以后修改。
 */
public interface IImageLoadTask {
    int initialize();

    int getImage(String url,
                 KLoadListener<byte[]> listener);

    void cancelTask(int taskId);

    void cancelQueuedTask(int taskId);

    int destroy();
}
