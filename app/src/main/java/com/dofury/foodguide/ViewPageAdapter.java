package com.dofury.foodguide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> items;
    private ArrayList<String> itext = new ArrayList<String>();
    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
        items = new ArrayList<Fragment>();
        items.add(new FoodDetailPage1());
        items.add(new FoodDetailPage2());
        items.add(new FoodDetailPage3());

        itext.add("정보");
        itext.add("아이");
        itext.add("후기");
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return itext.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
