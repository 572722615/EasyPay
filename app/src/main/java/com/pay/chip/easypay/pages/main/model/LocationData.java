package com.pay.chip.easypay.pages.main.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 百度地图信息
 */
public class LocationData {
    public String latitude;
    public String longitude;
    public String city;

    public static LocationData getFromJson(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        Gson gson = new Gson();
        LocationData info = null;
        try {
            info = gson.fromJson(jsonStr, LocationData.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }
        return info;
    }

    public static String toJsonString(LocationData loginUserInfo) {
        if (loginUserInfo == null) {
            return null;
        }
        String js = null;
        Gson gson = new Gson();
        try {
            js = gson.toJson(loginUserInfo);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }

        return js;
    }

}
