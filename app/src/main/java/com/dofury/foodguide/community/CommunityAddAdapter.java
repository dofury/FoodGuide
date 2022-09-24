package com.dofury.foodguide.community;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.Activity;
import com.dofury.foodguide.EditProfileActivity;
import com.dofury.foodguide.R;
import com.dofury.foodguide.SettingItem;
import com.dofury.foodguide.diary.DiaryPost;

import java.util.ArrayList;

public class CommunityAddAdapter extends RecyclerView.Adapter<CommunityAddAdapter.CommunityViewHolder> {

    private AppCompatActivity appCompatActivity;
    private ArrayList<Uri> list;
    private int position;

    public class CommunityViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        EditText editText;
        public CommunityViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_post_item);
            editText = itemView.findViewById(R.id.ev_post_item);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();

                // 랭크 클릭;
            });


        }

    }
    CommunityAddAdapter(AppCompatActivity appCompatActivity, ArrayList<Uri> list){
        this.appCompatActivity = appCompatActivity;
        this.list = list;
    }

    @NonNull
    @Override
    public CommunityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.post_item, parent, false) ;
        CommunityAddAdapter.CommunityViewHolder vh = new CommunityAddAdapter.CommunityViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, int position) {
        if(list != null) {
            Glide.with(this.appCompatActivity).load(list.get(position)).into(holder.imageView);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
