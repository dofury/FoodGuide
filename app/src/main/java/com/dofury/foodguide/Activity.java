package com.dofury.foodguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;



public class Activity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Main main;
    private Table table;
    private Community community;
    private Setting setting;
    private Food fd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_main:
                        setMenu(0);
                        break;
                    case R.id.action_table:
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

}