package com.pay.chip.easypay.pages.person.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.pay.chip.easypay.R;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView reg_head;
    private FrameLayout user_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        user_back = (FrameLayout) findViewById(R.id.leftFL);
        user_back.setOnClickListener(this);
        reg_head = (TextView) findViewById(R.id.titleTV);
        reg_head.setText(getResources().getString(R.string.news_title));
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