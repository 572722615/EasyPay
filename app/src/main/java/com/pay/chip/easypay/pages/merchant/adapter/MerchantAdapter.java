package com.pay.chip.easypay.pages.merchant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erban.imageloader.AsyncImageView;
import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.merchant.model.MerchantModel;

import java.util.ArrayList;

/**
 * Created by DavidLee on 2016/3/9.
 */
public class MerchantAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MerchantModel.DataEntity> dataList = new ArrayList<>();
    private String year;
    private String month;
    private String day;
    private String events = "";
    boolean isFirst = true;
    boolean isLast = true;

    public MerchantAdapter(Context context) {
        this.context = context;

    }





    public void setData(ArrayList<MerchantModel.DataEntity> data) {
        if (data == null) {
            return;
        }
        dataList = new ArrayList<>();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public void addToTail(ArrayList<MerchantModel.DataEntity> data) {
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
    public MerchantModel.DataEntity getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.merchant_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.picAIV = (AsyncImageView) convertView.findViewById(R.id.picAIV);
            viewHolder.desTV = (TextView) convertView.findViewById(R.id.desTV);
            viewHolder.priceRL = (RelativeLayout) convertView.findViewById(R.id.priceRL);
            viewHolder.addressTV = (TextView) convertView.findViewById(R.id.addressTV);
            viewHolder.getTV = (TextView) convertView.findViewById(R.id.getTV);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        final MerchantModel.DataEntity data = dataList.get(position);



        viewHolder.picAIV.setImageURL(data.getHead(),false);
        viewHolder.desTV.setText(data.getName());
        viewHolder.addressTV.setText(data.getLat());


        return convertView;
    }



    private static class ViewHolder {


        public AsyncImageView picAIV;
        public TextView desTV;
        public RelativeLayout priceRL;
        public TextView addressTV;
        public TextView getTV;



    }

}
