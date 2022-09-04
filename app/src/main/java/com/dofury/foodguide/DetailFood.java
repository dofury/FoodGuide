package com.dofury.foodguide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class DetailFood extends Fragment {
    Food selectedFood;
    View view;
    ImageView imageView;
    ImageButton selectImageBtn;
    Uri uri;
    public static DetailFood newInstance(){
        return new DetailFood();
    }
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_detail, container, false);
        setTap();//탭 설정 함수
        imageView = view.findViewById(R.id.food_detail_icon);

        getSelectedFood(); //선택한 음식정보 가져오기
        setValues(); //가져온 정보 화면에 보여주기
        foodTrans();
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
        ViewPager vp = view.findViewById(R.id.food_detail_view);
        ViewPageAdapter adapter = new ViewPageAdapter(getFragmentManager());
        vp.setAdapter(adapter);
        TabLayout tab = view.findViewById(R.id.food_detail_tap);
        tab.setupWithViewPager(vp);
    }
    private void getSelectedFood(){
        Bundle bundle = getArguments();
        selectedFood = bundle.getParcelable("ClickedFood");
    }
    public void foodTrans() {
        FoodDetailPage1 foodDetailPage1 = new FoodDetailPage1();
        Bundle bundle = new Bundle();
        bundle.putParcelable("FoodPage", selectedFood);
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