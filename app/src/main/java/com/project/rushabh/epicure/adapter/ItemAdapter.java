package com.project.rushabh.epicure.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.bumptech.glide.Glide;
import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.activity.LoginActivity;
import com.project.rushabh.epicure.model.Item;
import com.project.rushabh.epicure.model.Solution;

/**
 * Created by brkckr on 28.10.2017.
 */

public class ItemAdapter extends SectionedRecyclerViewAdapter<ItemAdapter.ItemViewHolder> {
    private Solution solution;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    private IItemAdapterCallback itemCallback;

    public ItemAdapter(Activity activity, Solution solution) {
        this.solution = solution;
        this.activity = activity;
        itemCallback = (IItemAdapterCallback) activity;
        sharedPreferences = activity.getSharedPreferences(activity.getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
    }

    public interface IItemAdapterCallback {
        void onItemCallback(Item item);

        void onAddItemCallback(ImageView imageView, Item item);
    }

    @Override
    public int getSectionCount() {
        return solution.subCategoryList.size();
    }

    @Override
    public int getItemCount(int section) {
        return solution.itemMap.get(solution.subCategoryList.get(section)).size();
    }

    @Override
    public void onBindHeaderViewHolder(ItemViewHolder holder, int section, boolean expanded) {
        holder.txtSectionTitle.setText(solution.subCategoryList.get(section).name);
        holder.imgCaret.setImageResource(expanded ? R.drawable.ic_collapse : R.drawable.ic_expand);
    }

    @Override
    public void onBindFooterViewHolder(ItemViewHolder holder, int section) {
        holder.title.setText("");
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int section, int relativePosition, int absolutePosition) {
        final Item item = solution.itemMap.get(solution.subCategoryList.get(section)).get(relativePosition);

        holder.txtItemName.setText(item.name);

        Glide.with(activity)
                .load(item.url)
                .into(holder.imgThumbnail);

        holder.txtPrice.setText(String.format("%.2f", item.unitPrice));

        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getBoolean(activity.getString(R.string.is_logged_in), false)) {
                    if (itemCallback != null) {
                        final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.scale);
                        holder.imgAdd.startAnimation(myAnim);

                        itemCallback.onAddItemCallback(holder.imgThumbnail, item);
                    }
                } else {
                    Toast.makeText(activity, "You must be logged in to place an order", Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                }
            }
        });

        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCallback != null) {
                    itemCallback.onItemCallback(item);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if (section == 1) {
            // VIEW_TYPE_FOOTER is -3, VIEW_TYPE_HEADER is -2, VIEW_TYPE_ITEM is -1.
            // You can return 0 or greater.
            return 0;
        }
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.item_header;
                break;
            default:
                // Our custom item, which is the 0 returned in getItemViewType() above
                layout = R.layout.item;
                break;
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ItemViewHolder(v, this);
    }

    static class ItemViewHolder extends SectionedViewHolder implements View.OnClickListener {
        final TextView txtItemName;
        final ImageView imgThumbnail;
        final ImageView imgAdd;
        final TextView txtPrice;
        final TextView txtSectionTitle;
        final ImageView imgCaret;
        final ItemAdapter adapter;
        final TextView title;
        final CardView cardItem;

        ItemViewHolder(View itemView, ItemAdapter adapter) {
            super(itemView);
            this.txtItemName = itemView.findViewById(R.id.txtItemName);
            this.imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            this.imgAdd = itemView.findViewById(R.id.imgAdd);
            this.txtPrice = itemView.findViewById(R.id.txtPrice);
            this.txtSectionTitle = itemView.findViewById(R.id.txtSectionTitle);
            this.imgCaret = itemView.findViewById(R.id.imgCaret);
            this.adapter = adapter;
            this.title = itemView.findViewById(R.id.title);
            this.cardItem = itemView.findViewById(R.id.cardItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //if it is header
            if (isHeader()) {
                //get relative section's position
                int position = getRelativePosition().section();

                //set it as expanded or not
                adapter.toggleSectionExpanded(position);

                //set sub category at the postion as expanded or not.
                adapter.solution.subCategoryList.get(position).setIsExpanded();
            }
        }
    }
}
