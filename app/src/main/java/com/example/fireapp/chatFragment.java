package com.example.fireapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fireapp.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
//used for tab1,tab2,tab3
public class chatFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_chat, container, false);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.bringToFront();
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new SectionsPagerAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}