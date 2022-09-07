package com.dofury.foodguide.community;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.Activity;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommunityAddActivity extends AppCompatActivity {

    private final UserAccount userAccount = UserAccount.getInstance();

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");

    private EditText et_title, et_content;
    private Button btn_save, btn_image;
    private ImageView iv_image;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_add);

        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpLoad();
            }
        });
        btn_image = findViewById(R.id.btn_image);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadImage();
            }
        });

        iv_image = findViewById(R.id.iv_image);

    }

    private void upLoadImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        launcher.launch(intent);
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        uri = result.getData().getData();
                        Glide.with(CommunityAddActivity.this).load(uri).into(iv_image);

                    }
                }
            });


    private void UpLoad() {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if(title.isEmpty() || content.isEmpty()) {
            Toast.makeText(CommunityAddActivity.this, "입력되지 않은 칸이 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = databaseReference.child("Community").push().getKey();
        CommunityDAO communityDAO = new CommunityDAO(title, userAccount.getIdToken(), userAccount.getNickname(), content,
                null, null, null, false, time, time, key);

        if(uri != null) {
            Toast.makeText(CommunityAddActivity.this, "게시글을 저장중입니다.", Toast.LENGTH_SHORT).show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("Community");
            storageReference.child(key).child("image.png").putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()) {
                        storageReference.child(key).child("image.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                communityDAO.setImage(uri.toString());
                                databaseReference.child("Community").child(key).setValue(communityDAO).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(CommunityAddActivity.this, "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        }
                                        else {
                                            Toast.makeText(CommunityAddActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });
                    }
                }
            });
        } else {
            Toast.makeText(CommunityAddActivity.this, "게시글을 저장중입니다.", Toast.LENGTH_SHORT).show();
            databaseReference.child("Community").child(key).setValue(communityDAO).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(CommunityAddActivity.this, "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    else {
                        Toast.makeText(CommunityAddActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



    }

}