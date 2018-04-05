package com.project.rushabh.restaurant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.rushabh.restaurant.R;

/**
 * Created by rushabh.modi on 05/04/18.
 */

public class ManageContainerFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public ManageContainerFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static ManageContainerFragment newInstance(int sectionNumber) {
        ManageContainerFragment fragment = new ManageContainerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_container, container, false);
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, new CategoryFragment());
        fragmentTransaction.commit(); // save the changes
        return rootView;
    }
}
