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

    private List<String> placesTitle;
    private Context context;

    public PlacesRecyclerViewAdapter(Context context, List<String> placesTitle) {
        this.context = context;
        this.placesTitle = placesTitle;
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
        holder.placesRecyclerTitle.setText(placesTitle.get(position));
    }

    @Override
    public int getItemCount() {
        return placesTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView placesRecyclerTitle;

        public ViewHolder(final View itemView) {
            super(itemView);
            placesRecyclerTitle = itemView.findViewById(R.id.text_places_title);
            placesRecyclerTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, Integer.toString(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, ItemActivity.class));
                }
            });
        }
    }
}
