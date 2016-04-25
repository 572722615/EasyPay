package com.pay.chip.easypay.pages.person.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Administrator on 2016/3/17 0017.
 */
public class UserData extends BaseResult {


    /**
     * code : 1
     * msg : 登录成功
     * data : {"id":"1","telno":"13513555265","pass":"123456","head":" ","name":" "}
     */

    /**
     * id : 1
     * telno : 13513555265
     * pass : 123456
     * head :
     * name :
     */

    private LoginUserInfo data;

    public static UserData getFromJson(String json) {
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        UserData info = null;
        try {
            info = gson.fromJson(json, UserData.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }
        return info;
    }


    public void setData(LoginUserInfo data) {
        this.data = data;
    }



    public LoginUserInfo getData() {
        return data;
    }


}
