package com.pay.chip.easypay.pages.discount.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pay.chip.easypay.R;
import com.pay.chip.easypay.pages.discount.model.DummyContent;
import com.pay.chip.easypay.util.LoadMoreRecyclerView;

import java.util.List;

public class DiscountItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DummyContent.DummyItem> mValues;
    private boolean mIsStagger;

    public DiscountItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
        mValues = items;
    }

    public void switchMode(boolean mIsStagger) {
        this.mIsStagger = mIsStagger;
    }

    public void setData(List<DummyContent.DummyItem> datas) {
        mValues = datas;
    }

    public void addDatas(List<DummyContent.DummyItem> datas) {
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
            staggerViewHolder.mContentView.setText(mValues.get(position).details);
        if(position%2==0){
            staggerViewHolder.iconView.setImageResource(R.drawable.about_logo);
        }else{
            staggerViewHolder.iconView.setImageResource(R.drawable.ic_launcher);
        }
        Bitmap bitmap = ((BitmapDrawable)  staggerViewHolder.iconView.getDrawable()).getBitmap();
        if(bitmap!=null){
            Palette.generateAsync(bitmap,
                    new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch vibrant =
                                    palette.getVibrantSwatch();
                            // If we have a vibrant color
                            // update the title TextView
                            staggerViewHolder.card_layout.setCardBackgroundColor(
                                    vibrant.getRgb());
                            staggerViewHolder.mContentView.setTextColor(
                                    vibrant.getTitleTextColor());
                        }
                    });
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
        public ImageView iconView;
        public TextView mContentView;
        public CardView card_layout;

        public StaggerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            iconView = (ImageView) itemView.findViewById(R.id.icon);
            mContentView = (TextView) itemView.findViewById(R.id.content);
            card_layout = (CardView) itemView.findViewById(R.id.card_layout);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyContent.DummyItem mItem;

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
