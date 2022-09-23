package com.dofury.foodguide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.diary.DiaryPost;

import java.util.ArrayList;
import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {

    private Fragment fragment;
    private ArrayList<SettingItem> list;
    private int position;

    public class SettingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView1,textView2;
        public SettingViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_setting_item);
            textView1 = itemView.findViewById(R.id.tv_setting_item1);
            textView2 = itemView.findViewById(R.id.tv_setting_item2);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                switch(pos)
                {
                    case 0:
                        Intent intent1 = new Intent(fragment.getContext(), EditProfileActivity.class);
                        fragment.getContext().startActivity(intent1);
                        break;
                    default:
                        Intent intent2 = new Intent(fragment.getContext(), DiaryPost.class);
                        fragment.getContext().startActivity(intent2);

                }
                // 랭크 클릭;
            });


        }

    }
    SettingAdapter(Fragment fragment,ArrayList<SettingItem> list){
        this.fragment = fragment;
        this.list = list;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.setting_item, parent, false) ;
        SettingAdapter.SettingViewHolder vh = new SettingAdapter.SettingViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        holder.textView1.setText(list.get(position).getTitle());
        holder.textView2.setText(list.get(position).getContent());
        holder.imageView.setImageResource(list.get(position).getImage());
        holder.imageView.setMaxHeight(300);
        holder.imageView.setMaxWidth(300);
        switch(position)
        {
            case 0:
                holder.imageView.setColorFilter(ContextCompat.getColor(fragment.getContext(),R.color.sky1));
                break;
            case 1:
                holder.imageView.setColorFilter(ContextCompat.getColor(fragment.getContext(),R.color.orange1));
                break;
            case 2:
                holder.imageView.setColorFilter(ContextCompat.getColor(fragment.getContext(),R.color.food1));
                break;
            case 3:
                holder.imageView.setColorFilter(ContextCompat.getColor(fragment.getContext(),R.color.gray1));
                break;

        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
