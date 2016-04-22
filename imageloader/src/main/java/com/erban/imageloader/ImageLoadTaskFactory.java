package com.erban.imageloader;

public class ImageLoadTaskFactory {
    public static IImageLoadTask createHomeNetwork() {
        IImageLoadTask network = new ImageLoadTask();
        network.initialize();
        return network;
    }
}
