package com.pay.chip.easypay.pages.person.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.util.Constant;
import com.pay.chip.easypay.util.HttpProcessManager;
import com.pay.chip.easypay.util.LoginDataHelper;
import com.pay.chip.easypay.util.VolleyManager;


/**
 * Created by Administrator on 2015/9/29.
 */
public class NamePopWindow extends PopupWindow {
    private TextView btn_cancel, btn_sure;
    private EditText user_changeName;

    private View mMenuView;
    String name;
    String id;


    public NamePopWindow(final Activity context, OnClickListener itemsOnClick) {


        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(
                R.layout.activity_user_info_layout_name_popwindow, null);
        user_changeName = (EditText) mMenuView.findViewById(R.id.user_changeName);
        btn_sure = (TextView) mMenuView.findViewById(R.id.user_change_sure);
        btn_sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                name = user_changeName.getText().toString().trim();
                id = LoginDataHelper.getInstance().getLoginUserInfo().id;
                if (null == name&&id==null) {
                    return;
                }

                StringRequest request = HttpProcessManager.getInstance().changeName(Constant.HOST_CHANGE_USER_NAME, id,name);
                VolleyManager.getInstance(context.getApplicationContext()).addToRequestQueue(request);
                dismiss();
            }
        });
        btn_cancel = (TextView) mMenuView.findViewById(R.id.user_change_cancel);
        // 取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.user_head_pop)
                        .getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}
