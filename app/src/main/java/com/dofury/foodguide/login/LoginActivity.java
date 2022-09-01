package com.dofury.foodguide.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerTabStrip;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dofury.foodguide.Activity;
import com.dofury.foodguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity{

    private FirebaseAuth firebaseAuth;  // 파이어베이스 인증처리
    private FirebaseUser firebaseUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;    // 실시간 데이터베이스

    private EditText et_email, et_pw;
    private Button btn_login;
    private TextView tv_register;
    private Switch swch_auto_login;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("preFile", 0);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FoodGuide");

        et_email = findViewById(R.id.et_email);
        et_pw = findViewById(R.id.et_pw);

        swch_auto_login = findViewById(R.id.swch_auto_login);
        swch_auto_login.setChecked(sharedPreferences.getBoolean("auto_login", false));

        //

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(0);
            }
        });


        tv_register = findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        // 자동로그인 체크였을 시 자동으로 로그인됨
        if(sharedPreferences.getBoolean("auto_login", false)) {
            login(1);
        }
    }

    private void login(int type) {
        String email;
        String pw;


        if(type == 1) {
            email = sharedPreferences.getString("auto_id", "");
            pw = sharedPreferences.getString("auto_pw", "");

        } else {
            email = et_email.getText().toString();
            pw = et_pw.getText().toString();

            if(email.isEmpty() || pw.isEmpty()) {
                Log.d("112", "입력안된친구있음");
                Toast.makeText(LoginActivity.this, "입력되지 않은 데이터가 있습니다", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        firebaseAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // 로그인 성공
                    Intent intent = new Intent(LoginActivity.this, Activity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "환영합니다", Toast.LENGTH_SHORT).show();

                    UserAccount userAccount = UserAccount.getInstance();

                    firebaseUser = firebaseAuth.getCurrentUser();
                    databaseReference.child("UserAccount").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userAccount.setIdToken(dataSnapshot.getValue(UserAccount.class).getIdToken());
                            userAccount.setEmail(dataSnapshot.getValue(UserAccount.class).getEmail());
                            userAccount.setNickname(dataSnapshot.getValue(UserAccount.class).getNickname());
                            userAccount.setProfile(dataSnapshot.getValue(UserAccount.class).getProfile());
                            userAccount.setProfileM(dataSnapshot.getValue(UserAccount.class).getProfileM());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(LoginActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // 자동 로그인 설정
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Boolean auto_flag = swch_auto_login.isChecked();
                    if(auto_flag) {
                        editor.putBoolean("auto_login", true);
                        editor.putString("auto_id", email);
                        editor.putString("auto_pw", pw);
                        editor.commit();
                    } else {
                        editor.putBoolean("auto_login", false);
                        editor.putString("auto_id", null);
                        editor.putString("auto_pw", null);
                        editor.commit();
                    }

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