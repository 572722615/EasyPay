package com.pay.chip.easypay.pages.merchant.model;

import java.util.ArrayList;

/**
 * Created by DavidLee on 2015/12/11.
 */

public class OrderDetailArray {
    public ArrayList<MenuItemData> orderDetail;

    public static class MenuItemData {
        public String id;
        public String num;
    }

}
