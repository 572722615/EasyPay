package com.erban.common.http;

/**
 * HTTP异常的封装类
 */
public class HttpException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 4619079551592196841L;

    public HttpException() {
    }

    public HttpException(String message) {
        super(message);
    }

}
