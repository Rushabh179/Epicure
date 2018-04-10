package com.project.rushabh.epicure.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.rushabh.epicure.R;

import java.util.List;

/**
 * Created by rushabh.modi on 10/04/18.
 */

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {

    List<String> placeNameList, timeStampList, itemCountList, statusList;

    public OrderRecyclerAdapter(List<String> placeNameList, List<String> timeStampList, List<String> itemCountList, List<String> statusList) {
        this.placeNameList = placeNameList;
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
        holder.orderDateTimeText.setText(timeStampList.get(position));
        holder.orderCountText.setText(itemCountList.get(position));
        holder.orderStatusText.setText(statusList.get(position));
    }

    @Override
    public int getItemCount() {
        return placeNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView orderPlaceNameText, orderDateTimeText, orderCountText, orderStatusText;

        public ViewHolder(View itemView) {
            super(itemView);
            orderPlaceNameText = itemView.findViewById(R.id.text_order_restaurant);
            orderDateTimeText = itemView.findViewById(R.id.text_order_date_time);
            orderCountText = itemView.findViewById(R.id.text_order_itemcount);
            orderStatusText = itemView.findViewById(R.id.text_order_status);
        }
    }
}
