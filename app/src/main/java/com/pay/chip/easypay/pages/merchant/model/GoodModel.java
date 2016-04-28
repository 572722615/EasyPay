package com.pay.chip.easypay.pages.merchant.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class GoodModel {


    /**
     * code : 1
     * data : [{"id":"3","m_id":"4","name":"商品1","desc":"描述1","price":"12一个","pic":""},{"id":"5","m_id":"4","name":"商品1","desc":"描述1","price":"12一个","pic":""},{"id":"6","m_id":"4","name":"商品1","desc":"描述1","price":"12一个","pic":""},{"id":"8","m_id":"4","name":"商品1","desc":"描述1","price":"12一个","pic":"http://chip-uploads.stor.sinaapp.com/Public/Uploads/2016-04-28/5721a87ac8ad3.png"}]
     */

    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * id : 3
     * m_id : 4
     * name : 商品1

     * desc : 描述1
     * price : 12一个
     * pic :
     */
    private String msg;
    public static GoodModel getFromJson(String json) {
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        GoodModel info = null;
        try {
            info = gson.fromJson(json, GoodModel.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }
        return info;
    }

    private List<DataEntity> data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String id;
        private String m_id;
        private String name;
        private String desc;
        private String price;
        private String pic;

        public void setId(String id) {
            this.id = id;
        }

        public void setM_id(String m_id) {
            this.m_id = m_id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getId() {
            return id;
        }

        public String getM_id() {
            return m_id;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        public String getPrice() {
            return price;
        }

        public String getPic() {
            return pic;
        }
    }
}
