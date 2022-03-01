package com.dofury.foodguide;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DetailFood extends Fragment {
    Food selectedFood;
    View view;
    ImageView imageView;
    Button selectImageBtn;
    Uri uri;

    public static DetailFood newInstance(){
        return new DetailFood();
    }
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_detail, container, false);
        imageView = view.findViewById(R.id.food_detail_image);
        getSelectedFood(); //선택한 음식정보 가져오기

        setValues(); //가져온 정보 화면에 보여주기
        viewImage(); //이미지 구현 함수
        return view;
    }
    private void setValues(){

        TextView tv = view.findViewById(R.id.food_detail_name);
        ImageView iv = view.findViewById(R.id.food_detail_image);

        tv.setText(selectedFood.getName());
        iv.setImageResource(selectedFood.getImage());
    }

    private void getSelectedFood(){
        Bundle bundle = getArguments();
        selectedFood = bundle.getParcelable("ClickedFood");
    }
    private void viewImage(){
        selectImageBtn = view.findViewById(R.id.viewButton);

        selectImageBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityResult.launch(intent);
            }
        });
    }
    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
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
            });
}