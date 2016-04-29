package com.pay.chip.easypay.pages.discount.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.List;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class DiscountModel {

    private static final int COUNT = 25;
    private static final int TOTALPAGE = 4;

    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
    /*public static List<DummyItem> generyData(int page) {
        int start = page * COUNT;
        int end = TOTALPAGE == page ? start + COUNT : start + COUNT;
        List<DummyItem> items = new ArrayList<DummyItem>();
        for (int i = start; i < end; i++) {
            items.add(createDummyItem(i));
        }
        return items;
    }*/

    /**
     * 是否还有更多
     *
     * @param page
     * @return
     */
    public static boolean hasMore(int page) {
        return page < TOTALPAGE;
    }

    /*private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }*/

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        int count = position % 3;
        for (int i = 0; i < count; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
    private List<DummyItem> data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(List<DummyItem> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public List<DummyItem> getData() {
        return data;
    }
    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String name;
        public final String desc;
        public final String pic;
        public final String address;

        public DummyItem(String name, String desc, String pic,String address) {
            this.name = name;
            this.desc = desc;
            this.pic = pic;
            this.address = address;
        }

        @Override
        public String toString() {
            return desc;
        }
    }

    public static DiscountModel getFromJson(String json) {
        if (json == null) {
            return null;
        }
        Gson gson = new Gson();
        DiscountModel info = null;
        try {
            info = gson.fromJson(json, DiscountModel.class);
        } catch (JsonSyntaxException jsonSyntaxException) {
        }
        return info;
    }

}
