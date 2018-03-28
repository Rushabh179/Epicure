package com.project.rushabh.epicure.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.test.SignUpSectionPagerAdaper;
import com.project.rushabh.epicure.util.NonSwipeableViewPager;

public class SignUpActivity extends AppCompatActivity {

    private SignUpSectionPagerAdaper sectionsPagerAdapter;
    private NonSwipeableViewPager viewPager;

    private TextView subTitleText, nextText, previousText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test_activity_signup);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        sectionsPagerAdapter = new SignUpSectionPagerAdaper(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById(R.id.viewpager_sign_up);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(1);//to load all the pages together

        subTitleText = findViewById(R.id.text_signup_sub_title);
        nextText = findViewById(R.id.text_signup_next);
        previousText = findViewById(R.id.text_signup_previous);

        previousText.setVisibility(View.INVISIBLE);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        subTitleText.setText(getString(R.string.sign_up_subtitle_1));
                        previousText.setVisibility(View.INVISIBLE);
                        nextText.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        subTitleText.setText(getString(R.string.sign_up_subtitle_2));
                        previousText.setVisibility(View.VISIBLE);
                        nextText.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public void onSignUpClick(View view) {
        switch (view.getId()) {
            case R.id.text_signup_next:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                break;
            case R.id.text_signup_previous:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                break;
            case R.id.text_title_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
        }
    }

}
