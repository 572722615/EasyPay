package com.pay.chip.easypay.pages.person.event;

import com.pay.chip.easypay.util.BaseEvent;

/**
 * Created by Administrator on 2016/4/26 0026.
 */
public class LoginOutEvent extends BaseEvent{
    public LoginOutEvent(int code, String msg) {
        super(code, msg);
    }
}
