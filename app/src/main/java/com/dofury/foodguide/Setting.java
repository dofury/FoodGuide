package com.dofury.foodguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dofury.foodguide.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Setting extends Fragment {

    private FirebaseAuth firebaseAuth;

    private ListView lv_list;
    private ArrayAdapter<String>adapter;

    public static Setting newInstance(){
        return new Setting();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();

        lv_list = view.findViewById(R.id.lv_list);
        setAdapter();

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        editProfile();
                        break;
                    case 1:
                        changePw();
                        break;
                    case 2:
                        help();
                        break;
                    case 3:
                        inquiry();
                        break;
                    case 4:
                        logout();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    void setAdapter() {
        List<String> data = Arrays.asList("프로필 수정", "비밀번호 변경", "도움말", "문의하기", "로그아웃");
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);
        lv_list.setAdapter(adapter);
    }

    void editProfile() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        startActivity(intent);
    }

    void changePw() {
        Intent intent = new Intent(getContext(), ChangePwActivity.class);
        startActivity(intent);
    }

    void help() {

    }

    void inquiry() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("plain/text");
        String[] address = {"chadh1004@gmail.com"};
        email.putExtra(Intent.EXTRA_EMAIL, address);
        email.putExtra(Intent.EXTRA_SUBJECT, "[FoodGuide 문의사항] 제목없음");
        email.putExtra(Intent.EXTRA_TEXT, "닉네임: \n날짜: \n내용: ");
        startActivity(email);
    }

    void logout() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preFile", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("auto_login", false);
        editor.putString("auto_id", null);
        editor.putString("auto_pw", null);
        editor.commit();

        firebaseAuth.signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}

