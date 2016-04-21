package com.pay.chip.easypay.pages.merchant.model;

public class PoiItemModel  {
    public String mainAddress;
    public String secondAddress;

    public double latitude;
    public double longitude;

    public PoiItemModel(String mainAddress, String secondAddress, double latitude, double longitude) {
        this.mainAddress = mainAddress;
        this.secondAddress = secondAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "PoiItemModel{" +
                "mainAddress='" + mainAddress + '\'' +
                ", secondAddress='" + secondAddress + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
