package com.pay.chip.easypay.util;

/**
 * Created by DavidLee on 2015/12/12.
 */

public class BaseEvent {
    public int code;
    public String msg;

    public BaseEvent(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
