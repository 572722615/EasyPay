package com.pay.chip.easypay.pages.merchant.adapter;

import android.content.Context;
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
import com.pay.chip.easypay.pages.merchant.model.GoodModel;

import java.util.ArrayList;

/**
 * Created by DavidLee on 2016/3/9.
 */
public class GoodAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GoodModel.DataEntity> dataList = new ArrayList<>();
    private String year;
    private String month;
    private String day;
    private String events = "";
    boolean isFirst = true;
    boolean isLast = true;

    public GoodAdapter(Context context) {
        this.context = context;

    }





    public void setData(ArrayList<GoodModel.DataEntity> data) {
        if (data == null) {
            return;
        }
        dataList = new ArrayList<>();
        dataList.addAll(data);
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
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.good_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.picAIV = (AsyncImageView) convertView.findViewById(R.id.picAIV);
            viewHolder.itemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            viewHolder.desTV = (TextView) convertView.findViewById(R.id.desTV);
            viewHolder.priceRL = (RelativeLayout) convertView.findViewById(R.id.priceRL);
            viewHolder.priceTV = (TextView) convertView.findViewById(R.id.priceTV);
            viewHolder.getTV = (TextView) convertView.findViewById(R.id.getTV);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        final GoodModel.DataEntity data = dataList.get(position);


        if(null!=data.getPic()){
            viewHolder.picAIV.setImageURL(data.getPic(),false);
        }

        viewHolder.desTV.setText(data.getName());
        viewHolder.priceTV.setText(data.getPrice());

        viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodDetailActivity.startGoodDetailActivity(context,data.getPic(),data.getName(),data.getPrice(),data.getDesc());
            }
        });

        return convertView;
    }



    private static class ViewHolder {


        public AsyncImageView picAIV;
        public TextView desTV;
        public RelativeLayout priceRL;
        public TextView priceTV;
        public TextView getTV;
        public LinearLayout itemLayout;


    }

}
