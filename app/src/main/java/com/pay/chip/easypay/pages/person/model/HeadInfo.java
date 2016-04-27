package com.pay.chip.easypay.pages.person.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class HeadInfo {

    /**
     * code : 1
     * msg : 上传成功
     * data : http://chip-uploads.stor.sinaapp.com/Public/Uploads/2016-04-27/572030c2857d2.png
     */


    public static HeadInfo getFromJson(String json) {
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        HeadInfo info = null;
        try {
            info = gson.fromJson(json, HeadInfo.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }
        return info;
    }
    private int code;
    private String msg;
    private String data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getData() {
        return data;
    }
}
