package com.project.rushabh.restaurant.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.rushabh.restaurant.R;
import com.project.rushabh.restaurant.adapter.ManageRecyclerAdapter;
import com.project.rushabh.restaurant.interfaces.OnRecyclerClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by rushabh.modi on 05/04/18.
 */

public class ItemFragment extends Fragment implements OnRecyclerClickListener, View.OnClickListener {

    private RecyclerView itemRecyclerView;
    private TextView manageTitleText;
    private FloatingActionButton itemAddFab;
    private ManageRecyclerAdapter itemRecyclerAdapter;
    private List<String> itemIdList, itemNameList, itemPriceList, itemImageList, subCategoryIdList, subCategoryNameList;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private int subCategoryPosition;
    private View rootView, dialogView;
    private Spinner subCategoryId;
    private TextInputEditText itemName, itemPrice, itemImage;
    private Map<String, Object> itemMap;

    private OnRecyclerClickListener onRecyclerClickListener;

    public ItemFragment() {
    }

    public void setSubCategoryinfo(int subCategoryPosition, List<String> subCategoryIdList, List<String> subCategoryNameList) {
        this.subCategoryPosition = subCategoryPosition;
        this.subCategoryIdList = subCategoryIdList;
        this.subCategoryNameList = subCategoryNameList;
        /*this.subCategoryNameList.remove(0);
        this.subCategoryIdList.remove(0);*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRecyclerClickListener = this;
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("restaurants").document("BJSdynFnNrQbGXmX7iMp").collection("items");
        /*if (subCategoryPosition == 0)
            itemBySubCategoryQuery = collectionReference;
        else*/
        //itemBySubCategoryQuery = collectionReference.whereEqualTo("subCategoryId", subCategoryIdList.get(subCategoryPosition /*- 1*/));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        itemIdList = new ArrayList<>();
        itemNameList = new ArrayList<>();
        itemPriceList = new ArrayList<>();
        itemImageList = new ArrayList<>();
        rootView = inflater.inflate(R.layout.fragment_manage, container, false);
        itemAddFab = rootView.findViewById(R.id.fab_manage);
        itemAddFab.setOnClickListener(this);
        manageTitleText = rootView.findViewById(R.id.text_title_manage);
        manageTitleText.setText(getText(R.string.manage_title_items));
        itemRecyclerView = rootView.findViewById(R.id.recyclerView_manage);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL));
        collectionReference
                .whereEqualTo("subCategoryId", subCategoryIdList.get(subCategoryPosition /*- 1*/))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                itemIdList.add(document.getId());
                                itemNameList.add(document.getString("name"));
                                itemPriceList.add(document.getDouble("price").toString());
                                itemImageList.add(document.getString("image"));
                            }
                            itemRecyclerAdapter = new ManageRecyclerAdapter(itemNameList);
                            itemRecyclerAdapter.setOnRecyclerClickListener(onRecyclerClickListener);
                            itemRecyclerView.setAdapter(itemRecyclerAdapter);
                        }
                    }
                });
        return rootView;
    }

    @Override
    public void onRecyclerClick(final View view, final int position) {

    }

    @Override
    public void onRecyclerLongClick(final View view, final int position) {
        new AlertDialog.Builder(view.getContext())
                .setTitle("Options")
                .setItems(new CharSequence[]{"Update", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                showDialog("Update", position);
                                break;
                            case 1:
                                new AlertDialog.Builder(view.getContext())
                                        .setMessage("Are you sure you want to delete this item?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                deleteItemQuery(position);
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .create()
                                        .show();
                                break;
                        }
                    }
                })
                .create()
                .show();
    }

    @SuppressLint("InflateParams")
    @Override
    public void onClick(View view) { //FAB
        showDialog("Add", 0);
    }

    @SuppressLint("InflateParams")
    public void showDialog(final String type, final int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        dialogView = inflater.inflate(R.layout.dialog_item, null, false);
        subCategoryId = dialogView.findViewById(R.id.spinner_dialog_subcategory);
        itemName = dialogView.findViewById(R.id.textInputEditText_item_name);
        itemPrice = dialogView.findViewById(R.id.textInputEditText_item_price);
        itemImage = dialogView.findViewById(R.id.textInputEditText_item_image);
        subCategoryId.setAdapter(new ArrayAdapter<>(dialogView.getContext(), android.R.layout.simple_list_item_1, subCategoryNameList));
        subCategoryId.setSelection(subCategoryPosition /*!= 0 ? subCategoryPosition - 1 : subCategoryPosition*/);
        //subCategoryPosition != 0 ? subCategoryId.setSelection(subCategoryPosition - 1) : subCategoryId.setSelection(subCategoryPosition);
        itemMap = new HashMap<>();

        if (Objects.equals(type, "Update")) {
            itemName.setText(itemNameList.get(position));
            itemPrice.setText(itemPriceList.get(position));
            itemImage.setText(itemImageList.get(position));
        }

        new AlertDialog.Builder(rootView.getContext())
                .setTitle(type + " Item")
                .setView(dialogView)
                .setPositiveButton(type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemMap.put("subCategoryId", subCategoryIdList.get(subCategoryId.getSelectedItemPosition()));
                        itemMap.put("id", 0);
                        itemMap.put("name", itemName.getText().toString());
                        itemMap.put("price", Double.parseDouble(itemPrice.getText().toString().isEmpty() ? "0" : itemPrice.getText().toString()));
                        itemMap.put("image", itemImage.getText().toString());
                        if (Objects.equals(type, "Add"))
                            addItemQuery(itemMap);
                        else
                            updateItemQuery(itemMap, position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create()
                .show();
    }

    public void addItemQuery(final Map<String, Object> itemMap) {
        collectionReference
                .add(itemMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        if (Objects.equals(itemMap.get("subCategoryId").toString(), subCategoryIdList.get(subCategoryPosition))) {
                            itemIdList.add(documentReference.getId());
                            itemNameList.add(itemMap.get("name").toString());
                            itemPriceList.add(itemMap.get("price").toString());
                            itemImageList.add(itemMap.get("image").toString());
                            itemRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void updateItemQuery(Map<String, Object> itemMap, final int position) {
        collectionReference.document(itemIdList.get(position))
                .update(itemMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                        itemRecyclerAdapter.notifyItemChanged(position);
                    }
                });
    }

    public void deleteItemQuery(final int position) {
        collectionReference.document(itemIdList.get(position))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        itemNameList.remove(position);
                        itemIdList.remove(position);
                        itemRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }
}
