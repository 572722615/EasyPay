package com.pay.chip.easypay.pages.person.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.person.event.UserRegisterEvent;
import com.pay.chip.easypay.util.Constant;
import com.pay.chip.easypay.util.CustomToast;
import com.pay.chip.easypay.util.HttpProcessManager;
import com.pay.chip.easypay.util.PassEditText;
import com.pay.chip.easypay.util.PhoneEditText;
import com.pay.chip.easypay.util.UserUtils;
import com.pay.chip.easypay.util.VolleyManager;

import de.greenrobot.event.EventBus;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private TextView reg_head;
    private FrameLayout user_back;

    private PhoneEditText registerPhoneEdit;
    private PassEditText registerPassEdit;
    private Button registerBtn;

    String phone;
    String pass;

    private void initView() {

        user_back = (FrameLayout) findViewById(R.id.leftFL);
        user_back.setOnClickListener(this);
        reg_head = (TextView) findViewById(R.id.titleTV);
        reg_head.setText(getResources().getString(R.string.login));

        registerPhoneEdit = (PhoneEditText) findViewById(R.id.register_phone_edit);
        registerPassEdit = (PassEditText) findViewById(R.id.register_pass_edit);
        registerBtn = (Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void userRegister() {
        phone = registerPhoneEdit.getPhoneString();
        pass = registerPassEdit.getPassString();

        if(TextUtils.isEmpty(phone)||TextUtils.isEmpty(pass)){
            return;
        }

        if(!UserUtils.isMobileNUM(phone)){
            CustomToast.showToast(getString(R.string.not_phone));
            return;
        }

        StringRequest request = HttpProcessManager.getInstance().registerStudent(Constant.HOST_USER_REGISTER,phone,pass);
        VolleyManager.getInstance(getApplicationContext()).addToRequestQueue(request);



    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {


            case R.id.leftFL:
                finish();
                break;


            case R.id.register_btn:
                userRegister();
                break;




        }
    }


    public void onEventMainThread(UserRegisterEvent event) {


        int code = event.code;
        if (event.info == null) {
            return;
        }

        if (code == Constant.CODE_FAIL) {
            CustomToast.showToast(getString(R.string.net_fail));

            return;
        } else {
            if (code == Constant.CODE_SUCCESS) {
              /*  String data = StudentInfoData.DataBean.toJsonString(event.infoData);
                LoginDataHelper.getInstance().setLoginUserInfo(data);
                fillText(event.infoData);*/
            }
            CustomToast.showLongToast(event.info.toString());

        }

    }


}
