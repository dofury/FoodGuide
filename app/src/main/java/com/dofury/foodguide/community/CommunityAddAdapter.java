package com.dofury.foodguide.community;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private ArrayList<String> textList;
    private int position;
    private SharedPreferences sharedPreferences;

    public class CommunityViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        EditText editText;
        TextView cancel;
        public CommunityViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_post_item);
            editText = itemView.findViewById(R.id.ev_post_item);
            cancel = itemView.findViewById(R.id.tv_post_item_cancel);
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
    CommunityAddAdapter(ArrayList<String> textList,AppCompatActivity appCompatActivity){
        this.appCompatActivity = appCompatActivity;
        this.textList = textList;
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
    public void onBindViewHolder(@NonNull CommunityViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(list != null) {
            Glide.with(this.appCompatActivity).load(list.get(position)).into(holder.imageView);
        }
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = "imageText" + String.valueOf(position);
                String imageText = holder.editText.getText().toString();
                sharedPreferences = appCompatActivity.getSharedPreferences("spText", 0);
                // 자동 로그인 설정
                SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(temp, imageText);
                    editor.commit();
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,list.size());
                //((ViewGroup) v.getParent()).removeView(v);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
