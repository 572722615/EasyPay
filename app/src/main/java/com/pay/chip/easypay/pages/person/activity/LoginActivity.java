package com.pay.chip.easypay.pages.person.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.person.event.UserLoginEvent;
import com.pay.chip.easypay.pages.person.model.LoginUserInfo;
import com.pay.chip.easypay.util.Constant;
import com.pay.chip.easypay.util.CustomToast;
import com.pay.chip.easypay.util.HttpProcessManager;
import com.pay.chip.easypay.util.LoginDataHelper;
import com.pay.chip.easypay.util.PassEditText;
import com.pay.chip.easypay.util.PhoneEditText;
import com.pay.chip.easypay.util.UserUtils;
import com.pay.chip.easypay.util.VolleyManager;

import de.greenrobot.event.EventBus;

public class LoginActivity extends Activity implements View.OnClickListener {

    private TextView reg_head;
    private FrameLayout user_back;

    private PhoneEditText loginPhoneEdit;
    private PassEditText loginPassEdit;
    private Button loginLoginBtn;
    private TextView registerTextBtn;
    private RelativeLayout thrindLine;

    String telno;
    String pass;

    private void initView() {


        user_back = (FrameLayout) findViewById(R.id.leftFL);
        user_back.setOnClickListener(this);
        reg_head = (TextView) findViewById(R.id.titleTV);
        reg_head.setText(getResources().getString(R.string.login));

        loginPhoneEdit = (PhoneEditText) findViewById(R.id.login_phone_edit);
        loginPassEdit = (PassEditText) findViewById(R.id.login_pass_edit);
        loginLoginBtn = (Button) findViewById(R.id.login_login_btn);
        loginLoginBtn.setOnClickListener(this);
        registerTextBtn = (TextView) findViewById(R.id.register_text_btn);
        registerTextBtn.setOnClickListener(this);
        thrindLine = (RelativeLayout) findViewById(R.id.thrind_line);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);
        initView();
    }

    private void userLogin() {

        telno = loginPhoneEdit.getPhoneString();
        pass = loginPassEdit.getPassString();
        if(TextUtils.isEmpty(telno)||TextUtils.isEmpty(pass)){
            return;
        }

        if(!UserUtils.isMobileNUM(telno)){
            CustomToast.showToast(getString(R.string.not_phone));
            return;
        }

        StringRequest request = HttpProcessManager.getInstance().loginUser(Constant.HOST_USER_LOGIN, telno, pass);
        VolleyManager.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {


            case R.id.leftFL:
                finish();
                break;

            case R.id.register_text_btn:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.login_login_btn:
                userLogin();
                break;


        }
    }


    public void onEventMainThread(UserLoginEvent event) {


        int code = event.code;
        if (event.msg == null) {
            return;
        }

        if (code == Constant.CODE_FAIL) {
            CustomToast.showToast(getString(R.string.net_fail));

            return;
        } else {
            if (code == Constant.CODE_SUCCESS) {
                String data = LoginUserInfo.toJsonString(event.loginUserInfo);
                LoginDataHelper.getInstance().setLoginUserInfo(data);
//                fillText(event.infoData);
                finish();
            }
            CustomToast.showLongToast(event.msg.toString());

        }

    }

}
