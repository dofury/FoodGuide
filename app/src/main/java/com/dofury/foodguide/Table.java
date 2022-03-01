package com.dofury.foodguide;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Table extends Fragment {
    View view;
    ImageView imageView;
    Food selectedFood;
    String preFrag = "dd";
    Bundle bundle;
    Food testA;


    public Table() {

    }

    public static Table newInstance(){
        return new Table();
    }
    public static Table newInstance(String preFrag){
        return new Table(preFrag);
    }
    public Table(String preFrag)
    {
        this.preFrag = preFrag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_table, container, false);
        bundle = getArguments();
        if(bundle != null)
            preFrag = bundle.getString("preFrag");

        switch (preFrag) {
            case "foodlist":
                selectFood();
                getSelectedFood();
                setValues();
                preFrag = "";
                break;
            default:
                selectFood();
                preFrag = "";
                break;
        }
        return view;
    }
    private void selectFood(){
        imageView = view.findViewById(R.id.appetizer_image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preFrag = "table";
                ((Activity)getActivity()).setFrag(FoodList.newInstance(preFrag));
            }
        });
    }
    private void setValues(){

        TextView tv = view.findViewById(R.id.appetizer_name);
        ImageView iv = view.findViewById(R.id.appetizer_image);

        tv.setText(selectedFood.getName());
        iv.setImageResource(selectedFood.getImage());
    }

    private void getSelectedFood(){
        selectedFood = bundle.getParcelable("foodlist");
    }
}
