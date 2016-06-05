package com.pay.chip.easypay.pages.merchant.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pay.chip.easypay.R;


/**
 * Created by Administrator on 2015/12/11 0011.
 */
public class ItemNumControl extends RelativeLayout implements View.OnClickListener {

    private TextView control_text;
    private ImageView control_add_btn,control_subtract_btn;
    private Integer control_num = 0;
    private RelativeLayout control_layout;
    private OnItemNumControlButtonClickedListener listener;

    public ItemNumControl(Context context) {
        super(context);
        initData(context);
        initView();

    }

    public ItemNumControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
        initView();
    }

    public ItemNumControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
        initView();
    }

    private void initView() {
        control_add_btn = (ImageView) findViewById(R.id.control_add_btn);
        control_add_btn.setOnClickListener(this);
        control_subtract_btn = (ImageView) findViewById(R.id.control_subtract_btn);
        control_subtract_btn.setOnClickListener(this);
        control_text = (TextView) findViewById(R.id.control_num);
        control_text.setText(control_num.toString());
        control_layout = (RelativeLayout) findViewById(R.id.control_layout);
        judgeNum();
    }

    private void initData(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.num_control_layout, this, true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.control_add_btn:
                control_num = control_num + 1;
                control_text.setText(control_num.toString());
                judgeNum();
                if (listener != null) {
                    listener.onItemNumControlButtonClicked(true, getCount());
                }
                break;
            case R.id.control_subtract_btn:
                control_num = control_num - 1;
                control_text.setText(control_num.toString());
                judgeNum();
                if (listener != null) {
                    listener.onItemNumControlButtonClicked(false, getCount());
                }
                break;
        }
    }

    private void judgeNum() {
        if (control_num <= 0) {
            control_subtract_btn.setVisibility(GONE);
            control_text.setVisibility(GONE);
//            control_layout.setLayoutParams(new RelativeLayout.LayoutParams(DeviceUtils.dip2px(getContext(), 48), DeviceUtils.dip2px(getContext(), 48)));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//            control_add_btn.setPadding(0,DeviceUtils.dip2px(getContext(),4),0,DeviceUtils.dip2px(getContext(),4));
//            control_add_btn.setLayoutParams(layoutParams);
        } else {
            control_subtract_btn.setVisibility(VISIBLE);
            control_text.setVisibility(VISIBLE);
//            control_layout.setLayoutParams(new RelativeLayout.LayoutParams(DeviceUtils.dip2px(getContext(),75), DeviceUtils.dip2px(getContext(),48)));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//            control_add_btn.setPadding(0,DeviceUtils.dip2px(getContext(),4),0,DeviceUtils.dip2px(getContext(),4));
//            control_add_btn.setLayoutParams(layoutParams);
        }
    }

    public int getCount() {
        return control_num;
    }

    public TextView getControlText() {

        return control_text;
    }

    public void setControlNum(int num) {
        control_num = num;
        control_text.setText(num + "");
        judgeNum();
    }

    public void setItemNumControlButtonClicked(OnItemNumControlButtonClickedListener listener) {
        this.listener = listener;
    }

    public interface OnItemNumControlButtonClickedListener {
        void onItemNumControlButtonClicked(boolean isAdd, int num);
    }

    public void hideControl(){
        control_add_btn.setVisibility(GONE);
        control_subtract_btn.setVisibility(GONE);
    }
}
