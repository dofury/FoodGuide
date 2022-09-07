package com.dofury.foodguide;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dofury.foodguide.food.FoodComment;
import com.dofury.foodguide.food.FoodCommentAdapter;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FoodDtComment extends Fragment {
    private List<FoodComment> foodCommentList = new ArrayList<>();
    private final UserAccount userAccount = UserAccount.getInstance();
    private RecyclerView recyclerView;
    private String mFoodName;

    public FoodDtComment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_detail_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_list);
        FloatingActionButton fab = view.findViewById(R.id.fab_add);

        getParentFragmentManager().setFragmentResultListener("key", this, (requestKey, result) -> {
            mFoodName = result.getString("food_name");
        });

        fab.setOnClickListener(v -> {
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.custom_dialog_add_own_word);
            EditText et_own_word = dialog.findViewById(R.id.et_own_word);
            Button btn_send = dialog.findViewById(R.id.btn_send);

            btn_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FoodComment foodComment = new FoodComment();
                    foodComment.setUid(userAccount.getIdToken());
                    foodComment.setDate(new SimpleDateFormat("yy-MM-dd").format(new Date()));
                    foodComment.setName(userAccount.getNickname());
                    foodComment.setContent(et_own_word.getText().toString());

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
                    databaseReference.child("Food").child(mFoodName).child("comment").get().addOnCompleteListener(task -> {
                        String json = String.valueOf(task.getResult().getValue());

                        if(!json.equals("null")) foodCommentList = new Gson().fromJson(json, new TypeToken<List<FoodComment>>(){}.getType());
                        foodCommentList.add(foodComment);
                        json = new Gson().toJson(foodCommentList);
                        databaseReference.child("Food").child(mFoodName).child("comment").setValue(json).addOnCompleteListener(task1 -> {
                            FoodCommentAdapter foodCommentAdapter = new FoodCommentAdapter(requireContext(), foodCommentList, mFoodName);
                            foodCommentAdapter.notifyDataSetChanged();
                            recyclerView.setAdapter(foodCommentAdapter);

                            dialog.onBackPressed();
                        });
                    });
                }
            });
            dialog.show();

        });

        loadComment();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadComment();

    }

    private void loadComment(){

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("FoodGuide");
        getParentFragmentManager().setFragmentResultListener("key", this, (requestKey, result) -> {
            mFoodName = result.getString("food_name");

            dbRef.child("Food").child(mFoodName).child("comment").get().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    String comment = String.valueOf(task.getResult().getValue());
                    if(!comment.isEmpty()) foodCommentList = new Gson().fromJson(comment, new TypeToken<List<FoodComment>>(){}.getType());

                    FoodCommentAdapter foodCommentAdapter = new FoodCommentAdapter(requireContext(), foodCommentList, mFoodName);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(foodCommentAdapter);
                }
            });
        });


    }
}