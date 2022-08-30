package com.dofury.foodguide.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dofury.foodguide.Activity;
import com.dofury.foodguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity{

    private FirebaseAuth firebaseAuth;  // 파이어베이스 인증처리
    private DatabaseReference databaseReference;    // 실시간 데이터베이스

    private EditText et_email, et_pw;
    private Button btn_login, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");

        et_email = findViewById(R.id.et_email);
        et_pw = findViewById(R.id.et_pw);

        //

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void login() {
        String email = et_email.getText().toString();
        String pw = et_pw.getText().toString();

        if(email.isEmpty() || pw.isEmpty()) {
            Toast.makeText(LoginActivity.this, "입력되지 않은 데이터가 있습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // 로그인 성공
                    Intent intent = new Intent(LoginActivity.this, Activity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "환영합니다", Toast.LENGTH_SHORT).show();
                    // 로그인 성공했으니 로그인 액티비티는 제거
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}