package com.ynot.jomgaa.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ynot.jomgaa.View.ViewPager1;
import com.ynot.jomgaa.View.ViewPager2;

public class PhotosAdapter extends FragmentStatePagerAdapter {
    public PhotosAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ViewPager1();
            case 1:
                return new ViewPager2();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

