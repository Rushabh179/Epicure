package com.project.rushabh.epicure.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.adapter.PlacesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment {

    private RecyclerView placesRecyclerView;
    private PlacesRecyclerViewAdapter placesRecyclerViewAdapter;
    private List<String> placesTitleList, placesDetailList, placesThumbList;
    private DividerItemDecoration decoration;
    private List<List<String>> placesLists;

    FirebaseFirestore db;

    public PlacesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placesTitleList = new ArrayList<>();
        placesDetailList = new ArrayList<>();
        placesThumbList = new ArrayList<>();
        placesLists = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        //Toast.makeText(getContext(), db.collection("restaurant").document("McDonald's").getId(), Toast.LENGTH_LONG).show();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_places);

        View view = inflater.inflate(R.layout.fragment_places, container, false);

        placesRecyclerView = view.findViewById(R.id.recyclerview_main);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        decoration = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        placesRecyclerView.addItemDecoration(decoration);

        db.collection("restaurants")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                Toast.makeText(getContext(), document.getString("name"), Toast.LENGTH_SHORT).show();
                                placesTitleList.add(document.getString("name"));
                                placesDetailList.add(document.getString("information"));
                                //placesDetailList.add("Fast food");
                                //placesDetailList.add("Fast food");
                                //placesDetailList.add("Mexican");

                                placesThumbList.add(document.getString("image"));
                                //placesThumbList.add("https://vignette.wikia.nocookie.net/ronaldmcdonald/images/b/b5/Mcdonalds-logo-current-1024x750.png/revision/latest?cb=20151128234130");
                                //placesThumbList.add("https://pbs.twimg.com/profile_images/804290535905259520/K6HVOG1O_400x400.jpg");
                                //placesThumbList.add("http://3.citynews-firenzetoday.stgy.ovh/~media/original-hi/67670779792462/image001-10.jpg");
                                //placesThumbList.add("http://visitoxfordms.com/wp-content/uploads/tacobell-logo1.jpg");

                                /*for (int i = 5; i <= 20; i++) {
                                    //placesTitleList.add("Place " + i);
                                    placesDetailList.add("Details " + i);
                                    placesThumbList.add("https://assets.epicurious.com/photos/57c5c6d9cf9e9ad43de2d96e/master/pass/the-ultimate-hamburger.jpg");
                                }*/

                            }
                            placesLists.add(placesTitleList);
                            placesLists.add(placesDetailList);
                            placesLists.add(placesThumbList);

                            //TODO get the context

                            placesRecyclerViewAdapter = new PlacesRecyclerViewAdapter(getContext(), placesLists);
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
