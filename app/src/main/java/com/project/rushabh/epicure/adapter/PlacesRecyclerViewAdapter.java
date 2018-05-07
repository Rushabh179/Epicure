package com.project.rushabh.epicure.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.activity.MapsActivity;
import com.project.rushabh.epicure.interfaces.OnRecyclerClickListener;

import java.util.List;

/**
 * Created by Rushabh on 05-Mar-18.
 */

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private List<List<String>> placesLists;
    private List<String> placesNameList, placesInformationList, placesImageList;
    private List<GeoPoint> placesLocationList;
    private Context context;
    private OnRecyclerClickListener onRecyclerClickListener;
    private View dialogView;
    private TextView emailText, nameText, phoneText, addressText;
    private FirebaseFirestore db;

    public PlacesRecyclerViewAdapter(Context context, List<List<String>> placesLists, List<GeoPoint> placesLocationList) {
        this.context = context;
        this.placesLists = placesLists;
        placesNameList = placesLists.get(0);
        placesInformationList = placesLists.get(1);
        placesImageList = placesLists.get(2);
        this.placesLocationList = placesLocationList;
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_recyclerview_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.placesTitleText.setText(placesNameList.get(position));
        holder.placesDetailsText.setText(placesInformationList.get(position));
        Glide.with(context).load(placesImageList.get(position)).into(holder.placesThumbImage);
    }

    @Override
    public int getItemCount() {
        return placesNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placesTitleText, placesDetailsText;
        private ImageView placesThumbImage, placesLocationImage;

        public ViewHolder(final View itemView) {
            super(itemView);
            placesTitleText = itemView.findViewById(R.id.text_places_title);
            placesDetailsText = itemView.findViewById(R.id.text_places_details);
            placesThumbImage = itemView.findViewById(R.id.image_places_thumb);
            placesLocationImage = itemView.findViewById(R.id.image_place_location);
            db = FirebaseFirestore.getInstance();

            placesTitleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, Integer.toString(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    //context.startActivity(new Intent(context, ItemActivity.class));
                    if (onRecyclerClickListener != null) {
                        onRecyclerClickListener.onRecyclerClick(view, getAdapterPosition());
                    }
                }
            });

            placesDetailsText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRecyclerClickListener != null) {
                        onRecyclerClickListener.onRecyclerClick(view, getAdapterPosition());
                    }
                }
            });

            placesTitleText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showInfo(getAdapterPosition());
                    return false;
                }
            });

            placesDetailsText.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showInfo(getAdapterPosition());
                    return false;
                }
            });

            placesLocationImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, MapsActivity.class)
                            .putExtra("source", "places")
                            .putExtra("latitude", placesLocationList.get(getAdapterPosition()).getLatitude())
                            .putExtra("longitude", placesLocationList.get(getAdapterPosition()).getLongitude()));
                }
            });
        }
    }

    @SuppressLint("InflateParams")
    public void showInfo(int position){
        LayoutInflater inflater = LayoutInflater.from(context);
        dialogView = inflater.inflate(R.layout.dialog_places_info, null, false);
        emailText = dialogView.findViewById(R.id.editText_dialog_email);
        nameText = dialogView.findViewById(R.id.editText_dialog_name);
        phoneText = dialogView.findViewById(R.id.editText_dialog_phone);
        addressText = dialogView.findViewById(R.id.editText_dialog_address);

        db.collection("restaurants").whereEqualTo("name", placesNameList.get(position))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                emailText.setText(document.getString("email"));
                                nameText.setText(document.getString("name"));
                                phoneText.setText(document.getString("contact"));
                                addressText.setText(document.getString("address"));
                            }
                        }
                    }
                });

        new AlertDialog.Builder(context)
                .setTitle("Restaurant Info")
                .setView(dialogView)
                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

}
