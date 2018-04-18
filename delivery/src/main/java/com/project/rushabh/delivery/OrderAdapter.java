package com.project.rushabh.delivery;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rushabh on 11-Apr-18.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<String> placeNameList, senderEmailList, timeStampList, itemCountList, statusList;

    public OrderAdapter(List<String> placeNameList, List<String> senderEmailList, List<String> timeStampList, List<String> itemCountList, List<String> statusList) {
        this.placeNameList = placeNameList;
        this.senderEmailList = senderEmailList;
        this.timeStampList = timeStampList;
        this.itemCountList = itemCountList;
        this.statusList = statusList;
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
        holder.orderPlaceNameText.setText(placeNameList.get(position));
        holder.orderEmailText.setText(senderEmailList.get(position));
        holder.orderDateTimeText.setText(timeStampList.get(position));
        holder.orderCountText.setText(itemCountList.get(position));
        holder.orderStatusText.setText(statusList.get(position));
    }

    @Override
    public int getItemCount() {
        return placeNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderPlaceNameText, orderEmailText, orderDateTimeText, orderCountText, orderStatusText;

        public ViewHolder(View itemView) {
            super(itemView);
            orderPlaceNameText = itemView.findViewById(R.id.text_order_restaurant);
            orderEmailText = itemView.findViewById(R.id.text_order_email);
            orderDateTimeText = itemView.findViewById(R.id.text_order_date_time);
            orderCountText = itemView.findViewById(R.id.text_order_itemcount);
            orderStatusText = itemView.findViewById(R.id.text_order_status);
        }
    }
}
