package com.pay.chip.easypay.util;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pay.chip.easypay.R;


/**
 * Created by Administrator on 2015/10/7.
 */
public class PhoneEditText extends RelativeLayout implements View.OnClickListener{
    private Context mContext;
    private EditText phoneEdit;
    private ImageView clearInput;
    public PhoneEditText(Context context) {
        super(context);
        initData(context);
    }

    public String getPhoneString(){
       return phoneEdit.getText().toString().trim();
    }

    public PhoneEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    private void initData(Context context) {
        mContext = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.phone_edittext_layout,this,true);

        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        clearInput = (ImageView) findViewById(R.id.clearInput);
        phoneEdit.addTextChangedListener(new MyTextWatcher());
        clearInput.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.clearInput:
                phoneEdit.setText("");
                break;
        }
    }

    private class MyTextWatcher implements TextWatcher {
        int length ;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(s)) {
                length = s.length();
                if(length>0){
                    clearInput.setVisibility(VISIBLE);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            length = s.length();
            if(length==0){
                clearInput.setVisibility(GONE);
            }
        }
    }

    public EditText getPhoneEdit(){
        return phoneEdit;
    }

    public void changeTypeText(){
        phoneEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        phoneEdit.setInputType(InputType.TYPE_CLASS_TEXT);

    }

    public void changeHintName(){
        phoneEdit.setHint(getResources().getString(R.string.name));
    }



}
