package com.pay.chip.easypay.pages.merchant.model;

import java.util.ArrayList;

/**
 * Created by DavidLee on 2015/12/15.
 */
public class ShoppingCartData  {
    public ArrayList<GoodModel.DataEntity> data;
    public int totalCount;
    public int totalMoney;

    public ShoppingCartData(ArrayList<GoodModel.DataEntity> data, int totalCount, int totalMoney) {
        this.data = data;
        this.totalCount = totalCount;
        this.totalMoney = totalMoney;
    }

}
