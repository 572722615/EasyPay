package com.pay.chip.easypay.pages.merchant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.merchant.adapter.ShoppingCarAdapter;
import com.pay.chip.easypay.pages.merchant.event.OrderEvent;
import com.pay.chip.easypay.pages.merchant.model.GoodModel;
import com.pay.chip.easypay.pages.merchant.model.OrderDetailArray;
import com.pay.chip.easypay.pages.merchant.model.ShoppingCartData;
import com.pay.chip.easypay.util.Constant;
import com.pay.chip.easypay.util.CustomToast;
import com.pay.chip.easypay.util.HttpProcessManager;
import com.pay.chip.easypay.util.ListViewForScrollView;
import com.pay.chip.easypay.util.LoginDataHelper;
import com.pay.chip.easypay.util.ViewUtil;
import com.pay.chip.easypay.util.VolleyManager;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by DavidLee on 2015/12/15.
 */

public class SubmitOrderActivity extends Activity implements View.OnClickListener {
    private FrameLayout leftFL;
    private TextView titleTV, totalPriceTV;
    private ListViewForScrollView selectedOrderLV;
    private ShoppingCarAdapter selectedAdapter;
    private EditText deskET, countET, remarkET;
    private TextView remarkTV;
    private Button okBtn;
    private String userId,merchantId, seatNum, totalPrice, remark, peopleNum;
    private OrderDetailArray orderDetailArray;
    private String info = "";

    public static void startSubmitOrderActivity(Context context, String merchantId) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SubmitOrderActivity.class);
        Bundle b = new Bundle();
        b.putString(Constant.KEY_MERCHANT_ID, merchantId);
        intent.putExtras(b);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        parserIntent();
        setContentView(R.layout.submit_order_activity);
        initView();
    }

    private void parserIntent() {
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            return;
        }
        Bundle b = intent.getExtras();
        merchantId = b.getString(Constant.KEY_MERCHANT_ID);
    }

    private void initView() {
        deskET = (EditText) findViewById(R.id.deskET);
        okBtn = (Button) findViewById(R.id.okBtn);
        countET = (EditText) findViewById(R.id.countET);
        remarkET = (EditText) findViewById(R.id.remarkET);
        okBtn.setOnClickListener(this);
        selectedOrderLV = (ListViewForScrollView) findViewById(R.id.selectedOrderLV);
        leftFL = (FrameLayout) findViewById(R.id.leftFL);
        totalPriceTV = (TextView) findViewById(R.id.totalPriceTV);
        titleTV = (TextView) findViewById(R.id.titleTV);
        titleTV.setText(getString(R.string.title_submit_order));
        leftFL.setOnClickListener(this);

        selectedAdapter = new ShoppingCarAdapter(this);
        selectedOrderLV.setAdapter(selectedAdapter);

        ShoppingCartData shoppingCartData = LoginDataHelper.getInstance().getShoppingCartData();
        if (shoppingCartData == null || shoppingCartData.data == null || shoppingCartData.data.isEmpty()) {
            return;
        }
        totalPriceTV.setText("合计：¥" + shoppingCartData.totalMoney);
//        totalPriceTV.setText(shoppingCartData.totalMoney + "");
        selectedAdapter.setData(shoppingCartData.data, shoppingCartData.totalCount, shoppingCartData.totalMoney);
        ViewUtil.setListViewHeightBasedOnChildren(selectedOrderLV);
        selectedAdapter.hideControl();
        userId = LoginDataHelper.getInstance().getLoginUserInfo().id;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private boolean checkInput() {
        seatNum = deskET.getText().toString().trim();
        peopleNum = countET.getText().toString().trim();
        remark = remarkET.getText().toString().trim();

        ShoppingCartData shoppingCartData = LoginDataHelper.getInstance().getShoppingCartData();
        if (shoppingCartData == null || shoppingCartData.data == null || shoppingCartData.data.isEmpty()) {
            CustomToast.showToast(getString(R.string.title_order_empty_tip));
            return false;
        }
        totalPrice = shoppingCartData.totalMoney + "";
        orderDetailArray = new OrderDetailArray();
        ArrayList<OrderDetailArray.MenuItemData> orderDetail = new ArrayList<>();
        for (GoodModel.DataEntity item : shoppingCartData.data) {
            OrderDetailArray.MenuItemData data = new OrderDetailArray.MenuItemData();
            data.id = item.getId();
            data.num = item.orderNum + "";
            orderDetail.add(data);
            info +=item.getName()+"("+item.orderNum+")";

        }
        orderDetailArray.orderDetail = orderDetail;

        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.leftFL:
                finish();
                break;
          /*  case R.id.remarkRL:
                RemarkActivity.startRemarkActivity(SubmitOrderActivity.this, remarkTV.getText().toString());
                break;*/
            case R.id.okBtn:
                if (checkInput()) {

                    StringRequest request = HttpProcessManager.getInstance().doOrder(Constant.HOST_ADD_ORDER,userId, merchantId, seatNum,info, totalPrice, remark, peopleNum);
                    VolleyManager.getInstance(getApplicationContext()).addToRequestQueue(request);
                }
                break;
        }
    }

    public void onEventMainThread(OrderEvent event) {
        if (event == null) {
            return;
        }
        if(event.code!=Constant.CODE_FAIL){
            finish();
        }
        CustomToast.showToast(event.msg);
    }

  /*

  public void onEventMainThread(UpdateMenuListEvent event) {
        ShoppingCartData shoppingCartData = LoginDataHelper.getInstance().getShoppingCartData();
        if (shoppingCartData == null) {
            return;
        }
        OrderMenuItem itemData = event.itemData;
        if (itemData == null) {
            return;
        }
        totalPriceTV.setText(RmbUtil.getPriceText(this,shoppingCartData.totalMoney+""));
        ViewUtil.setListViewHeightBasedOnChildren(selectedOrderLV);
    }

    public void onEventMainThread(DoOrderEvent event) {
        hideLoadingDialog();
        if (EBErrorCode.EB_CODE_SUCCESS != event.code || event.resp == null ||
                event.resp.data == null || TextUtils.isEmpty(event.resp.data.orderId)) {
            String tip = LoginDataHelper.getInstance().doRespEvent(event.code, event.msg);
            CustomToast.showToast(tip);
            return;
        }
        CustomToast.showToast("下单成功");
        Bundle b = new Bundle();
        b.putBoolean(EBConstant.KEY_ORDER_DETAIL_SHOW_TOP_VIEW, true);
        b.putString(EBConstant.KEY_ORDER_DETAIL_ID, event.resp.data.orderId);
        LoginUtil.startActivityByFlag(SubmitOrderActivity.this, EBConstant.UserFlag.ORDER_DETAIL.toString(), b);
        finish();
    }*/
}
