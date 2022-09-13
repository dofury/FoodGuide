package com.dofury.foodguide.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.Activity;
import com.dofury.foodguide.DetailFood;
import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class RankDirAdapter extends RecyclerView.Adapter<RankDirAdapter.ViewHolder> {
    private List<Food> foodList;
    private Context context;
    private FragmentManager fragmentManager;
    public RankDirAdapter(List<Food> foodList, Context context, FragmentManager fragmentManager) {
        this.foodList = foodList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public RankDirAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_rank_item, parent, false);
        return new RankDirAdapter.ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull RankDirAdapter.ViewHolder holder, int position) {
        List<String> list = new Gson().fromJson(foodList.get(position).getLike(), new TypeToken<List<String>>(){}.getType());
        holder.tv_like.setText(String.valueOf(list.size()));
        holder.tv_name.setText(foodList.get(position).getName());
        holder.tv_rank.setText(String.valueOf(foodList.get(position).getRank()));
        Glide.with(context).load(foodList.get(position).getImage()).into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_rank, tv_name, tv_like;
        private ImageView iv_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv_image = itemView.findViewById(R.id.iv_image);
            tv_like = itemView.findViewById(R.id.tv_like);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                // 랭크 클릭
                Intent intent = new Intent(context, Activity.class);
                intent.putExtra("foodNameForRank", String.valueOf(foodList.get(pos).getName()));
                context.startActivity(intent);
            });
        }
    }
}
