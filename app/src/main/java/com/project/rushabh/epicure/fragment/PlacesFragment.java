package com.project.rushabh.epicure.fragment;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.adapter.PlacesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment {

    private RecyclerView placesRecyclerView;
    private PlacesRecyclerViewAdapter placesRecyclerViewAdapter;
    private List<String> placesNameList, placesInformationList, placesImageList;
    private List<GeoPoint> placesLocationList;
    private DividerItemDecoration decoration;
    private List<List<String>> placesLists;

    FirebaseFirestore db;

    public PlacesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placesNameList = new ArrayList<>();
        placesInformationList = new ArrayList<>();
        placesImageList = new ArrayList<>();
        placesLists = new ArrayList<>();
        placesLocationList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_places, container, false);

        placesRecyclerView = view.findViewById(R.id.recyclerview_main);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        decoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        placesRecyclerView.addItemDecoration(decoration);

        db.collection("restaurants")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                //Toast.makeText(getContext(), document.getString("name"), Toast.LENGTH_SHORT).show();
                                placesNameList.add(document.getString("name"));
                                placesInformationList.add(document.getString("information"));
                                placesImageList.add(document.getString("image"));
                                placesLocationList.add(document.getGeoPoint("location"));
                            }
                            placesLists.add(placesNameList);
                            placesLists.add(placesInformationList);
                            placesLists.add(placesImageList);

                            placesRecyclerViewAdapter = new PlacesRecyclerViewAdapter(getContext(), placesLists, placesLocationList);
                            placesRecyclerView.setAdapter(placesRecyclerViewAdapter);
                        } else {
                            Toast.makeText(getContext(), "Error getting documents", Toast.LENGTH_SHORT).show();
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return view;
    }

}
