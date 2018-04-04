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
import android.widget.Toast;

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
 * Created by rushabh.modi on 04/04/18.
 */

public class ManageFragment extends Fragment implements OnRecyclerClickListener{

    private RecyclerView manageRecyclerView;
    private ManageRecyclerAdapter manageRecyclerAdapter;
    private List<String> items;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    private OnRecyclerClickListener onRecyclerClickListener;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public ManageFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static ManageFragment newInstance(int sectionNumber) {
        ManageFragment fragment = new ManageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        onRecyclerClickListener = this;
        collectionReference = db.collection("restaurants").document("BJSdynFnNrQbGXmX7iMp").collection("category");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage, container, false);
        manageRecyclerView = rootView.findViewById(R.id.recyclerView_manage);
        manageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        manageRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), DividerItemDecoration.VERTICAL));
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
                            manageRecyclerAdapter = new ManageRecyclerAdapter(items);
                            manageRecyclerAdapter.setOnRecyclerClickListener(onRecyclerClickListener);
                            manageRecyclerView.setAdapter(manageRecyclerAdapter);
                        }
                    }
                });
        return rootView;
    }

    @Override
    public void onRecyclerClick(View view, int position) {
        switch (position) {
            case 0:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(this).attach(this).commit();
                /*SubCategoryFragment fragment2 = new SubCategoryFragment();
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.attach(fragment2);
                //fragmentTransaction.replace(R.id.container, fragment2);
                fragmentTransaction.commit();*/
        }
    }
}
