package com.project.rushabh.restaurant.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by rushabh.modi on 04/04/18.
 */

public class CategoryFragment extends Fragment implements OnRecyclerClickListener, View.OnClickListener {

    private RecyclerView categoryRecyclerView;
    private TextView manageTitleText;
    private FloatingActionButton categoryAddFab;
    private ManageRecyclerAdapter categoryRecyclerAdapter;
    private List<String> categoryIdList, categoryNameList, subCategoryIdList;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private View rootView, dialogView;
    private TextInputEditText categoryName;
    private Map<String, Object> itemMap;

    private OnRecyclerClickListener onRecyclerClickListener;

    public CategoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        onRecyclerClickListener = this;
        collectionReference = db.collection("restaurants").document("BJSdynFnNrQbGXmX7iMp").collection("category");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        categoryIdList = new ArrayList<>();
        categoryNameList = new ArrayList<>();
        subCategoryIdList = new ArrayList<>();
        rootView = inflater.inflate(R.layout.fragment_manage, container, false);
        categoryAddFab = rootView.findViewById(R.id.fab_manage);
        categoryAddFab.setOnClickListener(this);
        manageTitleText = rootView.findViewById(R.id.text_title_manage);
        manageTitleText.setText(getText(R.string.manage_title_category));
        categoryRecyclerView = rootView.findViewById(R.id.recyclerView_manage);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL));
        collectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                categoryIdList.add(document.getId());
                                categoryNameList.add(document.getString("name"));
                                //Toast.makeText(getContext(), document.getString("name"), Toast.LENGTH_SHORT).show();
                            }
                            categoryRecyclerAdapter = new ManageRecyclerAdapter(categoryNameList);
                            categoryRecyclerAdapter.setOnRecyclerClickListener(onRecyclerClickListener);
                            categoryRecyclerView.setAdapter(categoryRecyclerAdapter);
                        }
                    }
                });
        return rootView;
    }

    @Override
    public void onRecyclerClick(View view, int position) {
        SubCategoryFragment subCategoryFragment = new SubCategoryFragment();
        FragmentManager fragmentManager = getFragmentManager();
        subCategoryFragment.setCategoryinfo(position, categoryIdList, categoryNameList);
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
        fragmentTransaction.replace(R.id.frame_container, subCategoryFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRecyclerLongClick(final View view, final int position) {
        if (position != 0) {
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
                                            .setMessage("Are you sure you want to delete this category? All the subcategories and items within it will also be deleted!")
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
    }

    @Override
    public void onClick(View view) {
        showDialog("Add", 0);
    }

    @SuppressLint("InflateParams")
    private void showDialog(final String type, final int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        dialogView = inflater.inflate(R.layout.dialog_category, null, false);
        categoryName = dialogView.findViewById(R.id.textInputEditText_category_name);
        itemMap = new HashMap<>();

        if (Objects.equals(type, "Update"))
            categoryName.setText(categoryNameList.get(position));

        new AlertDialog.Builder(rootView.getContext())
                .setTitle(type + " Item")
                .setView(dialogView)
                .setPositiveButton(type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemMap.put("id", 0);
                        itemMap.put("name", categoryName.getText().toString());
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

    private void addItemQuery(final Map<String, Object> itemMap) {
        collectionReference
                .add(itemMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Added", Toast.LENGTH_SHORT).show();
                        categoryIdList.add(documentReference.getId());
                        categoryNameList.add(itemMap.get("name").toString());
                        categoryRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void updateItemQuery(final Map<String, Object> itemMap, final int position) {
        collectionReference.document(categoryIdList.get(position))
                .update(itemMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                        categoryNameList.remove(position);
                        categoryNameList.add(position, itemMap.get("name").toString());
                        categoryRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void deleteItemQuery(final int position) {
        collectionReference.document(categoryIdList.get(position))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        categoryIdList.remove(position);
                        categoryNameList.remove(position);
                        categoryRecyclerAdapter.notifyDataSetChanged();
                    }
                });

        db.collection("restaurants").document("BJSdynFnNrQbGXmX7iMp").collection("subcategories")
                .whereEqualTo("categoryId", categoryIdList.get(position))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subCategoryIdList.add(document.getId());
                                document.getReference().delete();
                            }
                        for (String subCategoryId : subCategoryIdList)
                            db.collection("restaurants").document("BJSdynFnNrQbGXmX7iMp").collection("items")
                                    .whereEqualTo("subCategoryId", subCategoryId)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful())
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    document.getReference().delete();
                                                }
                                        }
                                    });
                    }
                });

    }
}
