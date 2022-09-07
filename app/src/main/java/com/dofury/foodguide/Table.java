package com.dofury.foodguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Table extends Fragment {
    View view;
    ImageView imageView;
    Food selectedFood;
    String preFrag = "table";
    Bundle bundle;
    private ViewGroup mainLayout;
    private int xDelta;
    private int yDelta;
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
    private View.OnTouchListener onTouchListener() {
        return (view, event) -> {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams)
                                view.getLayoutParams();

                        xDelta = x - lParams.leftMargin;
                        yDelta = y - lParams.topMargin;
                        break;

                    case MotionEvent.ACTION_UP:
                        ((Activity)getActivity()).setFrag(FoodList.newInstance("table"));
                        break;

                    case MotionEvent.ACTION_MOVE:
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
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
        iv.setImageResource(selectedFood.getImage());
    }

    private void getSelectedFood(){
        selectedFood = bundle.getParcelable("foodlist");
    }
}
