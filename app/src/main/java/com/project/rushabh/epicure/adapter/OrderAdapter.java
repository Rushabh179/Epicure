package com.project.rushabh.epicure.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.model.Order;

import java.util.ArrayList;

/**
 * Created by brkckr on 6.12.2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private ArrayList<Order> orderList;
    private Activity activity;

    private IOrderAdapterCallback orderCallback;

    public OrderAdapter(Activity activity, ArrayList<Order> orderList) {
        this.activity = activity;
        this.orderList = orderList;
        orderCallback = (IOrderAdapterCallback) activity;
    }

    public interface IOrderAdapterCallback {
        void onIncreaseDecreaseCallback();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);

        return new OrderViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Order order = orderList.get(position);

        Glide.with(activity)
                .load(order.item.url)
                .into(holder.imgThumbnail);

        holder.txtItemName.setText(order.item.name);
        holder.txtExtendedPrice.setText(String.format("%.2f", order.extendedPrice));
        holder.txtQuantity.setText(String.valueOf(order.quantity));

        holder.btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.quantity++;
                order.extendedPrice = order.quantity * order.item.unitPrice;
                holder.txtQuantity.setText(String.valueOf(order.quantity));
                holder.txtExtendedPrice.setText(String.format("%.2f", order.extendedPrice));

                notifyDataSetChanged();
                orderCallback.onIncreaseDecreaseCallback();
            }
        });

        holder.btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.quantity--;
                order.extendedPrice = order.quantity * order.item.unitPrice;
                holder.txtQuantity.setText(String.valueOf(order.quantity));
                holder.txtExtendedPrice.setText(String.format("%.2f", order.extendedPrice));

                if (order.quantity == 0) {
                    orderList.remove(position);
                }

                notifyDataSetChanged();
                orderCallback.onIncreaseDecreaseCallback();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnail;
        TextView txtItemName;
        TextView txtExtendedPrice;
        TextView txtQuantity;

        Button btnIncrease;

        Button btnDecrease;

        OrderViewHolder(View view) {
            super(view);
            imgThumbnail = view.findViewById(R.id.imgThumbnail);
            txtItemName = view.findViewById(R.id.txtItemName);
            txtExtendedPrice = view.findViewById(R.id.txtExtendedPrice);
            txtQuantity = view.findViewById(R.id.txtQuantity);
            btnIncrease = view.findViewById(R.id.btnIncrease);
            btnDecrease = view.findViewById(R.id.btnDecrease);
        }
    }
}

