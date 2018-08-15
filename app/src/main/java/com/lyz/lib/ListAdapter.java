package com.lyz.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by lyz on 2018/8/14.
 *
 */

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private final ItemClickListener itemClickListener;
    private Context mContext;
    private List<String> mData;
    private LayoutInflater layoutInflater;

    public ListAdapter(Context context) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
        itemClickListener = new ItemClickListener();
    }

    public void setData(List<String> list) {
        this.mData = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(layoutInflater.inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        String content = mData.get(position);
        holder.tvName.setText(content);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(itemClickListener);
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }

    class ItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            String s = mData.get(position);
            new EventPath(mContext, position, s).startEvent();
        }
    }

}
