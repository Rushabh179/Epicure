package com.project.rushabh.delivery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private RecyclerView orderRecyclerView;
    private List<String> orderIdList, placeNameList, senderEmailList, timeStampList, itemCountList, statusList;
    private List<List<Map<String, Object>>> itemList;
    private OrderAdapter orderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(getString(R.string.shared_pref_file_name), MODE_PRIVATE);
        orderRecyclerView = findViewById(R.id.recyclerView_order);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        orderIdList = new ArrayList<>();
        placeNameList = new ArrayList<>();
        senderEmailList = new ArrayList<>();
        timeStampList = new ArrayList<>();
        itemCountList = new ArrayList<>();
        itemList = new ArrayList<>();
        statusList = new ArrayList<>();

        db.collection("orders")
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                //.whereEqualTo("senderFirebaseId", sharedPreferences.getString(getString(R.string.spk_user_id), ""))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (Objects.equals(document.getString("deliverFirebaseId"),
                                        sharedPreferences.getString(getString(R.string.spk_deliver_id), ""))) { //Todo: Find a proper method
                                    orderIdList.add(document.getId());
                                    placeNameList.add(document.getString("restaurantName"));
                                    senderEmailList.add(document.getString("senderEmail"));
                                    timeStampList.add(document.getDate("timeStamp").toString());
                                    itemList.add((List<Map<String, Object>>) document.get("item"));
                                    statusList.add(document.getString("status"));
                                }
                            }
                            for (int i = 0; i < itemList.size(); i++)
                                itemCountList.add(Integer.toString(itemList.get(i).size()) + " items");
                            //Toast.makeText(getContext(), i + Integer.toString(itemList.get(i).size()), Toast.LENGTH_SHORT).show();
                            orderAdapter = new OrderAdapter(getApplicationContext(), orderIdList, placeNameList, senderEmailList, timeStampList, itemCountList, statusList);
                            orderRecyclerView.setAdapter(orderAdapter);
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }

       /* if (id == R.id.notification_test) {
            getNotification();
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void getNotification() {

    }
}
