package com.pay.chip.easypay.pages.person.event;

import com.pay.chip.easypay.util.BaseEvent;

/**
 * Created by Administrator on 2016/3/18 0018.
 */
public class UserLoginEvent extends BaseEvent {

    public UserLoginEvent(int code, String info) {
        super(code, info);
    }
}
