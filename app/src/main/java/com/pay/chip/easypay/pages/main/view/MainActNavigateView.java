package com.pay.chip.easypay.pages.main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pay.chip.easypay.R;


public final class MainActNavigateView extends FrameLayout implements View.OnClickListener {


    public static final class Component {
        public static final byte TAB_MERCHANT = 1;
        public static final byte TAB_DISCOUNT = 2;
        public static final byte TAB_PERSON = 3;
    }

    public static interface ICallback {
        void onClicked(byte component);
    }

    private ImageView wifiIV;
    private ImageView wifiRedDotIV;
    private ImageView discountIV;
    private ImageView discountRedDotIV;
    private ImageView merchantIV;
    private ImageView merchantRedDotIV;
    private ImageView personalIV;
    private ImageView personalRedDotIV;


    private ICallback mCallback;

    public MainActNavigateView(Context context) {
        this(context, null);
    }

    public MainActNavigateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainActNavigateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.main_act_navigate_layout, this);

        wifiIV = (ImageView) findViewById(R.id.wifiIV);
        discountIV = (ImageView) findViewById(R.id.discountIV);
        merchantIV = (ImageView) findViewById(R.id.merchantIV);
        personalIV = (ImageView) findViewById(R.id.personalIV);
        wifiRedDotIV = (ImageView) findViewById(R.id.wifiRedDotIV);
        discountRedDotIV = (ImageView) findViewById(R.id.discountRedDotIV);
        merchantRedDotIV = (ImageView) findViewById(R.id.merchantRedDotIV);
        personalRedDotIV = (ImageView) findViewById(R.id.personalRedDotIV);

        findViewById(R.id.wifiFrame).setOnClickListener(this);
        findViewById(R.id.discountFrame).setOnClickListener(this);
        findViewById(R.id.merchantFrame).setOnClickListener(this);
        findViewById(R.id.personalFrame).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mCallback == null) {
            return;
        }

        switch (v.getId()) {

            case R.id.discountFrame: {
                mCallback.onClicked(Component.TAB_DISCOUNT);
                break;
            }
            case R.id.merchantFrame: {
                mCallback.onClicked(Component.TAB_MERCHANT);
                break;
            }
            case R.id.personalFrame: {
                mCallback.onClicked(Component.TAB_PERSON);
                break;
            }
        }
    }

    public void setCallback(ICallback callback) {
        mCallback = callback;
    }

    public void setRedDotVisible(byte component, boolean visible) {
        switch (component) {

            case MainActNavigateView.Component.TAB_DISCOUNT: {
                if (visible) {
                    discountRedDotIV.setVisibility(VISIBLE);
                } else {
                    discountRedDotIV.setVisibility(INVISIBLE);
                }
                break;
            }
            case MainActNavigateView.Component.TAB_MERCHANT: {
                if (visible) {
                    merchantRedDotIV.setVisibility(VISIBLE);
                } else {
                    merchantRedDotIV.setVisibility(INVISIBLE);
                }
                break;
            }
            case MainActNavigateView.Component.TAB_PERSON: {
                if (visible) {
                    personalRedDotIV.setVisibility(VISIBLE);
                } else {
                    personalRedDotIV.setVisibility(INVISIBLE);
                }
                break;
            }
        }
    }

    public void setSelected(byte component) {
        switch (component) {

            case MainActNavigateView.Component.TAB_DISCOUNT: {
                wifiIV.setImageResource(R.drawable.main_icon_wifi);
                discountIV.setImageResource(R.drawable.main_icon_discount_selected);
                merchantIV.setImageResource(R.drawable.main_icon_merchant);
                personalIV.setImageResource(R.drawable.main_icon_personal);
                break;
            }
            case MainActNavigateView.Component.TAB_MERCHANT: {
                wifiIV.setImageResource(R.drawable.main_icon_wifi);
                discountIV.setImageResource(R.drawable.main_icon_discount);
                merchantIV.setImageResource(R.drawable.main_icon_merchant_selected);
                personalIV.setImageResource(R.drawable.main_icon_personal);
                break;
            }
            case MainActNavigateView.Component.TAB_PERSON: {
                wifiIV.setImageResource(R.drawable.main_icon_wifi);
                discountIV.setImageResource(R.drawable.main_icon_discount);
                merchantIV.setImageResource(R.drawable.main_icon_merchant);
                personalIV.setImageResource(R.drawable.main_icon_personal_selected);
                break;
            }
        }
    }
}
