package com.project.rushabh.epicure.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by rushabh.modi on 29/03/18.
 */

public class SignUpFragment3 extends Fragment {
    private static final String ARG_FRAGMENT_RESOURCE = "fragment_resourse";
    private static final String ARG_SECTION_NUMBER = "section_number";

    OnGetWelocomeViewListener onGetViewListener;

    public SignUpFragment3() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onGetViewListener = (OnGetWelocomeViewListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    // Returns a new instance of this fragment for the given section number.
    public static SignUpFragment3 newInstance(int fragmentResource, int sectionNumber) {
        SignUpFragment3 fragment = new SignUpFragment3();
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
        assert rootView != null;
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onGetViewListener.getWelocomeView(view);
    }

    public interface OnGetWelocomeViewListener {
        void getWelocomeView(View view);
    }
}
