package com.dofury.foodguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FoodAdapter extends ArrayAdapter<Food> {

    public FoodAdapter(Context context, int resource, List<Food> foodList){

        super(context,resource,foodList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        Food food = getItem(position);

        if(convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_item, parent, false);
        }

        TextView tv = convertView.findViewById(R.id.food_name);
        ImageView iv = convertView.findViewById(R.id.food_image);

        tv.setText(food.getName());
        iv.setImageResource(food.getImage());

        return convertView;
    }
}
