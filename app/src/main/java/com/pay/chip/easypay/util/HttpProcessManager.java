package com.pay.chip.easypay.util;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pay.chip.easypay.pages.merchant.event.MerchantEvent;
import com.pay.chip.easypay.pages.merchant.model.MerchantModel;
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


    public StringRequest loginStudent(String url, final String user_telno, final String user_pass) {
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
                }, new Response.ErrorListener() {

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


    public StringRequest findMerchant(String url, final String user_telno, final String user_pass) {
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
//                map.put(Constant.TELNO, user_telno);
//                map.put(Constant.PASS, user_pass);
                return map;
            }

        };
        return request;
    }
}


