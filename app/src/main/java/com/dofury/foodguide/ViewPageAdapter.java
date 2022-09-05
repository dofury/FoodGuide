package com.dofury.foodguide;

import android.app.FragmentManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class ViewPageAdapter extends FragmentStateAdapter{
    private int mPageCount = 3;
    public ViewPageAdapter(AppCompatActivity fm) {
        super(fm);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                FoodDetailPage1 foodDetailPage1 = new FoodDetailPage1();
                return foodDetailPage1;
            case 1:
                FoodDetailPage2 foodDetailPage2 = new FoodDetailPage2();
                return foodDetailPage2;
            case 2:
                FoodDetailPage3 foodDetailPage3 = new FoodDetailPage3();
                return foodDetailPage3;
            default:
                return null;
        }
    }

    @Override
    public long getItemId(int position)
    {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mPageCount;
    }
}
