package com.project.rushabh.restaurant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.rushabh.restaurant.R;

/**
 * Created by rushabh.modi on 04/04/18.
 *
 * A placeholder fragment containing a simple view.
 */

public class OrderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public OrderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static OrderFragment newInstance(int sectionNumber) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        TextView textView = rootView.findViewById(R.id.section_label);
        assert getArguments() != null; //To remove lint
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}

