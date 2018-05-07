package com.project.rushabh.delivery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by Rushabh on 11-Apr-18.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<String> orderIdList, placeNameList, senderEmailList, timeStampList, itemCountList, statusList;
    FirebaseFirestore db;
    View dialogView;
    TextView emailText, priceText, phoneText, addressText, itemsText;
    Context context;

    public OrderAdapter(Context context, List<String> orderIdList, List<String> placeNameList, List<String> senderEmailList, List<String> timeStampList, List<String> itemCountList, List<String> statusList) {
        this.context = context;
        this.orderIdList = orderIdList;
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
        db = FirebaseFirestore.getInstance();
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
                    }
                })
                .create()
                .show();
    }
}
