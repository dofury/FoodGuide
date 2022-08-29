package com.dofury.foodguide;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.io.File;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FoodWrite extends Fragment {
    private View view;
    EditText editText;
    ImageButton upload;
    //저장될 경로
    String dirPath;
    //이미지 파일명을 포함한 경로
    String picPath;
    public static FoodWrite newInstance(){
        return new FoodWrite();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_write, container, false);
        editText = view.findViewById(R.id.food_write_text);
        String tempPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        dirPath = tempPath + "/Android/data/" +  getActivity().getApplicationContext().getPackageName();
        write();
        return view;
    }

    private void write(){
        upload = view.findViewById(R.id.write_upload);
        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                UploadThread thread = new UploadThread();
                thread.start();
            }
        });

    }
    class UploadThread extends Thread{
        @Override
        public void run() {
            super.run();

            try{
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder = builder.url("http://192.168.0.2:8080/FoodGuideServe/upload.jsp");

                String mobile_str1 = editText.getText().toString();

                /*FormBody.Builder builder2 = new FormBody.Builder();
                builder2.add("mobile_str1",mobile_str1);

                FormBody body = builder2.build();
                builder = builder.post(body);

                Request request = builder.build();
                Call call = client.newCall(request);
                call.execute();*/

                MultipartBody.Builder builder3 = new MultipartBody.Builder();
                builder3.setType(MultipartBody.FORM);
                builder3.addFormDataPart("mobile_str1", mobile_str1);

                String fileName = "temp_" + System.currentTimeMillis() + ".jpg";
                picPath = dirPath + "/" + fileName;
                File file = new File(picPath);
                RequestBody body = RequestBody.create(MultipartBody.FORM, file);
                builder3.addFormDataPart("mobile_image", file.getName(), body);

                MultipartBody body2 = builder3.build();

                builder = builder.post(body2);
                Request request = builder.build();
                Call call = client.newCall(request);
                call.execute();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}

