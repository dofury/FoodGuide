package com.dofury.foodguide.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.dofury.foodguide.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Community extends Fragment {

    private TextView tv_isnull;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    private List<CommunityDAO> communityDAOList = new ArrayList<>();
    private RecyclerView rv_list;
    private CommunityAdapter communityAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_community, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_list = view.findViewById(R.id.rv_list);
        tv_isnull = view.findViewById(R.id.tv_isnull);

        FloatingActionButton fab_add = view.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommunityAddActivity.class);
                startActivity(intent);
            }
        });
        loadDatabase();
        communityAdapter = new CommunityAdapter(communityDAOList, requireContext());
        rv_list.setHasFixedSize(true);

        rv_list.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));
        rv_list.setAdapter(communityAdapter);
    }

    private void loadDatabase() {
        databaseReference.child("Community").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                communityDAOList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CommunityDAO communityDAO = dataSnapshot.getValue(CommunityDAO.class);
                    Log.d("__FoodGuide__", communityDAO.toString());
                    communityDAOList.add(communityDAO);
                }
                communityAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("__FoodGuide__", "데이터 불러오기 error");
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        loadDatabase();
    }
}

    