package com.project.rushabh.epicure.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rushabh.modi on 27/03/18.
 */

// A placeholder fragment containing a simple view.
public class SignUpPlaceHolderFragment extends Fragment {

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_FRAGMENT_RESOURCE = "fragment_resourse";
    private static final String ARG_SECTION_NUMBER = "section_number";

    public SignUpPlaceHolderFragment() {
    }

    // Returns a new instance of this fragment for the given section number.
    public static SignUpPlaceHolderFragment newInstance(int fragmentResource, int sectionNumber) {
        SignUpPlaceHolderFragment fragment = new SignUpPlaceHolderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FRAGMENT_RESOURCE, fragmentResource);
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        if (getArguments() != null) {
            rootView = inflater.inflate(getArguments().getInt(ARG_FRAGMENT_RESOURCE), container, false);
        }
        return rootView;
    }
}
