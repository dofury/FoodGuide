package com.dofury.foodguide.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankDirActivity extends AppCompatActivity {
    List<Food> foodList = new ArrayList<>();
    RecyclerView rv_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_dir);

        rv_list = findViewById(R.id.rv_list);
        rv_list.setHasFixedSize(true);
        RankDirAdapter rankDirAdapter = new RankDirAdapter(foodList);
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

                        food.setName(dataSnapshot.child("name").getValue().toString());
                        String json = dataSnapshot.child("like").getValue().toString();

                        if(json.isEmpty()) {
                            food.setLike("[]");
                        } else {
                            food.setLike(json);
                        }
                        foodList.add(food);
                    }
                    Collections.sort(foodList);

                    int rank = 1;
                    for(int i=0; i<foodList.size()-1; i++) {
                        boolean flag = false;
                        int tmp = 0;
                        for(int u=i; u<foodList.size()-1; u++) {
                            List<String> list = new Gson().fromJson(foodList.get(u).getLike(), new TypeToken<List<String>>(){}.getType());
                            List<String> list2 = new Gson().fromJson(foodList.get(u+1).getLike(), new TypeToken<List<String>>(){}.getType());
                            if(list.size() > list2.size()) {
                                foodList.get(u).setRank(rank);
                                foodList.get(u+1).setRank(rank+1);

                                break;
                            } else if (list.size() == list2.size()) {
                                flag = true;
                                tmp++;
                            }
                            else {
                                break;
                            }
                        }
                        if(flag) {
                            for(int j = i; j < tmp + i + 1; j++) {
                                foodList.get(j).setRank(tmp);
                            }
                        }
                        i += tmp;
                        rank++;
                    }

                    RankDirAdapter rankDirAdapter = new RankDirAdapter(foodList);
                    rv_list.setAdapter(rankDirAdapter);
                }
            }
        });
    }

}