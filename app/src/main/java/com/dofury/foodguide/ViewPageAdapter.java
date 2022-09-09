package com.dofury.foodguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dofury.foodguide.diary.FoodDtDiary;
import com.google.firebase.database.annotations.NotNull;

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
                FoodDtInformation foodDetailPage1 = new FoodDtInformation();
                return foodDetailPage1;
            case 1:
                FoodDtDiary foodDetailPage2 = new FoodDtDiary();
                return foodDetailPage2;
            case 2:
                FoodDtComment foodDetailPage3 = new FoodDtComment();
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
