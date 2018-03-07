package com.project.rushabh.epicure.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.activity.ItemActivity;

import java.util.List;

/**
 * Created by Rushabh on 05-Mar-18.
 */

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    private List<List<String>> placesLists;
    private List<String> placesTitle, placesDetails;
    private Context context;

    public PlacesRecyclerViewAdapter(Context context, List<List<String>> placesLists) {
        this.context = context;
        this.placesLists = placesLists;
        placesTitle = placesLists.get(0);
        placesDetails = placesLists.get(1);
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
        holder.placeTitleText.setText(placesTitle.get(position));
        holder.placesDetailsText.setText(placesDetails.get(position));
    }

    @Override
    public int getItemCount() {
        return placesTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView placeTitleText, placesDetailsText;

        public ViewHolder(final View itemView) {
            super(itemView);
            placeTitleText = itemView.findViewById(R.id.text_places_title);
            placesDetailsText = itemView.findViewById(R.id.text_places_details);
            placeTitleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, Integer.toString(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, ItemActivity.class));
                }
            });
        }
    }
}
