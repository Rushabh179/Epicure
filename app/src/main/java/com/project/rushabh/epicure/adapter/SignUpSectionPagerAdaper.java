package com.project.rushabh.epicure.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.project.rushabh.epicure.R;
import com.project.rushabh.epicure.fragment.SignUpFragment1;
import com.project.rushabh.epicure.fragment.SignUpFragment3;
import com.project.rushabh.epicure.fragment.SignUpFragment2;

/**
 * Created by rushabh.modi on 27/03/18.
 */

public class SignUpSectionPagerAdaper extends FragmentPagerAdapter {

    public SignUpSectionPagerAdaper(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SignUpFragment1.newInstance(R.layout.fragment_sign_up_1, position);
            case 1:
                return SignUpFragment2.newInstance(R.layout.fragment_sign_up_2, position);
            case 2:
                return SignUpFragment3.newInstance(R.layout.fragment_sign_up_3, position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
