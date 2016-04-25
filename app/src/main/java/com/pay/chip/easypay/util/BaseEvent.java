package com.pay.chip.easypay.util;

/**
 * Created by DavidLee on 2015/12/12.
 */

public class BaseEvent {
    public int code;
    public String info;

    public BaseEvent(int code, String info){
        this.code = code;
        this.info = info;
    }
}
