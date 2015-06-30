package com.example.randy.scrollselecotr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.randy.scrollselecotr.R.*;


/**
 * Created by jorge on 31/07/14.
 */
public class FirstHeaderFragment extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(layout.fragment_header_first, container, false);

        return rootView;
    }
}
