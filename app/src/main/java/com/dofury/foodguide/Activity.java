package com.dofury.foodguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;

import com.dofury.foodguide.community.Community;
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
    Bundle mBundle;
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