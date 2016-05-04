package com.pay.chip.easypay.pages.merchant.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erban.imageloader.AsyncImageView;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.merchant.activity.GoodDetailActivity;
import com.pay.chip.easypay.pages.merchant.event.UpdateMenuListEvent;
import com.pay.chip.easypay.pages.merchant.model.GoodModel;
import com.pay.chip.easypay.pages.merchant.model.ShoppingCartData;
import com.pay.chip.easypay.pages.merchant.view.ItemNumControl;
import com.pay.chip.easypay.util.LoginDataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by DavidLee on 2016/3/9.
 */
public class GoodAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GoodModel.DataEntity> dataList;
    private HashMap<String, GoodModel.DataEntity> selectHM;
    private String year;
    private String month;
    private String day;
    private String events = "";
    boolean isFirst = true;
    boolean isLast = true;

    private int totalMoney = 0;
    private int totalCount = 0;

    public GoodAdapter(Context context) {
        this.context = context;

    }

    private OrderMenuAdapterClickListener listener;

    public GoodAdapter(Context context, OrderMenuAdapterClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.dataList = new ArrayList<>();
        this.selectHM = new HashMap<>();
    }


    public void updateData(GoodModel.DataEntity itemData, int totalCount, int totalMoney) {
        if (itemData == null || TextUtils.isEmpty(itemData.getId())) {
            return;
        }
        this.totalCount = totalCount;
        this.totalMoney = totalMoney;

        for (int i = 0; i < dataList.size(); i++) {
            GoodModel.DataEntity allItem = dataList.get(i);
            if (itemData.getId().equalsIgnoreCase(allItem.getId())) {
                allItem.orderNum = itemData.orderNum;
                dataList.set(i, allItem);
            }
        }
        notifyDataSetChanged();
    }


    public void setData(ArrayList<GoodModel.DataEntity> data, int totalCount, int totalMoney) {
        if (data == null) {
            return;
        }
        dataList = new ArrayList<>();
        dataList.addAll(data);
        this.totalCount = totalCount;
        this.totalMoney = totalMoney;
        notifyDataSetChanged();
    }

    public void addToTail(ArrayList<GoodModel.DataEntity> data) {
        if (data == null) {
            return;
        }
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public GoodModel.DataEntity getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        final GoodModel.DataEntity data = dataList.get(position);
        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.good_item, parent, false);

        viewHolder.picAIV = (AsyncImageView) convertView.findViewById(R.id.picAIV);
            viewHolder.itemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            viewHolder.desTV = (TextView) convertView.findViewById(R.id.desTV);
            viewHolder.priceRL = (RelativeLayout) convertView.findViewById(R.id.priceRL);
            viewHolder.priceTV = (TextView) convertView.findViewById(R.id.priceTV);
            viewHolder.getTV = (TextView) convertView.findViewById(R.id.getTV);
            viewHolder.item_num_control = (ItemNumControl) convertView.findViewById(R.id.item_num_control);
            convertView.setTag(viewHolder);
//        viewHolder = (ViewHolder) convertView.getTag();


        if(null!=data.getPic()){
            viewHolder.picAIV.setImageURL(data.getPic(),false);
        }

        viewHolder.desTV.setText(data.getName());
        viewHolder.priceTV.setText(data.getPrice());

        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodDetailActivity.startGoodDetailActivity(context, data.getPic(), data.getName(), data.getPrice(), data.getDesc());
            }
        });
        final View v = viewHolder.item_num_control.findViewById(R.id.control_add_btn);
        viewHolder.item_num_control.setControlNum(data.orderNum);
        viewHolder.item_num_control.setItemNumControlButtonClicked(new ItemNumControl.OnItemNumControlButtonClickedListener() {
            @Override
            public void onItemNumControlButtonClicked(boolean isAdd, int num) {
                int price = 0;
                if (!TextUtils.isEmpty(data.getPrice())) {
                    price = Integer.parseInt(data.getPrice());

                }
                if (isAdd) {
                    totalCount++;
                    totalMoney += price;
                    if (listener != null) {
                        listener.onAddMenuBtnClicked(v);
                    }
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
                data.orderNum = num;
                if (isAdd || data.orderNum > 0) {
                    selectHM.put(data.getId(), data);
                } else {
                    selectHM.remove(data.getId());
                }
                updateSelectedData(data);
            }
        });

        return convertView;
    }

    public void setData(ArrayList<GoodModel.DataEntity> data) {
        if (data == null) {
            return;
        }
        dataList = new ArrayList<>();
        dataList.addAll(data);
        notifyDataSetChanged();
    }


    private static class ViewHolder {


        public AsyncImageView picAIV;
        public TextView desTV;
        public RelativeLayout priceRL;
        public TextView priceTV;
        public TextView getTV;
        public LinearLayout itemLayout;
        public ItemNumControl item_num_control;


    }

    public interface OrderMenuAdapterClickListener {
        void onAddMenuBtnClicked(View view);
    }

    private void updateSelectedData( GoodModel.DataEntity itemData) {
        ArrayList< GoodModel.DataEntity> selectDataList = new ArrayList<>();
        Iterator iter = selectHM.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            GoodModel.DataEntity item = ( GoodModel.DataEntity) entry.getValue();
            selectDataList.add(0, item);
        }
        ShoppingCartData shoppingCartData = new   ShoppingCartData(selectDataList, totalCount, totalMoney);
        LoginDataHelper.getInstance().setShoppingCartData(shoppingCartData);
        EventBus.getDefault().post(new UpdateMenuListEvent(itemData));
    }

}
