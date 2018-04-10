package com.project.rushabh.epicure.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.adapter.OrderRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by rushabh.modi on 10/04/18.
 */

public class OrderFragment extends Fragment {

    private View rootView;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private RecyclerView orderRecyclerView;
    private List<String> placeNameList, timeStampList, itemCountList, statusList;
    private List<List<Map<String, Object>>> itemList;
    private OrderRecyclerAdapter orderRecyclerAdapter;

    public OrderFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        assert getContext() != null;
        sharedPreferences = getContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, container, false);
        placeNameList = new ArrayList<>();
        timeStampList = new ArrayList<>();
        itemCountList = new ArrayList<>();
        itemList = new ArrayList<>();
        statusList = new ArrayList<>();
        orderRecyclerView = rootView.findViewById(R.id.recyclerView_order);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL));
        db.collection("orders")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                //.whereEqualTo("senderFirebaseId", sharedPreferences.getString(getString(R.string.spk_user_id), ""))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (Objects.equals(document.getString("senderFirebaseId"),
                                        sharedPreferences.getString(getString(R.string.spk_user_id), ""))) { //Todo: Find a proper method
                                    placeNameList.add(document.getString("restaurantName"));
                                    timeStampList.add(document.getDate("timeStamp").toString());
                                    itemList.add((List<Map<String, Object>>) document.get("item"));
                                    statusList.add(document.getString("status"));
                                }
                            }
                            for (int i = 0; i < itemList.size(); i++)
                                itemCountList.add(Integer.toString(itemList.get(i).size())+" items");
                            //Toast.makeText(getContext(), i + Integer.toString(itemList.get(i).size()), Toast.LENGTH_SHORT).show();
                            orderRecyclerAdapter = new OrderRecyclerAdapter(placeNameList, timeStampList, itemCountList, statusList);
                            orderRecyclerView.setAdapter(orderRecyclerAdapter);
                        }
                    }
                });
        return rootView;
    }
}