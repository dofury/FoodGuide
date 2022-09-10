package com.dofury.foodguide.diary;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.R;
import com.dofury.foodguide.community.CommunityReadActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {
    private ArrayList<PostInfo> mDataset;
    private Fragment fragment;

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
        TextView titleTextView = cardView.findViewById(R.id.tv_diary_item_title);
        titleTextView.setText(mDataset.get(position).getTitle());

        TextView dateTextView = cardView.findViewById(R.id.tv_diary_item_date);
        dateTextView.setText(mDataset.get(position).getDate());

        ImageView foodImageView = cardView.findViewById(R.id.iv_diary_item_image);
        if(mDataset.get(position).getImage() != null) {
            Glide.with(this.fragment).load(mDataset.get(position).getImage()).into(foodImageView);
        }
        TextView contents = cardView.findViewById(R.id.tv_diary_item_contents);
        contents.setText(mDataset.get(position).getContents());
    }

    @Override
    public int getItemCount() {return mDataset.size();}

}
