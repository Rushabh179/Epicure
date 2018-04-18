package com.project.rushabh.restaurant.adapter;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.rushabh.restaurant.R;

import java.util.List;

/**
 * Created by rushabh.modi on 09/04/18.
 */

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {

    List<String> senderEmailList, timeStampList, itemCountList, statusList;
    CharSequence status;
    CharSequence statusOptions[];
    FirebaseFirestore db;

    public OrderRecyclerAdapter(List<String> senderEmailList, List<String> timeStampList, List<String> itemCountList, List<String> statusList) {
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
        statusOptions = new CharSequence[]{"Open", "On way", "Closed"};
        db = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderEmailText.setText(senderEmailList.get(position));
        holder.orderDateTimeText.setText(timeStampList.get(position));
        holder.orderCountText.setText(itemCountList.get(position));
        holder.orderStatusText.setText(statusList.get(position));
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

            orderStatusText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Options")
                            .setItems(statusOptions, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    status = statusOptions[i];
                                    orderStatusText.setText(status);
                                }
                            })
                            .create()
                            .show();
                }
            });
        }
    }
}
