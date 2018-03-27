package com.project.rushabh.epicure.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.rushabh.epicure.R;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private Button toSignUpBtn, toLogInButton, toMainBtn;
    private TextView mainTitleText;
    private ImageView mainLogoImage;
    private Animation fadeInAnim, elevateAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        toSignUpBtn = findViewById(R.id.btn_to_signup);
        toLogInButton = findViewById(R.id.btn_to_login);
        toMainBtn = findViewById(R.id.btn_to_main);

        mainTitleText = findViewById(R.id.text_title_main);
        mainLogoImage = findViewById(R.id.image_main_logo);

        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.anim_fadein);
        elevateAnim = AnimationUtils.loadAnimation(this, R.anim.anim_elevate);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.shared_pref_file_name), MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sharedPreferences.getBoolean(getString(R.string.is_logged_in), false)) {
            //Toast.makeText(this, "You're not logged in", Toast.LENGTH_SHORT).show();
            mainLogoImage.startAnimation(elevateAnim);
            mainTitleText.startAnimation(elevateAnim);
            toSignUpBtn.startAnimation(fadeInAnim);
            toLogInButton.startAnimation(fadeInAnim);
            toMainBtn.startAnimation(fadeInAnim);
        } else {
            /*mainTitleText.startAnimation(steadyAnim);
            mainLogoImage.startAnimation(steadyAnim);
            toSignUpBtn.setVisibility(View.INVISIBLE);
            toLogInButton.setVisibility(View.INVISIBLE);
            toMainBtn.setVisibility(View.INVISIBLE);*/
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void onSplashClick(View view) {
        switch (view.getId()) {
            case R.id.btn_to_signup:
                break;
            case R.id.btn_to_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btn_to_main:
                finish();
                startActivity(new Intent(this, MainActivity.class));
        }
    }
}
