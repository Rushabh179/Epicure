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
    private List<String> placesTitle;
    private DividerItemDecoration decoration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesRecyclerView = findViewById(R.id.recyclerview_main);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        placesRecyclerView.addItemDecoration(decoration);

        placesTitle = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            placesTitle.add("Place " + i);
        }

        placesRecyclerViewAdapter = new PlacesRecyclerViewAdapter(this, placesTitle);
        placesRecyclerView.setAdapter(placesRecyclerViewAdapter);

    }
}
