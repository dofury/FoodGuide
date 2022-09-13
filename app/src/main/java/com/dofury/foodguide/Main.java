package com.dofury.foodguide;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dofury.foodguide.diary.PostInfo;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Main extends Fragment implements TextSetAble {
    private View view;
    private SearchView searchView;
    private FirebaseDatabase firebaseDatabase;
    private String preFrag;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    private final UserAccount userAccount = UserAccount.getInstance();
    public static Main newInstance(){
        return new Main();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        searchView = view.findViewById(R.id.food_search);
        searchView.setOnQueryTextListener(new MyTextQueryListener(this));
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);//전체영역 터치해도 검색반응
            }

        });
        dataSet();
        return view;
    }

    private void dataSet(){
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
/*        ArrayList<Food> list = new ArrayList<>();

        databaseReference.child("Food").child(food.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                list.clear();
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    PostInfo postInfo = dataSnapshot.getValue(PostInfo.class);
                    postInfo.add(new PostInfo(
                            Food.getId().toString(),
                            postInfo.getContents().toString(),
                            postInfo.getImage().toString(),
                            postInfo.getDate().toString(),
                            postInfo.getRating()));
                }

                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                RecyclerView recyclerView = view.findViewById(R.id.main_recycle_view);
                recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));

                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                MainAdapter adapter = new MainAdapter(this, list);
                recyclerView.setAdapter(adapter);
            }
        });*/
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


