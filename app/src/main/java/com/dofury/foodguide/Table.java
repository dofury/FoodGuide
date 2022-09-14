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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.inform.FoodInform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    Activity activity;
    private String selListFoodName = "";

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
        activity = (Activity) getActivity();
        if(bundle != null) {
            preFrag = bundle.getString("preFrag");
            selListFoodName = bundle.getString("foodName");
            Log.d("test", selListFoodName);
        }

        mainLayout = view.findViewById(R.id.main);
        switch (preFrag) {
            case "foodlist":
                    //selectFood();
                    //getSelectedFood();
                    dataLoad();
                    Log.d("test","도착");
                    break;
            default:
                    dataLoad();
                    Log.d("test","도착2");
                    break;
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity.fragmentSave(this,getContext());
        FloatingActionButton floatingActionButton = view.findViewById(R.id.table_item_add_button);
        floatingActionButton.setOnClickListener(v -> {
            ((Activity)getActivity()).setFrag(FoodList.newInstance("table"));

        });
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
    private void dataLoad(){
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        databaseReference.child("Food").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        if(selListFoodName.equals(dataSnapshot.child("name").getValue().toString()))
                        {
                            selectedFood = new Food();

                            FoodInform foodInform =  dataSnapshot.child("foodInform").getValue(FoodInform.class);
                            selectedFood.setFoodInform(foodInform);
                            selectedFood.setId(dataSnapshot.child("id").getValue().toString());
                            selectedFood.setName(dataSnapshot.child("name").getValue().toString());
                            selectedFood.setImage(dataSnapshot.child("image").getValue().toString());
                            selectedFood.setComment(dataSnapshot.child("comment").getValue().toString());
                            setValues();
                            break;
                        }

                }

            }
        });


    }

/*    private void selectFood(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preFrag = "table";
                ((Activity)getActivity()).setFrag(FoodList.newInstance(preFrag));
            }
        });
    }*/
    private void setValues(){

        ImageView iv = new ImageView(activity.mContext);
        iv.setOnTouchListener(onTouchListener());
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Glide.with(getContext()).load(selectedFood.getImage()).into(iv);
        mainLayout.addView(iv);
        Log.d("s","ss");
    }

    private void getSelectedFood(){
        selectedFood = bundle.getParcelable("foodlist");
    }
}
