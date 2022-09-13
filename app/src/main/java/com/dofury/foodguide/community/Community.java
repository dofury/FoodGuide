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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    private final List<CommunityDAO> communityDAOList = new ArrayList<>();
    private RecyclerView rv_list;

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

        Button btn_rank = view.findViewById(R.id.btn_rank);
        btn_rank.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), RankDirActivity.class);
            startActivity(intent);
        });

        FloatingActionButton fab_add = view.findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommunityAddActivity.class);
                startActivity(intent);
            }
        });

        loadDatabase();
    }

    private void loadDatabase() {
        databaseReference.child("Community").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                communityDAOList.clear();
                for(DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    CommunityDAO communityDAO = dataSnapshot.getValue(CommunityDAO.class);
                    communityDAOList.add(0, communityDAO);
                }
                CommunityAdapter communityAdapter = new CommunityAdapter(communityDAOList, getContext());

                rv_list.setHasFixedSize(true);
                rv_list.addItemDecoration(new DividerItemDecoration(requireContext(), 1));
                rv_list.setAdapter(communityAdapter);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDatabase();
    }
}

    