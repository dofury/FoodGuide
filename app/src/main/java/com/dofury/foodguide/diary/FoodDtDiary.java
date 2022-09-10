package com.dofury.foodguide.diary;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dofury.foodguide.Activity;
import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;
import com.dofury.foodguide.community.CommunityAdapter;
import com.dofury.foodguide.community.CommunityDAO;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodDtDiary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDtDiary extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private Food food;
    private View view;
    private Activity activity;
    FoodDtDiary foodDtDiary;
    private FloatingActionButton floatingActionButton;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    private final UserAccount userAccount = UserAccount.getInstance();
    private final ArrayList<PostInfo> postInfoList = new ArrayList<>();
    public FoodDtDiary() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment FoodDetailPage1.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodDtDiary newInstance(String param1) {
        FoodDtDiary fragment = new FoodDtDiary();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food_detail_diary, container, false);
        foodDtDiary = this;
        getSelectedFood();
        //init();
        floatingActionButton = view.findViewById(R.id.diary_add_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                myStartActivity();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDatabase();
    }
    private void loadDatabase() {
        databaseReference.child("UserAccount").child(userAccount.getIdToken()).child("food").child(food.getId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                postInfoList.clear();
                for(DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                    PostInfo postInfo = dataSnapshot.getValue(PostInfo.class);
                    postInfoList.add(new PostInfo(
                            postInfo.getTitle().toString(),
                            postInfo.getContents().toString(),
                            postInfo.getImage().toString(),
                            postInfo.getDate().toString()));
                }
                RecyclerView recyclerView = view.findViewById(R.id.diary_recycle_view);
                recyclerView.setHasFixedSize(true);
                //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));//구분선
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setReverseLayout(true);//역순으로 출력
                recyclerView.setLayoutManager(linearLayoutManager);
                DiaryAdapter diaryAdapter = new DiaryAdapter(foodDtDiary, postInfoList);
                recyclerView.setAdapter(diaryAdapter);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDatabase();
    }

    private void init(){
        ArrayList<String> arrayList = new ArrayList<>();



    }
    private void getSelectedFood(){
        activity = (Activity) getActivity();
        Bundle bundle = activity.mBundle;
        if (activity.mBundle != null) {
            food = bundle.getParcelable("FoodDiary");
        }
    }

    private void myStartActivity()
    {
        Intent intent = new Intent(getContext(), DiaryPost.class);
        intent.putExtra("DiaryPost",food);
        startActivity(intent);
    }

}