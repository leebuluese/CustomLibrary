package com.lyz.lib;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ListViewHolder extends RecyclerView.ViewHolder {

        final TextView tvName;

        public ListViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }