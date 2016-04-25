package com.pay.chip.easypay.pages.person.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Administrator on 2016/4/25 0025.
 */
public class LoginUserInfo {

    public String pass;
    public String id;
    public String telno;
    public String name;
    public String head;

    public static LoginUserInfo getFromJson(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        Gson gson = new Gson();
        LoginUserInfo info = null;
        try {
            info = gson.fromJson(jsonStr, LoginUserInfo.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }
        return info;
    }

    public static String toJsonString(LoginUserInfo loginUserInfo) {
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
