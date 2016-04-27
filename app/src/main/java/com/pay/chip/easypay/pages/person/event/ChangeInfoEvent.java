package com.pay.chip.easypay.pages.person.event;

import com.pay.chip.easypay.pages.person.model.HeadInfo;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class ChangeInfoEvent {
    public HeadInfo info;
    public ChangeInfoEvent(HeadInfo info) {
        this.info = info;
    }
}
