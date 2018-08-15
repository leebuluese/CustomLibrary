package com.lyz.lib.bitmap.longbitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lyz.lib.R;

import java.util.List;

/**
 * Created by 80002796 on 2018/8/15.
 * Created by lyz on 2018/8/15.
 */

public class LongBitmapAdapter extends RecyclerView.Adapter<LongBitmapAdapter.LongBitmapViewHolder>{

    private final LayoutInflater layoutInflater;
    private Context mContext;
    private List<Bitmap> mData;

    public LongBitmapAdapter(Context context) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Bitmap> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public LongBitmapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LongBitmapViewHolder(layoutInflater.inflate(R.layout.item_long_bitmap, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LongBitmapViewHolder holder, int position) {
        holder.ivLong.setImageBitmap(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }

    class LongBitmapViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivLong;

        public LongBitmapViewHolder(View itemView) {
            super(itemView);
            ivLong = itemView.findViewById(R.id.iv_photo);
        }
    }
}
