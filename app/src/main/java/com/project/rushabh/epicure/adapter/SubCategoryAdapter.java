package com.project.rushabh.epicure.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.model.SubCategory;

import java.util.ArrayList;

/**
 * Created by brkckr on 28.10.2017.
 */

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {
    private ArrayList<SubCategory> subCategoryList;
    public ISubCategoryAdapterItemCallback subCategoryAdapterItemCallback;

    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSubCategoryName;

        public SubCategoryViewHolder(View view) {
            super(view);
            txtSubCategoryName = view.findViewById(R.id.txtSubCategory);
        }
    }

    public interface ISubCategoryAdapterItemCallback {
        void onSubCategoryItemCallback(int position);
    }

    public SubCategoryAdapter(ArrayList<SubCategory> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_item, parent, false);
        return new SubCategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final SubCategory subCategoryItem = subCategoryList.get(position);
        holder.txtSubCategoryName.setText(subCategoryItem.name);

        if (subCategoryItem.isSelected) {
            holder.txtSubCategoryName.setBackgroundResource(R.drawable.sub_category_selected_item);
            holder.txtSubCategoryName.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.txtSubCategoryName.setBackgroundResource(R.drawable.sub_category_item);
            holder.txtSubCategoryName.setTextColor(Color.parseColor("#3F51B5"));
        }

        holder.txtSubCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subCategoryAdapterItemCallback != null) {
                    subCategoryAdapterItemCallback.onSubCategoryItemCallback(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public SubCategory getItem(@SuppressWarnings("SameParameterValue") int position) {
        return subCategoryList.get(position);
    }
}
