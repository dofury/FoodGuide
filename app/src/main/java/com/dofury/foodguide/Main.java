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

import com.google.firebase.database.FirebaseDatabase;


public class Main extends Fragment implements TextSetAble {
    private View view;
    SearchView searchView;
    private FirebaseDatabase firebaseDatabase;
    String preFrag;
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

        return view;
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


