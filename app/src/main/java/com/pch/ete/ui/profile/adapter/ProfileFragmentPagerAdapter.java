package com.pch.ete.ui.profile.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list = new ArrayList<>();
    private List<String> titleList=new ArrayList<>();

    public ProfileFragmentPagerAdapter(FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addFragment(Fragment fragment) {
        list.add(fragment);
    }

    public void setTitle(String title) {
        titleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(titleList.size()>0)
            return titleList.get(position);
        else
            return "";
    }
}
