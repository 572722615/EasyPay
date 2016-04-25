package com.pay.chip.easypay.pages.person.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public class BaseResult {

    /**
     * static : 0
     * info : 登陆失败
     */

    private int code;
    private String info;

    public void setCode(int code) {
        this.code = code;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static BaseResult getFromJson(String json) {
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        BaseResult info = null;
        try {
            info = gson.fromJson(json, BaseResult.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }
        return info;
    }
}
