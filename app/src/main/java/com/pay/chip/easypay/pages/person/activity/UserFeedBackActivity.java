package com.pay.chip.easypay.pages.person.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pay.chip.easypay.R;
import com.pay.chip.easypay.util.CustomToast;

public class UserFeedBackActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText optionEdit;
    private TextView contact;
    private EditText contactText;
    private Button optionSendBtn;

    private TextView reg_head;
    private FrameLayout user_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_option);
        initView();
    }


    private void initView() {
        user_back = (FrameLayout) findViewById(R.id.leftFL);
        user_back.setOnClickListener(this);
        reg_head = (TextView) findViewById(R.id.titleTV);
        reg_head.setText(getResources().getString(R.string.options));

        optionEdit = (EditText) findViewById(R.id.option_edit);
        contact = (TextView) findViewById(R.id.contact);
        contactText = (EditText) findViewById(R.id.contact_text);
        optionSendBtn = (Button) findViewById(R.id.option_send_btn);
        optionSendBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {


            case R.id.leftFL:
                finish();
                break;

            case R.id.option_send_btn:
                CustomToast.showToast(R.string.thanks_for_send);
                finish();
                break;

        }
    }


}
