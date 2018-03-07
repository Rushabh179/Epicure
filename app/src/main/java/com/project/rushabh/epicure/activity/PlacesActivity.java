package com.project.rushabh.epicure.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.adapter.PlacesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlacesActivity extends AppCompatActivity {

    private RecyclerView placesRecyclerView;
    private PlacesRecyclerViewAdapter placesRecyclerViewAdapter;
    private List<String> placesTitleList, placesDetailList, placesThumbList;
    private DividerItemDecoration decoration;
    private List<List<String>> placesLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        placesRecyclerView = findViewById(R.id.recyclerview_main);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        placesRecyclerView.addItemDecoration(decoration);

        placesTitleList = new ArrayList<>();
        placesDetailList = new ArrayList<>();
        placesThumbList = new ArrayList<>();
        placesLists = new ArrayList<>();

        placesTitleList.add("McDonald's");
        placesTitleList.add("KFC");
        placesTitleList.add("Burger King");
        placesTitleList.add("Taco Bell");

        placesDetailList.add("Fast food");
        placesDetailList.add("Fast food");
        placesDetailList.add("Fast food");
        placesDetailList.add("Mexican");

        placesThumbList.add("https://vignette.wikia.nocookie.net/ronaldmcdonald/images/b/b5/Mcdonalds-logo-current-1024x750.png/revision/latest?cb=20151128234130");
        placesThumbList.add("https://pbs.twimg.com/profile_images/804290535905259520/K6HVOG1O_400x400.jpg");
        placesThumbList.add("http://3.citynews-firenzetoday.stgy.ovh/~media/original-hi/67670779792462/image001-10.jpg");
        placesThumbList.add("http://visitoxfordms.com/wp-content/uploads/tacobell-logo1.jpg");

        for (int i = 5; i <= 20; i++) {
            placesTitleList.add("Place " + i);
            placesDetailList.add("Details " + i);
            placesThumbList.add("https://assets.epicurious.com/photos/57c5c6d9cf9e9ad43de2d96e/master/pass/the-ultimate-hamburger.jpg");
        }

        placesLists.add(placesTitleList);
        placesLists.add(placesDetailList);
        placesLists.add(placesThumbList);

        placesRecyclerViewAdapter = new PlacesRecyclerViewAdapter(this, placesLists);
        placesRecyclerView.setAdapter(placesRecyclerViewAdapter);

    }
}
