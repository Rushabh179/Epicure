package com.project.rushabh.restaurant.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.rushabh.restaurant.R;

import java.util.List;

/**
 * Created by rushabh.modi on 09/04/18.
 */

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {

    List<String> senderEmailList;

    public OrderRecyclerAdapter(List<String> senderEmailList) {
        this.senderEmailList = senderEmailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderEmailText.setText(senderEmailList.get(position));
    }

    @Override
    public int getItemCount() {
        return senderEmailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderEmailText, orderDateTimeText, orderCountText, orderStatusText;

        public ViewHolder(View itemView) {
            super(itemView);
            orderEmailText = itemView.findViewById(R.id.text_order_email);
            orderDateTimeText = itemView.findViewById(R.id.text_order_date_time);
            orderCountText = itemView.findViewById(R.id.text_order_itemcount);
            orderStatusText = itemView.findViewById(R.id.text_order_status);
        }
    }
}
