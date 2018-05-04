package com.project.rushabh.epicure.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.activity.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rushabh on 04-May-18.
 */

public class ProfileFragment extends Fragment {

    private View rootView;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView emailText;
    private EditText nameEditText, addressEditText, phoneEditText;
    private Button saveBtn, logoutBtn;
    private Map<String, Object> profileMap;

    public ProfileFragment() {
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
        profileMap = new HashMap<>();
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        emailText = rootView.findViewById(R.id.editText_profile_email);
        nameEditText = rootView.findViewById(R.id.editText_profile_name);
        addressEditText = rootView.findViewById(R.id.editText_profile_address);
        phoneEditText = rootView.findViewById(R.id.editText_profile_phone);
        saveBtn = rootView.findViewById(R.id.btn_profile_save);
        logoutBtn = rootView.findViewById(R.id.btn_profile_logout);
        emailText.setText(sharedPreferences.getString(getString(R.string.spk_email),""));
        nameEditText.setText(sharedPreferences.getString(getString(R.string.spk_name),""));
        addressEditText.setText(sharedPreferences.getString(getString(R.string.spk_address),""));
        phoneEditText.setText(sharedPreferences.getString(getString(R.string.spk_contact),""));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileMap.put("name", nameEditText.getText().toString());
                profileMap.put("address", addressEditText.getText().toString());
                profileMap.put("contact", phoneEditText.getText().toString());
                db.collection("user").document(sharedPreferences.getString(getString(R.string.spk_user_id),""))
                        .update(profileMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    editor.putString(getString(R.string.spk_name),nameEditText.getText().toString());
                                    editor.putString(getString(R.string.spk_email),emailText.getText().toString());
                                    editor.putString(getString(R.string.spk_contact),phoneEditText.getText().toString());
                                    Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = sharedPreferences.edit();
                FirebaseAuth.getInstance().signOut();
                editor.putBoolean(getString(R.string.is_logged_in), false).apply();
                startActivity(new Intent(getContext(), LoginActivity.class));

            }
        });

        return rootView;
    }
}
