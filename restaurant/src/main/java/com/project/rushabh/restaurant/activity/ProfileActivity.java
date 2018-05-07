package com.project.rushabh.restaurant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.rushabh.restaurant.R;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView emailText;
    private EditText nameEditText, infoEditText, addressEditText, phoneEditText;
    private Button saveBtn, logoutBtn;
    private Map<String, Object> profileMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(getString(R.string.shared_pref_file_name), Context.MODE_PRIVATE);
        profileMap = new HashMap<>();

        emailText = findViewById(R.id.editText_profile_email);
        nameEditText = findViewById(R.id.editText_profile_name);
        infoEditText = findViewById(R.id.editText_profile_info);
        addressEditText = findViewById(R.id.editText_profile_address);
        phoneEditText = findViewById(R.id.editText_profile_phone);
        saveBtn = findViewById(R.id.btn_profile_save);
        logoutBtn = findViewById(R.id.btn_profile_logout);

        emailText.setText(sharedPreferences.getString(getString(R.string.spk_email),""));
        nameEditText.setText(sharedPreferences.getString(getString(R.string.spk_name),""));
        infoEditText.setText(sharedPreferences.getString(getString(R.string.spk_information),""));
        addressEditText.setText(sharedPreferences.getString(getString(R.string.spk_address),""));
        phoneEditText.setText(sharedPreferences.getString(getString(R.string.spk_contact),""));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileMap.put("name", nameEditText.getText().toString());
                profileMap.put("information", infoEditText.getText().toString());
                profileMap.put("address", addressEditText.getText().toString());
                profileMap.put("contact", phoneEditText.getText().toString());
                db.collection("restaurants").document(sharedPreferences.getString(getString(R.string.spk_restaurant_id),""))
                        .update(profileMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    editor = sharedPreferences.edit();
                                    editor.putString(getString(R.string.spk_name),nameEditText.getText().toString());
                                    editor.putString(getString(R.string.spk_information),infoEditText.getText().toString());
                                    editor.putString(getString(R.string.spk_address),addressEditText.getText().toString());
                                    editor.putString(getString(R.string.spk_contact),phoneEditText.getText().toString());
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
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
                editor.putBoolean(getString(R.string.spk_is_logged_in), false).apply();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
