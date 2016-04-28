package com.pay.chip.easypay.pages.merchant.event;

/**
 * Created by Administrator on 2016/4/28 0028.
 */
public class LocationChangeEvent {
    public double latitude;
    public double longitude;

    public LocationChangeEvent(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }

}
