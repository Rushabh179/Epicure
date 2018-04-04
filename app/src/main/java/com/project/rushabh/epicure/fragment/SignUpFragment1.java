package com.project.rushabh.epicure.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rushabh.modi on 27/03/18.
 */

// A placeholder fragment containing a simple view.
public class SignUpFragment1 extends Fragment {

    // The fragment argument representing the section number for this fragment.
    private static final String ARG_FRAGMENT_RESOURCE = "fragment_resourse";
    private static final String ARG_SECTION_NUMBER = "section_number";
    public View view;
    OnGetAccountViewListener onGetViewListener;

    public SignUpFragment1() {
    }

    // Returns a new instance of this fragment for the given section number.
    public static SignUpFragment1 newInstance(int fragmentResource, int sectionNumber) {
        SignUpFragment1 fragment = new SignUpFragment1();
        Bundle args = new Bundle();
        args.putInt(ARG_FRAGMENT_RESOURCE, fragmentResource);
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onGetViewListener = (OnGetAccountViewListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = null;
        if (getArguments() != null) {
            rootView = inflater.inflate(getArguments().getInt(ARG_FRAGMENT_RESOURCE), container, false);
        }
        assert rootView != null;
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onGetViewListener.getAccountView(view);
    }

    public interface OnGetAccountViewListener {
        void getAccountView(View view);
    }
}
