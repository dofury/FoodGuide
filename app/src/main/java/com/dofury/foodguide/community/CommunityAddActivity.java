package com.dofury.foodguide.community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dofury.foodguide.R;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommunityAddActivity extends AppCompatActivity {

    private final UserAccount userAccount = UserAccount.getInstance();
    private static final String TAG = "MultiImageActivity";
    ArrayList<Uri> uriList = new ArrayList<>();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");

    private EditText et_title, et_content;
    private Button btn_save;
    private ImageView btn_image;
    private TextView cancel;
    private RelativeLayout loaderLayout;
    private ArrayList<String> imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_add_background);
        FrameLayout contentFrame = findViewById(R.id.community_post_frame);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_community_add,contentFrame,true);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        loaderLayout = findViewById(R.id.loaderLayout);
        loaderLayout.setVisibility(View.VISIBLE);

        btn_save = findViewById(R.id.community_top_menu_button2);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpLoad();
            }
        });
        btn_image = findViewById(R.id.community_bottom_menu_button1);
        btn_image.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.black));
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadImage();
            }
        });
        cancel = findViewById(R.id.community_top_menu_tv1);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        loaderLayout.setVisibility(View.GONE);
    }

    private void setAdapter(ArrayList<Uri> list){
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.community_add_recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        CommunityAddAdapter adapter = new CommunityAddAdapter(this,list);
        recyclerView.setAdapter(adapter);
    }
    private void upLoadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2222);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{   // 이미지를 하나라도 선택한 경우
            if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                setAdapter(uriList);
            }
            else{      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 10){   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }

                    setAdapter(uriList);     // 리사이클러뷰
                }
            }
        }
    }


    private void UpLoad() {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();
        ArrayList<String> imageContentList = new ArrayList<>();
        SharedPreferences sharedPreference = getSharedPreferences("spText", 0);
        for(int i=0;i<uriList.size();i++)
        {
            String temp = "imageText" + String.valueOf(i);
            imageContentList.add(sharedPreference.getString(temp, "이미지값"));
        }
        int index = 1;
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if(title.isEmpty() || content.isEmpty()) {
            loaderLayout.setVisibility(View.GONE);
            Toast.makeText(CommunityAddActivity.this, "입력되지 않은 칸이 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = databaseReference.child("Community").push().getKey();
        CommunityDAO communityDAO = new CommunityDAO(title, userAccount.getIdToken(), userAccount.getNickname(), content,
                null, null, null, false, time, time, key,null);

        if(uriList.size() != 0) {
            Toast.makeText(CommunityAddActivity.this, "게시물을 등록중입니다.", Toast.LENGTH_SHORT).show();
            Toast.makeText(CommunityAddActivity.this, "잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Community");

            imageList = new ArrayList<>();

            for(Uri uri : uriList)
            {
                String name = "";
                for(int i=0;i<uri.toString().length();i++)
                {
                    if(Character.isDigit(uri.toString().charAt(i)) == true)
                    {
                        name +=uri.toString().charAt(i);
                    }
                }
                name += ".png";
                //if(communityDAO.getImage() != null)imageList = new Gson().fromJson(communityDAO.getImage(), new TypeToken<List<String>>() {}.getType());
                final String code = name;
                storageReference.child(key).child(code).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            storageReference.child(key).child(code).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageList.add(uri.toString());
                                    communityDAO.setImage(new Gson().toJson(imageList));
                                    communityDAO.setImageContent(new Gson().toJson(imageContentList));
                                    databaseReference.child("Community").child(key).setValue(communityDAO).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                if(imageList.size() == uriList.size())
                                                {
                                                    loaderLayout.setVisibility(View.GONE);
                                                    Toast.makeText(CommunityAddActivity.this, "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                }

                                            }
                                            else {
                                                loaderLayout.setVisibility(View.GONE);
                                                Toast.makeText(CommunityAddActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            });
                        }

                    }
                });
            }
        } else {
            Toast.makeText(CommunityAddActivity.this, "게시물을 등록중입니다.", Toast.LENGTH_SHORT).show();
            Toast.makeText(CommunityAddActivity.this, "잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
            databaseReference.child("Community").child(key).setValue(communityDAO).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        //loaderLayout.setVisibility(View.GONE);
                        Toast.makeText(CommunityAddActivity.this, "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else {
                        //loaderLayout.setVisibility(View.GONE);
                        Toast.makeText(CommunityAddActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



    }

}