package com.dofury.foodguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.inform.FoodInform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FoodAdapter extends ArrayAdapter<Food> {
    private ArrayList<Food> foodList;
    public FoodAdapter(Context context, int resource, ArrayList<Food> foodList){

        super(context,resource,foodList);
        this.foodList = foodList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Food food = foodList.get(position);


        if(convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_item, parent, false);
        }

        TextView tv = convertView.findViewById(R.id.food_name);
        ImageView iv = convertView.findViewById(R.id.food_image);

        tv.setText(food.getName());

        Glide.with(getContext()).load(food.getImage()).into(iv);

        return convertView;
    }
}
