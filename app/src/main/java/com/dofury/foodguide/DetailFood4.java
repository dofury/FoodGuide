package com.dofury.foodguide;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.dofury.foodguide.databinding.ActivityDetailFood2Binding;
import com.google.android.material.tabs.TabLayout;

public class DetailFood4 extends AppCompatActivity {


    ImageView imageView;
    Food selectedFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTap();//탭 설정 함수
        imageView = findViewById(R.id.food_detail_icon);

        getSelectedFood(); //선택한 음식정보 가져오기
        setValues(); //가져온 정보 화면에 보여주기
        foodTrans();// 음식정보 page1로 전달

    }
    private void setValues(){

        TextView tv1 = findViewById(R.id.food_detail_name);
        TextView tv2 = findViewById(R.id.food_detail_number);
        ImageView iv = findViewById(R.id.food_detail_icon);

        tv1.setText(selectedFood.getName());
        tv2.setText("#"+selectedFood.getId());
        iv.setImageResource(selectedFood.getImage());
    }

    private void setTap(){
        ViewPager vp = findViewById(R.id.food_detail_view);
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        TabLayout tab = findViewById(R.id.food_detail_tap);
        tab.setupWithViewPager(vp);
    }
    private void getSelectedFood(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedFood = bundle.getParcelable("ClickedFood");
    }
    public void foodTrans() {
        FoodDetailPage1 foodDetailPage1 = new FoodDetailPage1();
    }

}