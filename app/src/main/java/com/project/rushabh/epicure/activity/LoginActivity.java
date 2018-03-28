package com.project.rushabh.epicure.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.project.rushabh.epicure.R;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), MODE_PRIVATE);
    }

    public void onLoginClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                editor = sharedPreferences.edit();
                editor.putBoolean(getString(R.string.is_logged_in), true).apply();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.text_to_signup:
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
