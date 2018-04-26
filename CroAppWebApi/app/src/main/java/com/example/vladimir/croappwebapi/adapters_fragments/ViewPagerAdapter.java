package com.example.vladimir.croappwebapi.adapters_fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.vladimir.croappwebapi.R;

/**
 * Created by Vladimir on 3/23/2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new FragmentOne();
            case 1:
                return new FragmentTwo();
            case 2:
                return new FragmentThree();
            default:
                return  null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.category_ask);
            case 1:
                return mContext.getString(R.string.category_contacts);
            case 2:
                return mContext.getString(R.string.category_awards);
            default:
                return null;
        }
    }
}
