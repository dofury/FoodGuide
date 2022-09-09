package com.dofury.foodguide.diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;
import com.dofury.foodguide.community.CommunityAddActivity;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryPost extends AppCompatActivity {
    private static final String TAG = "DiaryPost";
    private final UserAccount userAccount = UserAccount.getInstance();
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    Food food;
    public DiaryPost() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_food_detail_dirary_write);
        findViewById(R.id.btn_diary_save).setOnClickListener(onClickListener);
        Intent intent = getIntent();
        food = intent.getParcelableExtra("DiaryPost");



    }
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
        final String title = ((EditText) findViewById(R.id.et_diary_title)).getText().toString();
        final String contents = ((EditText) findViewById(R.id.et_diary_content)).getText().toString();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String key = databaseReference.child("UserAccount").push().getKey();
        if(title.length() > 0 || contents.length() > 0)
        {

            PostInfo postInfo = new PostInfo(title,contents);
            uploader(postInfo,key);
        }
        else {
            Toast.makeText(getApplicationContext(),"빈칸이 있습니다",Toast.LENGTH_SHORT).show();
        }

    }
    private void uploader(PostInfo postInfo,String key){
        databaseReference.child("UserAccount").child(userAccount.getIdToken()).child("food").child(food.getId()).child(key).setValue(postInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(DiaryPost.this, "게시물이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else {
                    Toast.makeText(DiaryPost.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
