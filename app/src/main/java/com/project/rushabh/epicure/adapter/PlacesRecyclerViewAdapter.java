package com.project.rushabh.epicure.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.GeoPoint;
import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.activity.ItemActivity;
import com.project.rushabh.epicure.activity.MapsActivity;

import java.util.List;

/**
 * Created by Rushabh on 05-Mar-18.
 */

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private List<List<String>> placesLists;
    private List<String> placesNameList, placesInformationList, placesImageList;
    private List<GeoPoint> placesLocationList;
    private Context context;

    public PlacesRecyclerViewAdapter(Context context, List<List<String>> placesLists, List<GeoPoint> placesLocationList) {
        this.context = context;
        this.placesLists = placesLists;
        placesNameList = placesLists.get(0);
        placesInformationList = placesLists.get(1);
        placesImageList = placesLists.get(2);
        this.placesLocationList = placesLocationList;
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
        holder.placeTitleText.setText(placesNameList.get(position));
        holder.placesDetailsText.setText(placesInformationList.get(position));
        Glide.with(context).load(placesImageList.get(position)).into(holder.placesThumbImage);
    }

    @Override
    public int getItemCount() {
        return placesNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placeTitleText, placesDetailsText;
        private ImageView placesThumbImage, placesLocationImage;

        public ViewHolder(final View itemView) {
            super(itemView);
            placeTitleText = itemView.findViewById(R.id.text_places_title);
            placesDetailsText = itemView.findViewById(R.id.text_places_details);
            placesThumbImage = itemView.findViewById(R.id.image_places_thumb);
            placesLocationImage = itemView.findViewById(R.id.image_place_location);

            placeTitleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, Integer.toString(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, ItemActivity.class));
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
}
