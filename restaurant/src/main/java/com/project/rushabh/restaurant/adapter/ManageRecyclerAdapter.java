package com.project.rushabh.restaurant.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.rushabh.restaurant.R;
import com.project.rushabh.restaurant.interfaces.OnRecyclerClickListener;

import java.util.List;

/**
 * Created by rushabh.modi on 04/04/18.
 */

public class ManageRecyclerAdapter extends RecyclerView.Adapter<ManageRecyclerAdapter.ViewHolder> {

    private List<String> items;
    private OnRecyclerClickListener onRecyclerClickListener;

    public ManageRecyclerAdapter(List<String> items) {
        this.items = items;
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_manage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemText.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;

        public ViewHolder(View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.text_list_manage);

            itemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onRecyclerClickListener != null) {
                        onRecyclerClickListener.onRecyclerClick(view, getAdapterPosition());
                    }
                }
            });
        }
    }
}
