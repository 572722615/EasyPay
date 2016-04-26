package com.pay.chip.easypay.pages.person.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.person.event.LoginOutEvent;
import com.pay.chip.easypay.pages.person.model.LoginUserInfo;
import com.pay.chip.easypay.pages.person.view.UserPopWindow;
import com.pay.chip.easypay.util.AsyncCircleImageView;
import com.pay.chip.easypay.util.Constant;
import com.pay.chip.easypay.util.LoginDataHelper;
import com.pay.chip.easypay.util.SelectIconHelper;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import de.greenrobot.event.EventBus;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout userInfoLayout;
    private RelativeLayout userIconLayout;
    private TextView title;
    private ImageView tag;
    private AsyncCircleImageView userIconText;
    private ProgressBar loadingProgressbar;
    private RelativeLayout userName;
    private TextView userNameIcon;
    private ImageView userNameArr;
    private TextView userNameText;
    private RelativeLayout userPhone;
    private TextView userPhoneIcon;
    private ImageView userPhoneArr;
    private TextView userPhoneText;
    private Button loginOut;

    private TextView reg_head;
    private FrameLayout user_back;

    // 弹出窗口
    private UserPopWindow menuWindow;

    private ProgressDialog mDialog ;

    private String phone;
    private String name;
    private String head;
    private Uri headUri;

    private void initView() {
        userInfoLayout = (LinearLayout) findViewById(R.id.user_info_layout);
        userIconLayout = (RelativeLayout) findViewById(R.id.user_icon_layout);
        userIconLayout.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        tag = (ImageView) findViewById(R.id.tag);
        userIconText = (AsyncCircleImageView) findViewById(R.id.user_icon_text);
        loadingProgressbar = (ProgressBar) findViewById(R.id.loadingProgressbar);
        userName = (RelativeLayout) findViewById(R.id.user_name);
        userNameIcon = (TextView) findViewById(R.id.user_name_icon);
        userNameArr = (ImageView) findViewById(R.id.user_name_arr);
        userNameText = (TextView) findViewById(R.id.user_name_text);
        userPhone = (RelativeLayout) findViewById(R.id.user_phone);
        userPhoneIcon = (TextView) findViewById(R.id.user_phone_icon);
        userPhoneArr = (ImageView) findViewById(R.id.user_phone_arr);
        userPhoneText = (TextView) findViewById(R.id.user_phone_text);
        loginOut = (Button) findViewById(R.id.login_out);
        loginOut.setOnClickListener(this);

        user_back = (FrameLayout) findViewById(R.id.leftFL);
        user_back.setOnClickListener(this);
        reg_head = (TextView) findViewById(R.id.titleTV);
        reg_head.setText(getResources().getString(R.string.info));

        mDialog = new ProgressDialog(this) ;
        mDialog.setCanceledOnTouchOutside(false);

    }



    LoginUserInfo loginUserInfo;

    public static void startUserInfoActivity(Context context, String loginUserInfo) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        Bundle b = new Bundle();
        b.putString(Constant.KEY_CARD_LOGIN_INFO, loginUserInfo);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    private void perseIntent() {
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            return;
        }

        Bundle b = intent.getExtras();
        loginUserInfo = LoginUserInfo.getFromJson(b.getString(Constant.KEY_CARD_LOGIN_INFO));
        phone = loginUserInfo.telno;
        name = loginUserInfo.name;
        head = loginUserInfo.head;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        perseIntent();
        initView();
        fillText();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private void fillText() {
        if (phone != null) {
            userPhoneText.setText(phone);
        }

        if (name != null) {
            userNameText.setText(name);
        }
        if (head != null) {
            userIconText.setImageURL(head, false);
        }
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.login_out:
                LoginDataHelper.getInstance().doLogout(UserInfoActivity.this);
                break;

            case R.id.leftFL:
                finish();
                break;

            case R.id.user_icon_layout:
                    popWindowForIcon();
                    break;
        }
    }

    private void popWindowForIcon() {
        // 实例化SelectPicPopupWindow
        menuWindow = new UserPopWindow(UserInfoActivity.this, (android.view.View.OnClickListener) itemsOnClick);
        // 显示窗口
        menuWindow.showAtLocation(
                        findViewById(R.id.user_info_layout),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private android.view.View.OnClickListener itemsOnClick = new android.view.View.OnClickListener() {

        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.user_changeFromCamera:
                    // 拍照获取
                    SelectIconHelper.getInstance().showCameraDialog(UserInfoActivity.this);
                    break;
                // 选择本地
                case R.id.user_changeFromSD:
                    SelectIconHelper.getInstance().showSettingFaceDialog(UserInfoActivity.this);
                    break;
                default:
                    break;
            }
        }

    };

    public void onEventMainThread(LoginOutEvent event) {
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case SelectIconHelper.SELECT_PIC_CODE:
//                    SelectIconHelper.getInstance().startPhotoCrop(this, intent.getData());
                    if (data != null) {
//                        Bundle extras = data.getExtras();
                        headUri = data.getData();
                        if(headUri!=null){
                            try
                            {
                                // 读取uri所在的图片
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), headUri);
                                uploadImg(bitmap);
                            }catch (Exception  e){

                            }
                            /**
                             * 上传服务器代码
                             */
//                        setPicToView(head);//保存在SD卡中
//                        ivHead.setImageBitmap(head);//用ImageView显示出来
                        }
                    }
                    break;
                case SelectIconHelper.SELECT_CAMERA_CODE:
//                    if (intent == null || intent.getData() == null) {
//                        SelectIconHelper.getInstance().startPhotoCrop(this, SelectIconHelper.getInstance().getCameraTempUri());
//                    } else {
//                        SelectIconHelper.getInstance().startPhotoCrop(this, intent.getData());
//                    }

                    break;
                default:
                    break;
            }
        }



        }

    public void uploadImg(Bitmap bitmap) throws AuthFailureError {
        mDialog.setMessage("图片上传中...");
        mDialog.show();

       /* UploadApi.uploadImg(UserInfoActivity.this, phone,bitmap, new ResponseListener<String>() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("zgy", "===========VolleyError=========" + error);
                Toast.makeText(UserInfoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }

            @Override
            public void onResponse(String response) {
              *//*  response = response.substring(response.indexOf("img src="));
                response = response.substring(8, response.indexOf("/>"));
                Log.v("zgy", "===========onResponse=========" + response);*//*
                mDialog.dismiss();
                Toast.makeText(UserInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }
        }) ;*/

        OkHttpClient mOkHttpClient = new OkHttpClient();
        File file = new File(Environment.getExternalStorageDirectory(), "balabala.mp4");

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(Headers.of(
                                "Content-Disposition",
                                "form-data; name=\"telno\""),
                        RequestBody.create(null, phone))
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=head;"
                        ), fileBody)
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.103:8080/okHttpServer/fileUpload")
                .post(requestBody)
                .build();

        Call call = mOkHttpClient.newCall(request);



    }



}
