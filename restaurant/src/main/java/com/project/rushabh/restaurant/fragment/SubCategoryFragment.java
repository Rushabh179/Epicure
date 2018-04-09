package com.project.rushabh.restaurant.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by rushabh.modi on 04/04/18.
 */

public class SubCategoryFragment extends Fragment implements OnRecyclerClickListener, View.OnClickListener {

    private RecyclerView subCategoryRecyclerView;
    private TextView manageTitleText;
    private FloatingActionButton subCategoryAddFab;
    private ManageRecyclerAdapter subCategoryRecyclerAdapter;
    private List<String> subCategoryIdList, subCategoryNameList, categoryIdList, categoryNameList;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private int categoryPosition;
    private Query subCategoryQuery;
    private View rootView, dialogView;
    private Spinner categoryId;
    private TextInputEditText subCategoryName;
    private Map<String, Object> itemMap;
    private SharedPreferences sharedPreferences;

    private OnRecyclerClickListener onRecyclerClickListener;

    public SubCategoryFragment() {
    }

    public void setCategoryinfo(int categoryPosition, List<String> categoryIdList, List<String> categoryNameList) {
        this.categoryPosition = categoryPosition;
        this.categoryIdList = categoryIdList;
        this.categoryNameList = categoryNameList;
        categoryIdList.remove(0);
        categoryNameList.remove(0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRecyclerClickListener = this;
        db = FirebaseFirestore.getInstance();
        assert getActivity() != null;
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        documentReference = db.collection("restaurants").document(sharedPreferences.getString(getString(R.string.spk_restaurant_id),""));
        collectionReference = documentReference.collection("subcategories");
        if (categoryPosition == 0)
            subCategoryQuery = collectionReference;
        else
            subCategoryQuery = collectionReference.whereEqualTo("categoryId", categoryIdList.get(categoryPosition - 1));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        subCategoryIdList = new ArrayList<>();
        subCategoryNameList = new ArrayList<>();
        rootView = inflater.inflate(R.layout.fragment_manage, container, false);
        subCategoryAddFab = rootView.findViewById(R.id.fab_manage);
        subCategoryAddFab.setOnClickListener(this);
        manageTitleText = rootView.findViewById(R.id.text_title_manage);
        manageTitleText.setText(getText(R.string.manage_title_subcategory));
        subCategoryRecyclerView = rootView.findViewById(R.id.recyclerView_manage);
        subCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        subCategoryRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL));
        subCategoryQuery
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subCategoryNameList.add(document.getString("name"));
                                subCategoryIdList.add(document.getId());
                                //Toast.makeText(getContext(), document.getString("name"), Toast.LENGTH_SHORT).show();
                            }
                            subCategoryRecyclerAdapter = new ManageRecyclerAdapter(subCategoryNameList);
                            subCategoryRecyclerAdapter.setOnRecyclerClickListener(onRecyclerClickListener);
                            subCategoryRecyclerAdapter.setOnRecyclerLongClickListener(onRecyclerClickListener);
                            subCategoryRecyclerView.setAdapter(subCategoryRecyclerAdapter);
                        }
                    }
                });
        return rootView;
    }

    @Override
    public void onRecyclerClick(View view, int position) {
        ItemFragment itemFragment = new ItemFragment();
        FragmentManager fragmentManager = getFragmentManager();
        itemFragment.setSubCategoryinfo(position, subCategoryIdList, subCategoryNameList);
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
        fragmentTransaction.replace(R.id.frame_container, itemFragment);
        fragmentTransaction.commit();
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
                                        .setMessage("Are you sure you want to delete this subcategory? All the items within it will also be deleted!")
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

    @Override
    public void onClick(View view) {
        showDialog("Add", 0);
    }

    @SuppressLint("InflateParams")
    private void showDialog(final String type, final int position) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        dialogView = inflater.inflate(R.layout.dialog_sub_category, null, false);
        categoryId = dialogView.findViewById(R.id.spinner_dialog_category);
        subCategoryName = dialogView.findViewById(R.id.textInputEditText_subcategory_name);
        categoryId.setAdapter(new ArrayAdapter<>(dialogView.getContext(), android.R.layout.simple_list_item_1, categoryNameList));
        categoryId.setSelection(categoryPosition != 0 ? categoryPosition - 1 : categoryPosition);
        //subCategoryPosition != 0 ? subCategoryId.setSelection(subCategoryPosition - 1) : subCategoryId.setSelection(subCategoryPosition);
        itemMap = new HashMap<>();

        if (Objects.equals(type, "Update"))
            subCategoryName.setText(subCategoryNameList.get(position));

        new AlertDialog.Builder(rootView.getContext())
                .setTitle(type + " Item")
                .setView(dialogView)
                .setPositiveButton(type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        itemMap.put("categoryId", categoryIdList.get(categoryId.getSelectedItemPosition()));
                        itemMap.put("id", 0);
                        itemMap.put("name", subCategoryName.getText().toString());
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
                        /*if (Objects.equals(itemMap.get("categoryId").toString(), categoryIdList.get(categoryPosition))) {*/
                        subCategoryIdList.add(documentReference.getId());
                        subCategoryNameList.add(itemMap.get("name").toString());
                        subCategoryRecyclerAdapter.notifyDataSetChanged();
                        //}
                    }
                });
    }

    private void updateItemQuery(final Map<String, Object> itemMap, final int position) {
        collectionReference.document(subCategoryIdList.get(position))
                .update(itemMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                        subCategoryNameList.remove(position);
                        subCategoryNameList.add(position, itemMap.get("name").toString());
                        subCategoryRecyclerAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void deleteItemQuery(final int position) {
        collectionReference.document(subCategoryIdList.get(position))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                        subCategoryIdList.remove(position);
                        subCategoryNameList.remove(position);
                        subCategoryRecyclerAdapter.notifyDataSetChanged();
                    }
                });
        documentReference.collection("items")
                .whereEqualTo("subCategoryId", subCategoryIdList.get(position))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                document.getReference().delete();
                    }
                });
    }
}
