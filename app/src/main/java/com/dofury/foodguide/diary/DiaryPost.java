package com.dofury.foodguide.diary;

import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.Activity;
import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;
import com.dofury.foodguide.community.CommunityAddActivity;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiaryPost extends AppCompatActivity {
    private static final String TAG = "DiaryPost";
    private final UserAccount userAccount = UserAccount.getInstance();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    public Food food;
    private Button diaryImageBtn;
    private ImageView diaryImageView;
    private EditText edContent;
    private Uri uri;
    private RelativeLayout loaderLayout;
    public DiaryPost() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_detail_dirary_write);
        init();
        edContent.setHint("간단한 한마디 적어주세요.");
        diaryImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadImage();
            }
        });

    }
    private void upLoadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        launcher.launch(intent);
    }
    private void init()
    {
        findViewById(R.id.btn_diary_save).setOnClickListener(onClickListener);
        Intent intent = getIntent();
        food = intent.getParcelableExtra("DiaryPost");
        diaryImageBtn= findViewById(R.id.btn_diary_image_select);
        edContent = findViewById(R.id.et_diary_content);
        diaryImageView= findViewById(R.id.iv_diary_image);
    }
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        Glide.with(DiaryPost.this).load(uri).into(diaryImageView);

                    }
                }
            });

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId())
            {
                case R.id.btn_diary_save:
                    postUpdate();
                    break;
            }
        }
    };
    private void postUpdate(){
        final String contents = ((EditText) findViewById(R.id.et_diary_content)).getText().toString();
        final float rating = ((RatingBar) findViewById(R.id.diary_rating)).getRating();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String key = databaseReference.child("UserAccount").push().getKey();
        loaderLayout = findViewById(R.id.loaderLayout);
        if(contents.length() > 0 && uri != null)
        {
            loaderLayout.setVisibility(View.VISIBLE);
            PostInfo postInfo = new PostInfo(key,contents,null,time,rating);
            uploader(postInfo,key);
        }
        else if(contents.length() == 0){
            loaderLayout.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"내용이 공백이 있습니다",Toast.LENGTH_SHORT).show();
        }
        else if(uri == null)
        {
            loaderLayout.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"이미지를 올려주세요",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loaderLayout.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
        }

    }
    private void uploader(PostInfo postInfo,String key) {
        if (uri != null) {
            Toast.makeText(DiaryPost.this, "다이어리를 등록중입니다.", Toast.LENGTH_SHORT).show();
            Toast.makeText(DiaryPost.this, "잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Diary");
            storageReference.child(key).child("image.png").putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.child(key).child("image.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                postInfo.setImage(uri.toString());
                                databaseReference.child("UserAccount").child(userAccount.getIdToken()).child("food").child(food.getId()).child(key).setValue(postInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loaderLayout.setVisibility(View.GONE);
                                            Toast.makeText(DiaryPost.this, "다이어리가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        } else {
                                            loaderLayout.setVisibility(View.GONE);
                                            Toast.makeText(DiaryPost.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });
                    }
                }
            });
        }
    }

}
