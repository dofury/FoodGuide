package com.dofury.foodguide.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dofury.foodguide.R;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommunityAddActivity extends AppCompatActivity {

    private UserAccount userAccount = UserAccount.getInstance();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");

    private EditText et_title, et_content;
    private Button btn_save, btn_image;
    private ImageView iv_image;

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
        iv_image = findViewById(R.id.iv_image);

    }

    private void UpLoad() {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();
        String image = null;
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        if(title.isEmpty() || content.isEmpty()) {
            Toast.makeText(CommunityAddActivity.this, "입력되지 않은 칸이 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String key = databaseReference.child("Community").push().getKey();
        CommunityDAO communityDAO = new CommunityDAO(title, userAccount.getIdToken(), userAccount.getNickname(), content,
                null, image, null, false, time, time, key);
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