package com.dofury.foodguide.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dofury.foodguide.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity{

    private FirebaseAuth firebaseAuth;  // 파이어베이스 인증처리
    private DatabaseReference databaseReference;    // 실시간 데이터베이스

    private EditText et_email, et_pw, et_pw_, et_nickname;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");

        et_email = findViewById(R.id.et_email);
        et_pw = findViewById(R.id.et_pw);
        et_pw_ = findViewById(R.id.et_pw_);
        et_nickname = findViewById(R.id.et_nickname);

        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }


    private void register() {
        String email = et_email.getText().toString();
        String pw = et_pw.getText().toString();
        String pw_ = et_pw_.getText().toString();
        String nickname = et_nickname.getText().toString();

        if(email.isEmpty() || pw.isEmpty() || nickname.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "입력되지 않은 데이터가 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if(!pw.equals(pw_)) {
            Toast.makeText(RegisterActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else if(!checkPW(pw)) {
            Toast.makeText(RegisterActivity.this, "비밀번호는 6~12 자의 영문과 숫자가 포함되어야합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    // 데이터베이스 연동에 성공하여 로그인이 성공함
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    UserAccount userAccount = new UserAccount();
                    userAccount.setIdToken(firebaseUser.getUid());
                    // setEmail에는 연동된 이메일을 set
                    userAccount.setEmail(firebaseUser.getEmail());
                    userAccount.setPw(pw);
                    userAccount.setNickname(nickname);

                    // 데이터 베이스 삽입
                    databaseReference.child("UserAccount").child(firebaseUser.getUid()).setValue(userAccount);
                    Toast.makeText(RegisterActivity.this, "회원가입에 성공했습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPW(String pw) {
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).{6,12}$");
        Matcher matcher = pattern.matcher(pw);

        if(pw.contains(" ")) return false;
        else if(!matcher.find()){
            return false;
        }

        return true;
    }
}