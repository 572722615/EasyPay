package com.pay.chip.easypay.pages.discount.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.discount.model.DiscountModel;
import com.pay.chip.easypay.util.AsyncCircleImageView;
import com.pay.chip.easypay.util.LoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DiscountItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int IO_BUFFER_SIZE = 1024;
    private List<DiscountModel.DummyItem> mValues = new ArrayList<DiscountModel.DummyItem>();
    private boolean mIsStagger;
    Bitmap bitmap;
    public Context context;

    public DiscountItemRecyclerViewAdapter(List<DiscountModel.DummyItem> items) {
        mValues = items;
    }

    public DiscountItemRecyclerViewAdapter() {

    }

    public DiscountItemRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void switchMode(boolean mIsStagger) {
        this.mIsStagger = mIsStagger;
    }

    public void setData(List<DiscountModel.DummyItem> datas) {
        mValues = datas;
    }

    public void addDatas(List<DiscountModel.DummyItem> datas) {
        mValues.addAll(datas);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LoadMoreRecyclerView.TYPE_STAGGER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_item_staggel, parent, false);
            return new StaggerViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (mIsStagger) {
        final StaggerViewHolder staggerViewHolder = (StaggerViewHolder) holder;
        staggerViewHolder.iconView.setVisibility(View.VISIBLE);
        staggerViewHolder.mContentView.setText(mValues.get(position).desc);
        if (mValues.get(position).pic != null) {
            staggerViewHolder.iconView.setImageURL(mValues.get(position).pic, false);
//                bitmap = BitmapFactory.decodeResource(context.getResources(), staggerViewHolder.iconView.getId());
            staggerViewHolder.iconView.setDrawingCacheEnabled(true);
            bitmap = staggerViewHolder.iconView.getImageBitmap();


            //((BitmapDrawable) staggerViewHolder.iconView.getDrawable()).getBitmap();

        }
       /* if(position%2==0){
            staggerViewHolder.iconView.setImageResource(R.drawable.about_logo);
        }else{
            staggerViewHolder.iconView.setImageResource(R.drawable.ic_launcher);
        }*/
        staggerViewHolder.card_layout.setRadius(10);

        //staggerViewHolder.iconView.getImageRes
        //GetLocalOrNetBitmap(mValues.get(position).pic);
       /* staggerViewHolder.iconView.setDrawingCacheEnabled(true);*/


        if (bitmap != null) {
            Palette palette = Palette.generate(bitmap);
            staggerViewHolder.card_layout.setCardBackgroundColor(palette.getLightVibrantColor(context.getResources().getColor(R.color.light)));

        }

       /* } else {
            ViewHolder mHolder = (ViewHolder) holder;
            mHolder.mItem = mValues.get(position);
            mHolder.mContentView.setText(mValues.get(position).content);
            mHolder.mIdView.setText(mValues.get(position).id);
        }*/
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class StaggerViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public AsyncCircleImageView iconView;
        public TextView mContentView;
        public CardView card_layout;

        public StaggerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            iconView = (AsyncCircleImageView) itemView.findViewById(R.id.icon);
            mContentView = (TextView) itemView.findViewById(R.id.content);
            card_layout = (CardView) itemView.findViewById(R.id.card_layout);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DiscountModel.DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


}
