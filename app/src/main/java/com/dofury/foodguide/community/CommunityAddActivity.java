package com.dofury.foodguide.community;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dofury.foodguide.R;
import com.dofury.foodguide.login.UserAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommunityAddActivity extends AppCompatActivity {

    private UserAccount userAccount = UserAccount.getInstance();
    private EditText et_title, et_content;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_add);

        et_title = findViewById(R.id.et_title);
        et_title = findViewById(R.id.et_content);
        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpLoad();
            }
        });

    }

    private void UpLoad() {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();

        if(title.isEmpty() || content.isEmpty()) {
            Toast.makeText(CommunityAddActivity.this, "입력되지 않은 칸이 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    }

}