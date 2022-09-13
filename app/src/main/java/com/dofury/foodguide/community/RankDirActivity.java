package com.dofury.foodguide.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;
import com.dofury.foodguide.inform.FoodInform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankDirActivity extends AppCompatActivity {
    List<Food> foodList = new ArrayList<>();
    RecyclerView rv_list;
    FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_dir);

        rv_list = findViewById(R.id.rv_list);
        rv_list.setHasFixedSize(true);
        RankDirAdapter rankDirAdapter = new RankDirAdapter(foodList, RankDirActivity.this, fragmentManager);
        rv_list.setAdapter(rankDirAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();

    }

    private void getList() {
        foodList.clear();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("FoodGuide").child("Food");
        dbRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    for(DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        Food food = new Food();

                        FoodInform foodInform =  dataSnapshot.child("foodInform").getValue(FoodInform.class);
                        food.setFoodInform(foodInform);
                        food.setId(dataSnapshot.child("id").getValue().toString());
                        food.setName(dataSnapshot.child("name").getValue().toString());
                        food.setImage(dataSnapshot.child("image").getValue().toString());
                        food.setComment(dataSnapshot.child("comment").getValue().toString());
                        String json = dataSnapshot.child("like").getValue().toString();

                        if(json.isEmpty()) {
                            food.setLike("[]");
                        } else {
                            food.setLike(json);
                        }
                        foodList.add(food);
                    }
                    Collections.sort(foodList);

                    for(int i=0; i<foodList.size(); i++){
                        int rank = 1;
                        List<String> list = new Gson().fromJson(foodList.get(i).getLike(), new TypeToken<List<String>>(){}.getType());
                        for(int j=0; j<foodList.size(); j++){
                            List<String> list2 = new Gson().fromJson(foodList.get(j).getLike(), new TypeToken<List<String>>(){}.getType());

                            if(list.size()<list2.size()){
                                rank++;
                            }
                        }
                        foodList.get(i).setRank(rank);
                    }

                    RankDirAdapter rankDirAdapter = new RankDirAdapter(foodList, RankDirActivity.this, fragmentManager);
                    rv_list.setAdapter(rankDirAdapter);
                }
            }
        });
    }

}