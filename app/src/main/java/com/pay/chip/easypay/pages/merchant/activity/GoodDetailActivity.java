package com.pay.chip.easypay.pages.merchant.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erban.imageloader.AsyncImageView;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.util.Constant;

public class GoodDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private AsyncImageView goodPic;
    private TextView goodName;
    private TextView goodPrice;
    private TextView goodDesc;
    private LinearLayout detail_layout;

    private TextView reg_head;
    private FrameLayout user_back;


    private void initView() {

        user_back = (FrameLayout) findViewById(R.id.leftFL);
        user_back.setOnClickListener(this);
        reg_head = (TextView) findViewById(R.id.titleTV);
        reg_head.setText(getResources().getString(R.string.good_detail));

        goodPic = (AsyncImageView) findViewById(R.id.good_pic);
        detail_layout = (LinearLayout) findViewById(R.id.detail_layout);
        goodPic.setImageURL(pic, false);
        goodName = (TextView) findViewById(R.id.good_name);
        goodName.setText(name);
        goodPrice = (TextView) findViewById(R.id.good_price);
        goodPrice.setText(price);
        goodDesc = (TextView) findViewById(R.id.good_desc);
        goodDesc.setText(desc);


    }


    private String pic;
    private String name;
    private String price;
    private String desc;

    public static void startGoodDetailActivity(Context context,String pic,String name,String price,String desc) {
        Intent intent = new Intent(context, GoodDetailActivity.class);
        Bundle b = new Bundle();
        b.putString(Constant.PIC, pic);
        b.putString(Constant.NAME, name);
        b.putString(Constant.PRICE, price);
        b.putString(Constant.DESC, desc);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_detail);
        perseIntent();
        initView();
    }


    private void perseIntent() {
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            return;
        }

        Bundle b = intent.getExtras();
        pic = b.getString(Constant.PIC);
        name = b.getString(Constant.NAME);
        price = b.getString(Constant.PRICE);
        desc = b.getString(Constant.DESC);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Bitmap bitmap = null;
        try {
            if (null == goodPic || null == goodPic.getDrawable() || null == ((BitmapDrawable) goodPic.getDrawable()).getBitmap()) {
                return;
            }
            bitmap = ((BitmapDrawable) goodPic.getDrawable()).getBitmap();
        }catch (Exception e){

        }
        if(null==bitmap){
            return;
        }

        Palette palette = Palette.generate(bitmap);
            detail_layout.setBackgroundColor(palette.getLightVibrantColor(getResources().getColor(R.color.light)));

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {


            case R.id.leftFL:
                finish();
                break;


        }
    }
}
