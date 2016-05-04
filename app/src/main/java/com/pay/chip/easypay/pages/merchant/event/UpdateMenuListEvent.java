package com.pay.chip.easypay.pages.merchant.event;


import com.pay.chip.easypay.pages.merchant.model.GoodModel;

/**
 * Created by DavidLee on 2015/12/15
 */

public class UpdateMenuListEvent {
    public GoodModel.DataEntity itemData;

    public UpdateMenuListEvent(GoodModel.DataEntity itemData) {
        this.itemData = itemData;
    }
}
