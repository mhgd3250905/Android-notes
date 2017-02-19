package com.immoc.ww.greendaodemo.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by admin on 2016/11/28.
 */
/*
* 
* 描    述：ViewPager Adapter
* 作    者：ksheng
* 时    间：2016/11/28$ 21:57$.
*/

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;
    private String[] TITLE;

    public MyPagerAdapter(android.support.v4.app.FragmentManager fm, List<Fragment> fragments, String[] TITLE) {

        super(fm);
        this.TITLE = TITLE;
        this.fragments = fragments;

    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }

}

