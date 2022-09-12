package com.dofury.foodguide.diary;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.Activity;
import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;
import com.dofury.foodguide.community.CommunityReadActivity;
import com.dofury.foodguide.foodComment.FoodComment;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {
    private ArrayList<PostInfo> mDataset;
    private Fragment fragment;
    private Activity activity;
    private Food food;
    private RelativeLayout loaderLayout;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    private final UserAccount userAccount = UserAccount.getInstance();
    String key;
    public static class DiaryViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        private TextView tv_delete;
        public DiaryViewHolder(CardView v){
            super(v);
            cardView = v;
            tv_delete = cardView.findViewById(R.id.tv_diary_item_delete);

        }
    }
    public DiaryAdapter(Fragment fragment, ArrayList<PostInfo> mDataset, Food food){
        this.mDataset = mDataset;
        this.fragment = fragment;
        this.food = food;
    }
    @NonNull
    @Override
    public DiaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_post_item,parent,false);
        final DiaryViewHolder diaryViewHolder = new DiaryViewHolder(cardView);

        return diaryViewHolder;
    }

    private void delete() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(fragment.getContext());
        dialog.setTitle("다이어리 삭제");
        dialog.setMessage("정말로 다이어리를 삭제하시겠습니까?");
        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(fragment.getContext(), "글을 삭제하고 있습니다.", Toast.LENGTH_SHORT).show();
                databaseReference.child("UserAccount").child(userAccount.getIdToken()).child("food").child(food.getId()).child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(fragment.getContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.setNegativeButton("아니요", null);
        dialog.show();
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        holder.tv_delete.setOnClickListener(view -> {
            databaseReference.child("UserAccount").child(userAccount.getIdToken()).child("food").child(food.getId()).child(key).get().addOnCompleteListener(task -> {
                delete();

            });
        });
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
        key = mDataset.get(position).getId();

    }

    @Override
    public int getItemCount() {return mDataset.size();}

}
