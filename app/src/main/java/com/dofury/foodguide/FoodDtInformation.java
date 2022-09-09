package com.dofury.foodguide;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodDtInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodDtInformation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FoodDtInformation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodDetailPage1.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodDtInformation newInstance(String param1, String param2) {
        FoodDtInformation fragment = new FoodDtInformation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    Food selectedFood;
    View view;
    Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void getSelectedFood(){
        activity = (Activity) getActivity();
        Bundle bundle = activity.mBundle;
        if (activity.mBundle != null) {
            selectedFood = bundle.getParcelable("FoodPage");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_food_detail_information,container,false);
        getSelectedFood();
        printFood();
        return view;
    }
    private void printFood(){
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
    }
}