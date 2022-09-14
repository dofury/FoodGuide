package com.dofury.foodguide;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.inform.FoodInform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Table extends Fragment {
    View view;
    ImageView imageView;
    Food selectedFood;
    String preFrag = "table";
    Bundle bundle;
    private ViewGroup mainLayout;
    private int xDelta;
    private int yDelta;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
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

        mainLayout = view.findViewById(R.id.main);
        imageView = view.findViewById(R.id.appetizer_image);
        imageView.setOnTouchListener(onTouchListener());
        switch (preFrag) {
            case "foodlist":
                selectFood();
                getSelectedFood();
                setValues();
                break;
            default:
                break;
        }
        return view;
    }
    @SuppressLint("ClickableViewAccessibility")
    private View.OnTouchListener onTouchListener() {
        return (view, event) -> {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_UP:
                        break;

                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }

                mainLayout.invalidate();
                return true;


        };
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
        Glide.with(getContext()).load(selectedFood.getImage()).into(iv);
    }

    private void getSelectedFood(){
        selectedFood = bundle.getParcelable("foodlist");
    }
}
