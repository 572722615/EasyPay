package com.pay.chip.easypay.pages.person.event;

import com.pay.chip.easypay.util.BaseEvent;

/**
 * Created by Administrator on 2016/3/18 0018.
 */
public class UserRegisterEvent extends BaseEvent {

    public UserRegisterEvent(int code, String info) {
        super(code, info);
    }
}
