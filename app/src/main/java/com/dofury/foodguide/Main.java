package com.dofury.foodguide;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dofury.foodguide.login.UserAccount;
import com.dofury.foodguide.diary.PostInfo;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class Main extends Fragment implements TextSetAble {
    private View view;
    SearchView searchView;
    Context context;
    private FirebaseDatabase firebaseDatabase;
    UserAccount userAccount = UserAccount.getInstance();
    String preFrag;
    Fragment fragment;
    public static Main newInstance(){
        return new Main();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        context = this.getContext();

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

    private void dataSet(){
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<Food> list = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
        databaseReference.child("UserAccount").child(userAccount.getIdToken()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                List<String> foodLogs = new ArrayList<>();
                if(userAccount.getFoodLogs() != null) foodLogs = new Gson().fromJson(userAccount.getFoodLogs(), new TypeToken<List<String>>() {}.getType());
                for(String log: foodLogs)
                {
                    databaseReference.child("Food").child(log).child("image").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            list.clear();
                            for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                                Food food = dataSnapshot.getValue(Food.class);
                                list.add(new Food(
                                        food.getId().toString(),
                                        food.getName().toString(),
                                        food.getImage()
                                ));
                            }

                            // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                            RecyclerView recyclerView = view.findViewById(R.id.main_recycle_view);
                            recyclerView.setLayoutManager(new GridLayoutManager(getCurrentContext(), 3));

                            // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                            MainAdapter adapter = new MainAdapter(fragment, list);
                            recyclerView.setAdapter(adapter);
                        }
                    });

                }

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


