package com.project.rushabh.restaurant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.rushabh.restaurant.R;
import com.project.rushabh.restaurant.adapter.ManageRecyclerAdapter;
import com.project.rushabh.restaurant.interfaces.OnRecyclerClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rushabh.modi on 05/04/18.
 */

public class ItemFragment extends Fragment implements OnRecyclerClickListener{

    private RecyclerView subCategoryRecyclerView;
    private TextView manageTitleText;
    private ManageRecyclerAdapter subCategoryRecyclerAdapter;
    private List<String> items;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    private OnRecyclerClickListener onRecyclerClickListener;

    public ItemFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        onRecyclerClickListener = this;
        collectionReference = db.collection("restaurants").document("BJSdynFnNrQbGXmX7iMp").collection("items");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage, container, false);
        manageTitleText = rootView.findViewById(R.id.text_title_manage);
        manageTitleText.setText(getText(R.string.manage_title_items));
        subCategoryRecyclerView = rootView.findViewById(R.id.recyclerView_manage);
        subCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        subCategoryRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL));
        collectionReference
                .get()
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
}
