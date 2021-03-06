package com.pay.chip.easypay.util;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pay.chip.easypay.pages.discount.model.DiscountModel;
import com.pay.chip.easypay.pages.merchant.event.DiscountEvent;
import com.pay.chip.easypay.pages.merchant.event.GoodEvent;
import com.pay.chip.easypay.pages.merchant.event.MerchantEvent;
import com.pay.chip.easypay.pages.merchant.event.OrderEvent;
import com.pay.chip.easypay.pages.merchant.model.GoodModel;
import com.pay.chip.easypay.pages.merchant.model.MerchantModel;
import com.pay.chip.easypay.pages.person.event.UserChangeNameEvent;
import com.pay.chip.easypay.pages.person.event.UserLoginEvent;
import com.pay.chip.easypay.pages.person.event.UserRegisterEvent;
import com.pay.chip.easypay.pages.person.model.BaseResult;
import com.pay.chip.easypay.pages.person.model.UserData;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static com.pay.chip.easypay.util.UserUtils.stripSAE;

public class HttpProcessManager {
    public static HttpProcessManager instance = null;

    public static HttpProcessManager getInstance() {
        if (null == instance) {
            synchronized (HttpProcessManager.class) {
                if (null == instance) {
                    instance = new HttpProcessManager();
                }
            }
        }
        return instance;
    }


    public StringRequest loginUser(String url, final String user_telno, final String user_pass) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        String res = stripSAE(arg0);
                        UserData result = UserData.getFromJson(res);
                        UserLoginEvent baseEvent = new UserLoginEvent(result.getCode(), result.getInfo(),result.getData());
                        EventBus.getDefault().post(baseEvent);
                        return;
                    }
                }
                , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {


                EventBus.getDefault().post(new UserLoginEvent(Constant.CODE_FAIL, null,null));
                return;
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //传递参数
                Map<String, String> map = new HashMap<String, String>();
                map.put(Constant.TELNO, user_telno);
                map.put(Constant.PASS, user_pass);
                return map;
            }

        };
        return request;
    }

    public StringRequest registerStudent(String url, final String user_telno, final String user_pass) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        String res = stripSAE(arg0);
                        BaseResult result = BaseResult.getFromJson(res);
                        UserRegisterEvent baseEvent = new UserRegisterEvent(result.getCode(), result.getInfo());
                        EventBus.getDefault().post(baseEvent);
                        return;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {


                EventBus.getDefault().post(new UserRegisterEvent(Constant.CODE_FAIL, null));
                return;
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //传递参数
                Map<String, String> map = new HashMap<String, String>();
                map.put(Constant.TELNO, user_telno);
                map.put(Constant.PASS, user_pass);
                return map;
            }

        };
        return request;
    }


    public StringRequest findMerchant(String url, final String lat, final String lng) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        String res = stripSAE(arg0);
                        MerchantModel result = MerchantModel.getFromJson(res);
                        MerchantEvent baseEvent = new MerchantEvent(result.getCode(), result.getMsg(),result.getData());
                        EventBus.getDefault().post(baseEvent);
                        return;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {


                EventBus.getDefault().post(new MerchantEvent(Constant.CODE_FAIL, null,null));
                return;
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //传递参数
                Map<String, String> map = new HashMap<String, String>();
                map.put(Constant.LAT, lat);
                map.put(Constant.LNG, lng);
                return map;
            }

        };
        return request;
    }

    public StringRequest findGoods(String url, final String id) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        String res = stripSAE(arg0);
                        GoodModel result = GoodModel.getFromJson(res);
                        GoodEvent baseEvent = new GoodEvent(result.getCode(),result.getMsg() ,result.getData());
                        EventBus.getDefault().post(baseEvent);
                        return;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {


                EventBus.getDefault().post(new GoodEvent(Constant.CODE_FAIL, null,null));
                return;
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //传递参数
                Map<String, String> map = new HashMap<String, String>();
                map.put(Constant.M_ID, id);
                return map;
            }

        };
        return request;
    }

    public StringRequest getDicount(String url) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        String res = stripSAE(arg0);
                        DiscountModel result = DiscountModel.getFromJson(res);
                        DiscountEvent baseEvent = new DiscountEvent(result.getCode(),result.getMsg() ,result.getData());
                        EventBus.getDefault().post(baseEvent);
                        return;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {


                EventBus.getDefault().post(new DiscountEvent(Constant.CODE_FAIL, null,null));
                return;
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //传递参数
                Map<String, String> map = new HashMap<String, String>();
                return map;
            }

        };
        return request;
    }


    public StringRequest changeName(String url,final String id ,final String name) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        String res = stripSAE(arg0);
                        BaseResult result = BaseResult.getFromJson(res);
                        UserChangeNameEvent baseEvent = new UserChangeNameEvent(result.getCode(),result.getInfo() );
                        EventBus.getDefault().post(baseEvent);
                        return;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {


                EventBus.getDefault().post(new UserChangeNameEvent(Constant.CODE_FAIL, null));
                return;
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //传递参数
                Map<String, String> map = new HashMap<String, String>();
                map.put(Constant.NAME,name);
                map.put(Constant.ID,id);
                return map;
            }

        };
        return request;
    }

    public StringRequest doOrder(String url,final  String userId, final String merchantId, final String seatNum, final String info,   final String totalPrice, final String remark, final String peopleNum) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        String res = stripSAE(arg0);
                        BaseResult result = BaseResult.getFromJson(res);
                        OrderEvent baseEvent = new OrderEvent(result.getCode(),result.getInfo() );
                        EventBus.getDefault().post(baseEvent);
                        return;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {


                EventBus.getDefault().post(new OrderEvent(Constant.CODE_FAIL, null));
                return;
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //传递参数
                Map<String, String> map = new HashMap<String, String>();
                map.put(Constant.U_ID,userId);
                map.put(Constant.M_ID,merchantId);
                map.put(Constant.SEAT_NUM,seatNum);
                map.put(Constant.ORDER_INFO,info);
                map.put(Constant.TOTAL_PRICE,totalPrice);
                map.put(Constant.PEOPLE_NUM,peopleNum);
                map.put(Constant.REMARK,remark);
                return map;
            }

        };
        return request;
    }
}


