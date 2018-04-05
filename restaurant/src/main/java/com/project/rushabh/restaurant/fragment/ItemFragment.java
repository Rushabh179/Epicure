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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.rushabh.restaurant.R;
import com.project.rushabh.restaurant.adapter.ManageRecyclerAdapter;
import com.project.rushabh.restaurant.interfaces.OnRecyclerClickListener;
import com.project.rushabh.restaurant.test.R_MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rushabh.modi on 05/04/18.
 */

public class ItemFragment extends Fragment implements OnRecyclerClickListener, View.OnClickListener {

    private RecyclerView subCategoryRecyclerView;
    private TextView manageTitleText;
    private FloatingActionButton manageAddFab;
    private ManageRecyclerAdapter subCategoryRecyclerAdapter;
    private List<String> items, subCategoryIdList, subCategoryNameList;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private int subCategoryPosition;
    private Query itemQuery;
    private View rootView, dialogView;
    private Spinner subCategoryId;
    private TextInputEditText itemName, itemPrice, itemImage;
    private Map<String, Object> itemMap;

    private OnRecyclerClickListener onRecyclerClickListener;

    public ItemFragment() {
    }

    public void setSubCategoryinfo(int subCategoryPosition, List<String> subCategoryNameList, List<String> subCategoryIdList) {
        this.subCategoryPosition = subCategoryPosition;
        this.subCategoryNameList = subCategoryNameList;
        this.subCategoryIdList = subCategoryIdList;
        this.subCategoryNameList.remove(0);
        this.subCategoryIdList.remove(0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        onRecyclerClickListener = this;
        collectionReference = db.collection("restaurants").document("BJSdynFnNrQbGXmX7iMp").collection("items");
        if (subCategoryPosition == 0)
            itemQuery = collectionReference;
        else
            itemQuery = collectionReference.whereEqualTo("subCategoryId", subCategoryIdList.get(subCategoryPosition - 1));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_manage, container, false);
        manageAddFab = rootView.findViewById(R.id.fab_manage);
        manageAddFab.setOnClickListener(this);
        manageTitleText = rootView.findViewById(R.id.text_title_manage);
        manageTitleText.setText(getText(R.string.manage_title_items));
        subCategoryRecyclerView = rootView.findViewById(R.id.recyclerView_manage);
        subCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        subCategoryRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL));
        itemQuery.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                items.add(document.getString("name"));
                                //Toast.makeText(getContext(), document.getString("name"), Toast.LENGTH_SHORT).show();
                            }
                            subCategoryRecyclerAdapter = new ManageRecyclerAdapter(items);
                            subCategoryRecyclerAdapter.setOnRecyclerClickListener(onRecyclerClickListener);
                            subCategoryRecyclerView.setAdapter(subCategoryRecyclerAdapter);
                        }
                    }
                });
        return rootView;
    }

    @Override
    public void onRecyclerClick(View view, int position) {

    }

    @SuppressLint("InflateParams")
    @Override
    public void onClick(View view) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        dialogView = inflater.inflate(R.layout.dialog_add_item, null, false);
        subCategoryId = dialogView.findViewById(R.id.spinner_dialog_subcategory);
        itemName = dialogView.findViewById(R.id.textInputEditText_item_name);
        itemPrice = dialogView.findViewById(R.id.textInputEditText_item_price);
        itemImage = dialogView.findViewById(R.id.textInputEditText_item_image);
        subCategoryId.setAdapter(new ArrayAdapter<>(dialogView.getContext(), android.R.layout.simple_list_item_1, subCategoryNameList));
        subCategoryId.setSelection(subCategoryPosition != 0 ? subCategoryPosition - 1 : subCategoryPosition);
        //subCategoryPosition != 0 ? subCategoryId.setSelection(subCategoryPosition - 1) : subCategoryId.setSelection(subCategoryPosition);
        itemMap = new HashMap<>();

        new AlertDialog.Builder(view.getContext())
                .setTitle("Add Item")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemMap.put("subCategoryId", subCategoryIdList.get(subCategoryId.getSelectedItemPosition()));
                        itemMap.put("id", 0);
                        itemMap.put("name", itemName.getText().toString());
                        itemMap.put("price", Double.parseDouble(itemPrice.getText().toString()));
                        itemMap.put("image", itemImage.getText().toString());
                        db.collection("restaurants").document("BJSdynFnNrQbGXmX7iMp").collection("items")
                                .add(itemMap)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
}
