package com.dofury.foodguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePwActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    private EditText et_now_pw, et_change_pw, et_change_pw_;
    private Button btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        et_now_pw = findViewById(R.id.et_now_pw);
        et_change_pw = findViewById(R.id.et_change_pw);
        et_change_pw_ = findViewById(R.id.et_change_pw_);
        btn_change = findViewById(R.id.btn_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change();
            }
        });
    }

    private void change() {
        String email = firebaseUser.getEmail();
        String nowPW = et_now_pw.getText().toString();
        String changePW = et_change_pw.getText().toString();
        String changePW_ = et_change_pw_.getText().toString();

        if(nowPW.isEmpty() || changePW.isEmpty() || changePW_.isEmpty()) {
            Toast.makeText(ChangePwActivity.this, "입력되지 않은 빈 칸이 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!changePW.equals(changePW_)) {
            Toast.makeText(ChangePwActivity.this, "변경될 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!checkPW(changePW)) {
            Toast.makeText(ChangePwActivity.this, "비밀번호 생성 규칙이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }


        // 사용자 재인증
        AuthCredential credential = EmailAuthProvider.getCredential(email, nowPW);
        firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    // 재인증이 성공하면 비밀번호 변경함
                    firebaseUser.updatePassword(changePW).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(ChangePwActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                            else {
                                Toast.makeText(ChangePwActivity.this, "비밀번호가 변경되지않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(ChangePwActivity.this, "비밀번호가 일치하지 않거나 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkPW(String pw) {
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).{6,12}$");
        Matcher matcher = pattern.matcher(pw);

        if(pw.contains(" ")) return false;
        else return matcher.find();
    }
}