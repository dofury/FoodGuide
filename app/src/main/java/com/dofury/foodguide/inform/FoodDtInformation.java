package com.dofury.foodguide.inform;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dofury.foodguide.Activity;
import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;
import com.dofury.foodguide.community.CommunityDAO;
import com.dofury.foodguide.diary.DiaryAdapter;
import com.dofury.foodguide.diary.PostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodDtInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDtInformation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public FoodDtInformation() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FoodDtInformation newInstance(String param1) {
        FoodDtInformation fragment = new FoodDtInformation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    private Food selectedFood;
    private View view;
    private Activity activity;
    private TextView tv_intro,tv_recipes,tv_origin;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }
    private void getSelectedFood(){
        activity = (Activity) getActivity();
        Bundle bundle = activity.mBundle;
        if (activity.mBundle != null) {
            selectedFood = bundle.getParcelable("FoodPage");
        }
    }
    private void loadDatabase() {
        databaseReference.child("Food").child(selectedFood.getName()).child("foodInform").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("__FoodGuide__", "글 불러오기 실패");
                } else {
                    FoodInform foodInform = task.getResult().getValue(FoodInform.class);
                    tv_intro.setText(foodInform.getIntro());
                    tv_origin.setText(foodInform.getOrigin());
                    tv_recipes.setText(foodInform.getRecipes());

                }
            }
        });
    }
    private void init(){
        tv_intro = view.findViewById(R.id.food_detail_tap_item_text1);
        tv_origin = view.findViewById(R.id.food_detail_tap_item_text2);
        tv_recipes = view.findViewById(R.id.food_detail_tap_item_text3);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food_detail_information,container,false);
        init();
        getSelectedFood();
        loadDatabase();
        //printFood();
        return view;
    }
    /*private void printFood(){
       TextView detailText1 = view.findViewById(R.id.food_detail_tap_item1_text1);
        TextView detailText2 = view.findViewById(R.id.food_detail_tap_item1_text2);
        switch (selectedFood.getId())
        {
            case "1":
            {
                detailText1.setText("빵을 포갠 음식");
                detailText2.setText("예전에 서양권에서 즐겨먹었다");
                break;
            }
            case "2":
            {
                detailText1.setText("닭을 튀긴 음식");
                detailText2.setText("한국에서 즐겨먹는다");
                break;
            }
            case "3":
            {
                detailText1.setText("면을 넣은 국");
                detailText2.setText("동남권에서 즐겨먹는다");
                break;
            }
        }
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }*/
}