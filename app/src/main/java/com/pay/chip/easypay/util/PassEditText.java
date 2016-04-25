package com.pay.chip.easypay.util;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pay.chip.easypay.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * Created by Administrator on 2015/10/7.
 */
public class PassEditText extends RelativeLayout implements View.OnClickListener{
    private Context mContext;
    private EditText passEdit;
    private ImageView passEye;
    private Boolean eyeType = false;
    public PassEditText(Context context) {
        super(context);
        initData(context);
    }

    public String getPassString(){
        return passEdit.getText().toString().trim();
    }

    public PassEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    private void initData(Context context) {
        mContext = context;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.pass_edittext_layout,this,true);

        passEdit = (EditText) findViewById(R.id.passEdit);
        passEye = (ImageView) findViewById(R.id.passEye);
//        passEdit.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.register_pass_digits)));
        passEye.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.passEye:
                if(eyeType == false){
                    eyeType = true;
//                    passEdit.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.register_pass_digits)));
                    passEdit.addTextChangedListener(new SearchWather(passEdit));
                    passEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passEye.setImageResource(R.drawable.eye);
                    passEdit.setSelection(passEdit.getText().length());

                }else if(eyeType==true){
                    eyeType = false;
//                    passEdit.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.register_pass_digits)));
                    passEdit.addTextChangedListener(new SearchWather(passEdit));
                    passEye.setImageResource(R.drawable.eye_closed);
                    passEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passEdit.setSelection(passEdit.getText().length());

                }

                break;
        }
    }


    public EditText getPassEdit(){
        return passEdit;
    }

    public void eyeOpen(){
        eyeType = true;
//        passEdit.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.register_pass_digits)));
        passEdit.addTextChangedListener(new SearchWather(passEdit));
        passEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        passEye.setImageResource(R.drawable.eye);
        passEdit.setSelection(passEdit.getText().length());
    }

    public void eyeClose(){
        eyeType = false;
//        passEdit.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.register_pass_digits)));
        passEdit.addTextChangedListener(new SearchWather(passEdit));
        passEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passEye.setImageResource(R.drawable.eye_closed);
        passEdit.setSelection(passEdit.getText().length());
    }

    class SearchWather implements TextWatcher {


        //监听改变的文本框
        private EditText editText;


        public SearchWather(EditText editText){
            this.editText = editText;
        }

        @Override
        public void onTextChanged(CharSequence ss, int start, int before, int count) {
            String editable = editText.getText().toString();
            String str = stringFilter(editable.toString());
            if(!editable.equals(str)){
                editText.setText(str);
                //设置新的光标所在位置
                editText.setSelection(str.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {

        }

    }


    public static String stringFilter(String str)throws PatternSyntaxException {
        // 只允许字母和数字
        String   regEx  =  "[^a-zA-Z0-9]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return   str;
    }
}
