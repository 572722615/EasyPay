package com.pay.chip.easypay.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by DavidLee on 2015/12/15.
 */

public class BaseRequest implements KeepBase{
    public static <T> T getFromJson(Class<T> c, String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        Gson gson = new Gson();
        T info = null;
        try {
            info = gson.fromJson(jsonStr, c);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }
        return info;
    }

    public static String toJsonString(Object data) {
        if (data == null) {
            return null;
        }
        String js = null;
        Gson gson = new Gson();
        try {
            js = gson.toJson(data);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }

        return js;
    }
}
