package com.pay.chip.easypay.pages.merchant.event;

import com.pay.chip.easypay.util.BaseEvent;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class OrderEvent extends BaseEvent{
    public OrderEvent(int code, String msg) {
        super(code, msg);
    }
}
