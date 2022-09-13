package com.dofury.foodguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dofury.foodguide.community.Community;
import com.dofury.foodguide.diary.DiaryPost;
import com.dofury.foodguide.inform.FoodInform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Activity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Main main;
    private Table table;
    private Community community;
    private Setting setting;
    private Food fd;
    public Bundle mBundle;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    // 확인할 권한 목록
    String [] permission_list = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_write:
                        setMenu(0);
                        break;
                    case R.id.action_profile:
                        setMenu(1);
                        break;
                    case R.id.action_community:
                        setMenu(2);
                        break;
                    case R.id.action_setting:
                        setMenu(3);
                        break;
                }
                return true;
            }
        });
        main = new Main();
        table = new Table();
        community = new Community();
        setting = new Setting();
        setMenu(0); //첫 메뉴 화면을 무엇을 지정해줄 것인지 선택.
        //세이브선//
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permission_list, 0);
        } else {
        }

        Intent intent = getIntent();
        String foodName = intent.getStringExtra("foodNameForRank");
        if(foodName != null) {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("FoodGuide").child("Food");
            dbRef.child(foodName).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()) {
                        Food food = task.getResult().getValue(Food.class);
                        Bundle bundle = new Bundle();
                        DetailFood detailFood = new DetailFood();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        bundle.putParcelable("ClickedFood", food);
                        detailFood.setArguments(bundle);
                        transaction.replace(R.id.main_frame, detailFood);
                        transaction.commit();
                    }
                }
            });
        }

        //foodInit();
    }
    //메뉴 추가시 사용하는 관리함수
    private void foodInit()
    {
        String name = "샌드위치";
        String id = "1";
        String intro = "빵을 포갠 음식";
        String origin = "미국에서 즐겨먹는다";
        String recipe = "https://wtable.co.kr/recipes/2qn9139vimEpjh8e3QN9PFFA";
        String comment = "";
        String like = "[]";
        FoodInform foodInform = new FoodInform();
        foodInform.setIntro(intro);
        foodInform.setOrigin(origin);
        foodInform.setRecipes(recipe);
        Food food = new Food(id,name,0,foodInform,comment,like);
        databaseReference.child("Food").child(name).setValue(food).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Activity.this, name+"이 추가되었습니다 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int result: grantResults){
            if(result == PackageManager.PERMISSION_DENIED){
                return;
            }
        }
    }

    //메뉴내에서 탭교체 함수
    private void setMenu(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, main);
                ft.commit(); //저장
                break;
            case 1:
                ft.replace(R.id.main_frame, table);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, community);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, setting);
                ft.commit();
                break;

        }
    }
    public void setFrag(Fragment fragment){//프래그먼트 탭 교체 함수
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment).commit();
    }

    public void bundleSave(Bundle bundle) {
        this.mBundle = bundle;
    }
}