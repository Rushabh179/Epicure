package com.project.rushabh.epicure.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.test.SignUpSectionPagerAdaper;

public class SignUpActivity extends AppCompatActivity {

    private SignUpSectionPagerAdaper sectionsPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test_activity_signup);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        sectionsPagerAdapter = new SignUpSectionPagerAdaper(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById(R.id.viewpager_sign_up_test);
        viewPager.setAdapter(sectionsPagerAdapter);
    }
}
