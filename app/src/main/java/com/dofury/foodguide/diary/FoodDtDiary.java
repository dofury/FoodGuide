package com.dofury.foodguide.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dofury.foodguide.Activity;
import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodDtDiary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDtDiary extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    Food food;
    View view;
    Activity activity;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }
    FloatingActionButton floatingActionButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food_detail_diary, container, false);
        getSelectedFood();
        floatingActionButton = view.findViewById(R.id.food_dt_diary_add_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                myStartActivity();
            }
        });

        // Inflate the layout for this fragment
        return view;
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