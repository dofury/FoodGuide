package com.dofury.foodguide.diary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.Activity;
import com.dofury.foodguide.R;
import com.dofury.foodguide.community.CommunityReadActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {
    private ArrayList<PostInfo> mDataset;
    private Fragment fragment;
    private Activity activity;

    public static class DiaryViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public DiaryViewHolder(CardView v){
            super(v);
            cardView = v;

        }
    }
    public DiaryAdapter(Fragment fragment,ArrayList<PostInfo> mDataset){
        this.mDataset = mDataset;
        this.fragment = fragment;
    }
    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_post_item,parent,false);
        final DiaryViewHolder diaryViewHolder = new DiaryViewHolder(cardView);
        return diaryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        TextView dateTextView = cardView.findViewById(R.id.tv_diary_item_date);
        dateTextView.setText(mDataset.get(position).getDate());

        RatingBar ratingBar = cardView.findViewById(R.id.diary_item_rating);
        ratingBar.setRating(mDataset.get(position).getRating());

        ImageView foodImageView = cardView.findViewById(R.id.iv_diary_item_image);
        if(mDataset.get(position).getImage() != null) {
            Glide.with(this.fragment).load(mDataset.get(position).getImage()).into(foodImageView);
        }
        TextView contents = cardView.findViewById(R.id.tv_diary_item_contents);
        contents.setText(mDataset.get(position).getContents());
        FoodDtDiary foodDtDiary = FoodDtDiary.newInstance(mDataset.get(position).getId());
    }

    @Override
    public int getItemCount() {return mDataset.size();}

}
