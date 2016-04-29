package com.pay.chip.easypay.pages.merchant.event;

import com.pay.chip.easypay.pages.discount.model.DiscountModel;
import com.pay.chip.easypay.util.BaseEvent;

import java.util.List;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class DiscountEvent extends BaseEvent{
    public List<DiscountModel.DummyItem> data;
    public DiscountEvent(int code, String msg, List<DiscountModel.DummyItem> data) {
        super(code, msg);
        this.data = data;
    }
}
