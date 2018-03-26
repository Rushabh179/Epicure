package com.project.rushabh.epicure.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.project.rushabh.epicure.R;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (!sharedPreferences.getBoolean(getString(R.string.is_logged_in), false)){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }*/
    }

    public void onSplashClick(View view) {
        switch (view.getId()) {
            case R.id.btn_to_signup:
                break;
            case R.id.btn_to_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btn_without_login:
                finish();
                startActivity(new Intent(this, MainActivity.class));
        }
    }
}
