package com.lyz.lib.photo;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lyz.lib.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lyz.lib.util.GlideUtil;

/**
 *
 * @author liyongzhen
 * @date 2018/4/27
 * 添加照片
 */

public class AddPictureAdapter extends RecyclerView.Adapter<AddPictureAdapter.AddPictureViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final DeleteItemListener deleteItemListener;
    private final CheckPictureListener checkPictureListener;
    private Activity mContext;
    private List<String> mBitmaps;
    private boolean isDelete = true;
    private OnPicRemoveListener mListener;
    private final int REQUEST_DELETE = 1012;
    private int mPicCount = 3;

    public AddPictureAdapter(Activity context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        deleteItemListener = new DeleteItemListener();
        checkPictureListener = new CheckPictureListener();
    }

    public void setData(List<String> bitmapList) {
        this.mBitmaps = bitmapList;
    }

    public List<String> getData() {
        return mBitmaps;
    }

    public void setPicCount(int count) {
        this.mPicCount = count;
    }

    public int getPicCount() {
        return this.mPicCount;
    }

    public void setIsDeletePicture(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public void addData(String bitmap) {
        if (null == mBitmaps) {
            mBitmaps = new ArrayList<>();
        }
        // 限制最多添加三张
        if (mBitmaps.size() < mPicCount) {
            mBitmaps.add(bitmap);
        }
    }

    public List<String> getBitmaps() {
        return mBitmaps;
    }

    @NonNull
    @Override
    public AddPictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflateView = mLayoutInflater.inflate(R.layout.item_add_picture, parent, false);
        return new AddPictureViewHolder(inflateView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddPictureViewHolder holder, final int position) {
        String bitmap = mBitmaps.get(position);
        GlideUtil.loadImg(mContext, holder.ivPicture, bitmap);

        if (isDelete) {
            holder.ivDelete.setTag(position);
            holder.ivDelete.setOnClickListener(deleteItemListener);
        } else {
            holder.ivDelete.setVisibility(View.GONE);
        }

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(checkPictureListener);
    }

    class DeleteItemListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            mBitmaps.remove(position);
            notifyDataSetChanged();
            if (null != mListener) {
                mListener.onRemove(mBitmaps);
            }
        }
    }

    class CheckPictureListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            Intent intent = new Intent(mContext, BrowsePhotoActivity.class);
            intent.putExtra(BrowsePhotoActivity.KEY_LIST, (Serializable) mBitmaps);
            intent.putExtra(BrowsePhotoActivity.KEY_DELETE, isDelete);
            intent.putExtra(BrowsePhotoActivity.KEY_INDEX, position);
            if (isDelete) {
                mContext.startActivityForResult(intent, REQUEST_DELETE);
            } else {
                mContext.startActivity(intent);
            }
        }
    }

    @Override
    public int getItemCount() {
        return null == mBitmaps ? 0 : mBitmaps.size();
    }

    class AddPictureViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivPicture;
        private ImageView ivDelete;

        AddPictureViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            ivPicture = itemView.findViewById(R.id.iv_picture);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }

    public void setOnPicRemoveListener(OnPicRemoveListener listener) {
        this.mListener = listener;
    }

    public interface OnPicRemoveListener {
        /**
         * 移除
         * @param list
         */
        void onRemove(List<String> list);
    }
}
