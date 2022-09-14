package com.dofury.foodguide;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dofury.foodguide.inform.FoodInform;
import com.dofury.foodguide.login.LoginActivity;
import com.dofury.foodguide.login.UserAccount;
import com.dofury.foodguide.diary.PostInfo;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Main extends Fragment implements TextSetAble {
    private View view;
    SearchView searchView;
    Context context;
    private FirebaseDatabase firebaseDatabase;
    UserAccount userAccount = UserAccount.getInstance();
    String preFrag;
    Fragment fragment;
    List<String> finalFoodLogs;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    public static Main newInstance(){
        return new Main();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        context = this.getContext();
        fragment = this;


        searchView = view.findViewById(R.id.food_search);
        searchView.setOnQueryTextListener(new MyTextQueryListener(this));
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);//전체영역 터치해도 검색반응
            }

        });

        //dataSet();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataSet();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dataSet(){
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<Food> list = new ArrayList<>();
        finalFoodLogs = new ArrayList<>();
        databaseReference.child("UserAccount").child(userAccount.getIdToken()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(userAccount.getFoodLogs() != null) finalFoodLogs = new Gson().fromJson(userAccount.getFoodLogs(), new TypeToken<List<String>>() {}.getType());
                databaseReference.child("Food").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        list.clear();
                        for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                            Boolean check = false;
                            for(String log: finalFoodLogs) {
                                if(log.equals(dataSnapshot.child("name").getValue().toString()))
                                {
                                    check = true;
                                    break;
                                }
                            }
                            if(check == true)
                            {
                                Food food = new Food();

                                FoodInform foodInform =  dataSnapshot.child("foodInform").getValue(FoodInform.class);
                                food.setFoodInform(foodInform);
                                food.setId(dataSnapshot.child("id").getValue().toString());
                                food.setName(dataSnapshot.child("name").getValue().toString());
                                food.setImage(dataSnapshot.child("image").getValue().toString());
                                food.setComment(dataSnapshot.child("comment").getValue().toString());
                                list.add(food);
                            }
                            check = false;

                        }

                        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                        RecyclerView recyclerView = view.findViewById(R.id.main_recycle_view);
                        recyclerView.setHasFixedSize(true);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getCurrentContext(), 4,GridLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                        for(int j = finalFoodLogs.size()-1;j>=0;j--)//순서 동기화
                        {
                            for(int i =0;i<list.size();i++)
                            {
                                if(finalFoodLogs.get(j).equals(list.get(i).getName()))
                                {
                                    list.add(0,list.get(i));
                                    list.remove(i+1);
                                }
                            }
                        }
                        MainAdapter adapter = new MainAdapter(fragment, list,finalFoodLogs);
                        recyclerView.setAdapter(adapter);
                    }
                });



            }
        });

    }

    @Override
    public void onSearchEnd(String text) {
        preFrag = "main";

        ((Activity)getActivity()).setFrag(FoodList.newInstance(text,preFrag));
        //Toast.makeText(getCurrentContext(), "검색 처리됨 : " + text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getCurrentContext() {
        return getContext();
    }


}


