package com.project.rushabh.restaurant.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.rushabh.restaurant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rushabh.modi on 09/04/18.
 */

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {

    Context context;
    List<String> orderIdList, senderEmailList, timeStampList, itemCountList, statusList, itemNameList;
    CharSequence status;
    CharSequence statusOptions[];
    FirebaseFirestore db;
    View dialogView;
    TextView emailText, priceText, phoneText, addressText, itemsText;
    Button deliveryBtn;

    public OrderRecyclerAdapter(Context context, List<String> orderIdList, List<String> senderEmailList, List<String> timeStampList, List<String> itemCountList, List<String> statusList) {
        this.context = context;
        this.orderIdList = orderIdList;
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

            orderEmailText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInfo(getAdapterPosition());

                }
            });
        }
    }

    @SuppressLint("InflateParams")
    private void showInfo(int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        dialogView = inflater.inflate(R.layout.dialog_order_info, null, false);
        emailText = dialogView.findViewById(R.id.editText_dialog_email);
        priceText = dialogView.findViewById(R.id.editText_dialog_price);
        phoneText = dialogView.findViewById(R.id.editText_dialog_phone);
        addressText = dialogView.findViewById(R.id.editText_dialog_address);
        itemsText = dialogView.findViewById(R.id.editText_dialog_items);
        deliveryBtn = dialogView.findViewById(R.id.btn_dialog_delivery);
        itemNameList = new ArrayList<>();

        db.collection("orders").document(orderIdList.get(position))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        emailText.setText(documentSnapshot.getString("senderEmail"));
                        priceText.setText(documentSnapshot.getString("totalPrice"));
                        phoneText.setText(documentSnapshot.getString("senderContact"));
                        addressText.setText(documentSnapshot.getString("senderAddress"));
                    }
                });
                /*.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                emailText.setText(document.getString("senderEmail"));
                                priceText.setText(document.getString("totalPrice"));
                                phoneText.setText(document.getString("senderContact"));
                                addressText.setText(document.getString("senderAddress"));
                            }
                        }
                    }
                });*/

        /*deliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        new AlertDialog.Builder(context)
                .setTitle("Order")
                .setView(dialogView)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        deliveryBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogInterface.dismiss();
                            }
                        });
                    }
                })
                .create()
                .show();
    }
}
