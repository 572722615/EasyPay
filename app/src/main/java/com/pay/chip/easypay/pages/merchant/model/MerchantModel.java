package com.pay.chip.easypay.pages.merchant.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class MerchantModel {

    /**
     * code : 1
     * msg : 成功
     * data : [{"id":"3","name":"sae上传图片","pass":"123456","lat":"39.9420167458","lng":"116.4439751540","geohash":"wx4g1xpzj7948u3me","head":"http://chip-uploads.stor.sinaapp.com/Public/Uploads/2016-04-27/5720883d94cf5.png"},{"id":"4","name":"sae上传","pass":"123456","lat":"39.9420167400","lng":"116.4439751500","geohash":"wx4g1xpzj7948eq61","head":"http://chip-uploads.stor.sinaapp.com/Public/Uploads/2016-04-27/5720898f5b048.gif"}]
     */

    private int code;
    private String msg;
    /**
     * id : 3
     * name : sae上传图片
     * pass : 123456
     * lat : 39.9420167458
     * lng : 116.4439751540
     * geohash : wx4g1xpzj7948u3me
     * head : http://chip-uploads.stor.sinaapp.com/Public/Uploads/2016-04-27/5720883d94cf5.png
     */

    public static MerchantModel getFromJson(String json) {
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        MerchantModel info = null;
        try {
            info = gson.fromJson(json, MerchantModel.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }
        return info;
    }

    private List<DataEntity> data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String id;
        private String name;
        private String pass;
        private String lat;
        private String lng;
        private String geohash;
        private String head;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        private String address;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPass(String pass) {
            this.pass = pass;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public void setGeohash(String geohash) {
            this.geohash = geohash;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getPass() {
            return pass;
        }

        public String getLat() {
            return lat;
        }

        public String getLng() {
            return lng;
        }

        public String getGeohash() {
            return geohash;
        }

        public String getHead() {
            return head;
        }
    }
}
