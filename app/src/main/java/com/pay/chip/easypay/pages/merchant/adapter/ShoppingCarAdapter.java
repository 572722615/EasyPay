package com.pay.chip.easypay.pages.merchant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.merchant.event.UpdateMenuListEvent;
import com.pay.chip.easypay.pages.merchant.model.GoodModel;
import com.pay.chip.easypay.pages.merchant.model.ShoppingCartData;
import com.pay.chip.easypay.pages.merchant.view.ItemNumControl;
import com.pay.chip.easypay.util.LoginDataHelper;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/11/30 0030.
 */

public class ShoppingCarAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<GoodModel.DataEntity> dataList;
    private int totalMoney = 0;
    private int totalCount = 0;

    public ShoppingCarAdapter(Context context) {
        this.context = context;
        dataList = new ArrayList<>();
    }

    public void setData(ArrayList<GoodModel.DataEntity> data, int totalCount, int totalMoney) {
        dataList = new ArrayList<>();
        dataList.addAll(data);
        this.totalCount = totalCount;
        this.totalMoney = totalMoney;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GoodModel.DataEntity itemData = (GoodModel.DataEntity) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.shopping_item, null);
            viewHolder.menuNameTV = (TextView) convertView.findViewById(R.id.menuNameTV);
            viewHolder.priceTV = (TextView) convertView.findViewById(R.id.priceTV);
            viewHolder.item_num_control = (ItemNumControl) convertView.findViewById(R.id.item_num_control);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.menuNameTV.setText(getMenuName(itemData));
        viewHolder.priceTV.setText("¥" + itemData.getPrice());
        viewHolder.item_num_control.setControlNum(itemData.orderNum);
        viewHolder.item_num_control.setItemNumControlButtonClicked(new ItemNumControl.OnItemNumControlButtonClickedListener() {
            @Override
            public void onItemNumControlButtonClicked(boolean isAdd, int num) {
                int price = 0;
                if (!TextUtils.isEmpty(itemData.getPrice())) {
                    price = Integer.parseInt(itemData.getPrice());

                }
                if (isAdd) {
                    totalCount++;
                    totalMoney += price;
                } else {
                    totalCount--;
                    if (totalCount < 0) {
                        totalCount = 0;
                    }
                    totalMoney -= price;
                    if (totalMoney < 0) {
                        totalMoney = 0;
                    }
                }
                itemData.orderNum = num;
                if (num < 1) {
                    dataList.remove(position);
                    notifyDataSetChanged();
                }
                updateSelectedData(itemData);
            }
        });
        return convertView;
    }

    private void updateSelectedData(GoodModel.DataEntity itemData) {
        ShoppingCartData shoppingCartData = new ShoppingCartData(dataList, totalCount, totalMoney);
        LoginDataHelper.getInstance().setShoppingCartData(shoppingCartData);
        EventBus.getDefault().post(new UpdateMenuListEvent(itemData));
    }

    private String getMenuName(GoodModel.DataEntity itemData) {
        if (itemData == null || TextUtils.isEmpty(itemData.getName())) {
            return "";
        }
        String menuName = itemData.getName();
        String productNum = itemData.orderNum+"";
        if (!TextUtils.isEmpty(productNum)) {
            menuName += "(" + productNum + ")";
        }
        return menuName;
    }

    private static class ViewHolder {
        TextView menuNameTV;
        TextView priceTV;
        ItemNumControl item_num_control;
    }
}
