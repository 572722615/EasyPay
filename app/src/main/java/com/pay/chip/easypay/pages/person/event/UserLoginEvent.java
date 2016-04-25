package com.pay.chip.easypay.pages.person.event;

import com.pay.chip.easypay.pages.person.model.LoginUserInfo;
import com.pay.chip.easypay.util.BaseEvent;

/**
 * Created by Administrator on 2016/3/18 0018.
 */
public class UserLoginEvent extends BaseEvent {

    public LoginUserInfo loginUserInfo;
    public UserLoginEvent(int code, String msg,LoginUserInfo loginUserInfo) {
        super(code, msg);
        this.loginUserInfo = loginUserInfo;
    }
}
