package com.pay.chip.easypay.util;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pay.chip.easypay.pages.person.event.UserLoginEvent;
import com.pay.chip.easypay.pages.person.model.BaseResult;

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


    public StringRequest loginStudent(String url, final String student_name, final String student_pass) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        String res = stripSAE(arg0);
                        BaseResult result = BaseResult.getFromJson(res);
                        UserLoginEvent baseEvent = new UserLoginEvent(result.getCode(), result.getInfo());
                        CustomToast.showToast(result.toString());
                        EventBus.getDefault().post(baseEvent);
                        return;
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {


                EventBus.getDefault().post(new UserLoginEvent(Constant.CODE_FAIL, null));
                return;
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //传递参数
                Map<String, String> map = new HashMap<String, String>();
                map.put(Constant.ID, student_name);
                map.put(Constant.PASS, student_pass);
                return map;
            }

        };
        return request;
    }


}

