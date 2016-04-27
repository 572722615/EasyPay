package com.pay.chip.easypay.pages.merchant.event;

import com.pay.chip.easypay.pages.merchant.model.MerchantModel;
import com.pay.chip.easypay.util.BaseEvent;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class MerchantEvent extends BaseEvent{
    public List<MerchantModel.DataEntity> data;
    public MerchantEvent(int code, String msg,List<MerchantModel.DataEntity> data) {
        super(code, msg);
        this.data = data;
    }
}
