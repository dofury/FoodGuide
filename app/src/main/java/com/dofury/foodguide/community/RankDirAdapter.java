package com.dofury.foodguide.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;

import java.util.List;

public class RankDirAdapter extends RecyclerView.Adapter<RankDirAdapter.ViewHolder> {
    private List<Food> foodList;

    public RankDirAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public RankDirAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_rank_item, parent, false);
        return new RankDirAdapter.ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull RankDirAdapter.ViewHolder holder, int position) {
        holder.tv_like.setText(String.valueOf(foodList.get(position).getLike()));
        holder.tv_name.setText(foodList.get(position).getName());
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
        }
    }
}
