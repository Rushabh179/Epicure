package com.project.rushabh.restaurant.fragment;

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
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.rushabh.restaurant.R;
import com.project.rushabh.restaurant.adapter.OrderRecyclerAdapter;

import java.util.Map;

/**
 * Created by rushabh.modi on 04/04/18.
 *
 * A placeholder fragment containing a simple view.
 */

public class OrderFragment extends Fragment {

    //private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private RecyclerView orderRecyclerView;
    private OrderRecyclerAdapter orderRecyclerAdapter;

    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;

    private Map<String, Object> orderMap;

    public OrderFragment() {
    }

    /*
     * Returns a new instance of this fragment for the given section number.
     *//*
    public static OrderFragment newInstance(int sectionNumber) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getContext() != null;
        sharedPreferences = getContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        db.collection("orders").whereEqualTo("receiverFirebaseId", sharedPreferences.getString(getString(R.string.spk_restaurant_id), ""))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "Error getting the order", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                //Toast.makeText(MainActivity.this, doc.getId(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, container, false);
        orderRecyclerView = rootView.findViewById(R.id.recyclerView_order);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL));
        return rootView;
    }
}
