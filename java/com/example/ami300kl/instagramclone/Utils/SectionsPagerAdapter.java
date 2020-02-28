package com.example.ami300kl.instagramclone.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ami300kl on 26. 11. 2018.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private static String TAG ="Sections Pager Adapter";
    private  final List <Fragment> mFragmentList= new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment Fragment)
    {
        mFragmentList.add(Fragment);
    }
}
