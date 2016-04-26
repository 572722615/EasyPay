package com.pay.chip.easypay.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by moon.zhong on 2015/3/2.
 */
public class UploadApi {

    /**
     * 上传图片接口
     * @param bitmap 需要上传的图片
     * @param listener 请求回调
     */
    public static void uploadImg(Context context,String phone,Bitmap bitmap,ResponseListener listener) throws AuthFailureError {
        List<FormImage> imageList = new ArrayList<FormImage>() ;
        imageList.add(new FormImage(bitmap)) ;
        Request request = new PostUploadRequest(Constant.UPLOAD_HEAD_URL,phone,imageList,listener) ;
        VolleyManager.getInstance(context).addToRequestQueue(request);
    }
}
