package com.dofury.foodguide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.community.CommunityDAO;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DetailFood extends Fragment {
    private Food selectedFood;
    private View view;
    private ImageView imageView;
    private ImageButton selectImageBtn;
    private Uri uri;
    private Activity activity;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private ViewPageAdapter viewPageAdapter;
    Context context;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");

    private final UserAccount userAccount = UserAccount.getInstance();
    public static DetailFood newInstance(){
        return new DetailFood();
    }
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        context = container.getContext();
        setTap();//탭 설정 함수
        imageView = view.findViewById(R.id.food_detail_icon);

        getSelectedFood(); //선택한 음식정보 가져오기
        setValues(); //가져온 정보 화면에 보여주기
        foodTrans();//음식 정보 전송
        setLoadLike();
        //write();
        //viewImage(); //이미지 구현 함수
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
/*        Bundle bundle = new Bundle();
        bundle.putString("food_name", selectedFood.getName());
        getParentFragmentManager().setFragmentResult("key", bundle);*/
        //getParentFragmentManager().setFragmentResult("key", bundle);
    }

    private void setLoadLike()
    {
        ToggleButton tgb_like =  view.findViewById(R.id.food_detail_like_button);
        TextView tv_like = view.findViewById(R.id.food_detail_like_text);
        databaseReference.child("Food").child(selectedFood.getName()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DataSnapshot> task) {
                Food food = task.getResult().getValue(Food.class);
                List<String>jsonLikeList = new ArrayList<>();
                if(food.getLike() != null) jsonLikeList = new Gson().fromJson(food.getLike(), new TypeToken<List<String>>() {}.getType());

                if(jsonLikeList.isEmpty()) tv_like.setText("0");
                else tv_like.setText(String.valueOf(jsonLikeList.size()));

                boolean flag = false;
                for(String s : jsonLikeList) {
                    if(s.equals(userAccount.getIdToken())) {
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }
                }
                tgb_like.setChecked(flag);

                tgb_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseReference.child("Food").child(food.getName()).child("like").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                List<String>jsonLikeList = new ArrayList<>();
                                String taskJson = String.valueOf(task.getResult().getValue());
                                if(!taskJson.equals("null")) {
                                    jsonLikeList = new Gson().fromJson(taskJson, new TypeToken<List<String>>(){}.getType());
                                }

                                if(tgb_like.isChecked()) {
                                    jsonLikeList.add(userAccount.getIdToken());
                                }
                                else {
                                    jsonLikeList.remove(userAccount.getIdToken());
                                }

                                taskJson = new Gson().toJson(jsonLikeList);

                                databaseReference.child("Food").child(food.getName()).child("like").setValue(taskJson).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        setLoadLike();
                                    }
                                });
                            }
                        });
                    }
                });
        }
        });

    }
    private void setValues(){

        TextView tv1 = view.findViewById(R.id.food_detail_name);
        TextView tv2 = view.findViewById(R.id.food_detail_number);
        ImageView iv = view.findViewById(R.id.food_detail_icon);

        tv1.setText(selectedFood.getName());
        tv2.setText("#"+selectedFood.getId());
        Glide.with(context).load(selectedFood.getImage()).into(iv);


    }

    private void setTap(){
        mTabLayout = (TabLayout) view.findViewById(R.id.food_detail_tap);
        mViewPager = (ViewPager2) view.findViewById(R.id.food_detail_view);

        //프레그먼트 이동 구현
        viewPageAdapter = new ViewPageAdapter((AppCompatActivity) getActivity());
        mViewPager.setAdapter(viewPageAdapter);

        final List<String> tabElement = Arrays.asList("기본 정보","다이어리","한마디");
        //tabLyout와 viewPager 연결
        new TabLayoutMediator(mTabLayout,mViewPager,new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(context);
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/maruburi_regular.otf");
                textView.setTypeface(typeface);
                textView.setText(tabElement.get(position));
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.parseColor("#000000"));
                //textView.setBackgroundColor(Color.parseColor("#afe3ff"));//백그라운드 색 설정
                tab.setCustomView(textView);
            }

        }).attach();

    }
    private void getSelectedFood(){
        Bundle bundle = getArguments();
        selectedFood = bundle.getParcelable("ClickedFood");
    }
    public void foodTrans() {
        Bundle bundle = new Bundle();
        activity = (Activity) getActivity();
        bundle.putParcelable("FoodPage", selectedFood);
        bundle.putParcelable("FoodDiary",selectedFood);
        bundle.putParcelable("FoodComment", selectedFood);
        activity.bundleSave(bundle);
    }
/*    private void write(){
        selectImageBtn = view.findViewById(R.id.food_detail_write);
        selectImageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                ((Activity)getActivity()).setFrag(FoodWrite.newInstance());
            }
        });

    }*/
/*    private void viewImage(){
        selectImageBtn = view.findViewById(R.id.viewButton);

        selectImageBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityResult.launch(intent);
            }
        });
    }*/
/*    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== RESULT_OK && result.getData() !=null){

                        uri = result.getData().getData();

                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                            imageView.setImageBitmap(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            });*/
}