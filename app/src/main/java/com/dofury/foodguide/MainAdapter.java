package com.dofury.foodguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private ArrayList<String> mData = null ;

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MainViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_main_item_image);
        }

    }
    MainAdapter(ArrayList<String> list){
        this.mData = list;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.main_item, parent, false) ;
        MainAdapter.MainViewHolder vh = new MainAdapter.MainViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String text = mData.get(position) ;
        //if(mData.get(position).getImage() != null) {
            //Glide.with(this).load(mData.get(position).getImage()).into(holder.imageView);
        //}
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}
