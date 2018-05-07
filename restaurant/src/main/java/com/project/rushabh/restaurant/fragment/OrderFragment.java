package com.project.rushabh.restaurant.fragment;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import com.project.rushabh.restaurant.R;
import com.project.rushabh.restaurant.activity.MainActivity;
import com.project.rushabh.restaurant.adapter.OrderRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rushabh.modi on 04/04/18.
 * <p>
 * A placeholder fragment containing a simple view.
 */

public class OrderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private RecyclerView orderRecyclerView;
    private OrderRecyclerAdapter orderRecyclerAdapter;

    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;

    private List<String> orderIdList, senderEmailList, timeStampList, itemCountList, statusList;
    private List<List<Map<String, Object>>> itemList;

    private NotificationCompat.Builder pushNotificationBuilder;
    private NotificationManagerCompat pushNotificationManager;
    private PendingIntent pushPendingIntent;
    private Intent intent;
    private int uniqueID = 99;

    public OrderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static OrderFragment newInstance(int sectionNumber) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        assert getContext() != null;
        sharedPreferences = getContext().getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order, container, false);
        orderRecyclerView = rootView.findViewById(R.id.recyclerView_order);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL));
        orderIdList = new ArrayList<>();
        senderEmailList = new ArrayList<>();
        timeStampList = new ArrayList<>();
        itemCountList = new ArrayList<>();
        itemList = new ArrayList<>();
        statusList = new ArrayList<>();
        db.collection("orders")
                //.whereEqualTo("receiverFirebaseId", sharedPreferences.getString(getString(R.string.spk_restaurant_id), ""))
                .orderBy("timeStamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            loadOrders(task.getResult());
                        }
                    }
                });

        /*db.collection("orders")
                .whereEqualTo("receiverFirebaseId", sharedPreferences.getString(getString(R.string.spk_restaurant_id), ""))
                .whereEqualTo("status", "New")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(getContext(), "Error getting the order", Toast.LENGTH_SHORT).show();
                        } else {
                            getNotification();
                        }
                    }
                });*/
        return rootView;
    }

    private void loadOrders(QuerySnapshot queryDocumentSnapshots){
        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            orderIdList.add(document.getId());
            senderEmailList.add(document.getString("senderEmail"));
            timeStampList.add(document.getDate("timeStamp").toString());
            itemList.add((List<Map<String, Object>>) document.get("item"));
            statusList.add(document.getString("status"));
            //Toast.makeText(MainActivity.this, doc.getId(), Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < itemList.size(); i++)
            itemCountList.add(Integer.toString(itemList.get(i).size())+" items");
        //orderRecyclerAdapter.notifyDataSetChanged();
        orderRecyclerAdapter = new OrderRecyclerAdapter(getContext(), orderIdList, senderEmailList, timeStampList, itemCountList, statusList);
        orderRecyclerView.setAdapter(orderRecyclerAdapter);
    }

    private void getNotification() {
        intent = new Intent(getContext(), MainActivity.class);
        pushPendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        pushNotificationBuilder = new NotificationCompat.Builder(rootView.getContext(), "com.project.rushabh.restaurant.fragment");
        pushNotificationBuilder.setAutoCancel(true)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setTicker("New order")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText("New Order"))
                .setContentTitle("New Order")
                .setContentText("New Order")
                .addAction(android.R.drawable.arrow_up_float, "Open", pushPendingIntent)
                .setContentIntent(pushPendingIntent);

        //Issue notification
        pushNotificationManager = NotificationManagerCompat.from(rootView.getContext());
        pushNotificationManager.notify(uniqueID, pushNotificationBuilder.build());
    }
}
