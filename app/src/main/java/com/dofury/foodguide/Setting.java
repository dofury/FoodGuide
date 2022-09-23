package com.dofury.foodguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.login.LoginActivity;
import com.dofury.foodguide.login.UserAccount;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting extends Fragment {

    private FirebaseAuth firebaseAuth;

    private ListView lv_list;
    private ArrayAdapter<String>adapter;
    private CircleImageView circleImageView;
    private TextView tv_nickname;
    private View view;
    public static Setting newInstance(){
        return new Setting();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        firebaseAuth = FirebaseAuth.getInstance();
        circleImageView = view.findViewById(R.id.iv_profile);
        lv_list = view.findViewById(R.id.lv_list);
        tv_nickname = view.findViewById(R.id.tv_nickname);
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

    @Override
    public void onResume() {
        super.onResume();
        UserAccount userAccount = UserAccount.getInstance();
        tv_nickname.setText(userAccount.getNickname());
        if(!userAccount.getProfile().equals("null")) {
            Glide.with(requireContext()).load(userAccount.getProfileM()).into(circleImageView);
        }
    }

    void setAdapter() {
        List<String> data = Arrays.asList("프로필 수정", "비밀번호 변경", "도움말", "문의하기", "로그아웃");
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, data);
        lv_list.setAdapter(adapter);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = view.findViewById(R.id.setting_recycle_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        ArrayList<SettingItem> list = new ArrayList<>();
        for(int i =0;i<4;i++)
        {
            SettingItem settingItem = new SettingItem();
            switch(i)
            {
                case 0:
                    settingItem.setTitle("내 정보");
                    settingItem.setContent("나의 정보");
                    settingItem.setImage(R.drawable.ic_baseline_info_24);
                    break;
                case 1:
                    settingItem.setTitle("내 피드");
                    settingItem.setContent("내가 올린 다이어리");
                    settingItem.setImage(R.drawable.ic_baseline_post_add_24);
                    break;
                case 2:
                    settingItem.setTitle("도움말");
                    settingItem.setContent("궁금한 사항");
                    settingItem.setImage(R.drawable.ic_baseline_emoji_objects_24);
                    break;
                case 3:
                    settingItem.setTitle("설정");
                    settingItem.setContent("환경 설정");
                    settingItem.setImage(R.drawable.ic_baseline_settings_24);
                    break;
            }
            list.add(settingItem);
        }

        SettingAdapter adapter = new SettingAdapter(this,list);
        recyclerView.setAdapter(adapter);
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
    //di
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

