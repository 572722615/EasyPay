package com.pay.chip.easypay.pages.merchant.event;

import com.pay.chip.easypay.pages.merchant.model.GoodModel;
import com.pay.chip.easypay.util.BaseEvent;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class GoodEvent extends BaseEvent{
    public List<GoodModel.DataEntity> data;
    public GoodEvent(int code, String msg, List<GoodModel.DataEntity> data) {
        super(code, msg);
        this.data = data;
    }
}
