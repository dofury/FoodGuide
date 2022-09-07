package com.dofury.foodguide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class DetailFood extends Fragment {
    Food selectedFood;
    View view;
    ImageView imageView;
    ImageButton selectImageBtn;
    Uri uri;
    Activity activity;
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private ViewPageAdapter viewPageAdapter;
    Context context;
    public static DetailFood newInstance(){
        return new DetailFood();
    }
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_detail, container, false);
        context = container.getContext();
        setTap();//탭 설정 함수
        imageView = view.findViewById(R.id.food_detail_icon);

        getSelectedFood(); //선택한 음식정보 가져오기
        setValues(); //가져온 정보 화면에 보여주기
        foodTrans();//음식 정보 전송
        //write();
        //viewImage(); //이미지 구현 함수
        return view;
    }
    private void setValues(){

        TextView tv1 = view.findViewById(R.id.food_detail_name);
        TextView tv2 = view.findViewById(R.id.food_detail_number);
        ImageView iv = view.findViewById(R.id.food_detail_icon);

        tv1.setText(selectedFood.getName());
        tv2.setText("#"+selectedFood.getId());
        iv.setImageResource(selectedFood.getImage());
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