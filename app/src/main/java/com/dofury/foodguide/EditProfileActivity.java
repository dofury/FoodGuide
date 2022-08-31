package com.dofury.foodguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private UserAccount userAccount = UserAccount.getInstance();
    private EditText et_nickname, et_email;
    private Button btn_save, btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        et_nickname = findViewById(R.id.et_nickname);
        et_email = findViewById(R.id.et_email);
        setInit();

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    private void setInit() {
        et_nickname.setText(userAccount.getNickname());
        et_email.setText(userAccount.getEmail());
    }

    private void save() {
        String nickname = et_nickname.getText().toString();
        String email = et_email.getText().toString();

        if(nickname.isEmpty() || email.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "입력되지 않은 데이터가 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        userAccount.setNickname(nickname);
        userAccount.setEmail(email);

        String key = userAccount.getIdToken();
        Map<String, Object> postValues = userAccount.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/FoodGuide/UserAccount/" + key, postValues);;
        databaseReference.updateChildren(childUpdates);
        firebaseUser.updateEmail(email);

        finish();
        Toast.makeText(EditProfileActivity.this, "프로필이 업데이트 되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void delete() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_delete_dialog);

        EditText editText = dialog.findViewById(R.id.et_pw);
        Button button = dialog.findViewById(R.id.btn_save);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pw = editText.getText().toString();
                Log.d("test", pw);
                if(pw.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "패스워드를 정확하게 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 패스워드 재인증
                AuthCredential credential = EmailAuthProvider.getCredential(userAccount.getEmail(), pw);
                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            // 데이터베이스 삭제가 안됨 나중에 다시 해봄
                            databaseReference.child("FoodGuide").child("UserAccount").child(userAccount.getIdToken()).removeValue();
                            firebaseUser.delete();
                            Toast.makeText(EditProfileActivity.this, "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences("preFile", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("auto_login", false);
                            editor.putString("auto_id", null);
                            editor.putString("auto_pw", null);
                            editor.commit();

                            PackageManager packageManager = getPackageManager();
                            Intent intent = packageManager.getLaunchIntentForPackage(getPackageName());
                            ComponentName componentName = intent.getComponent();
                            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                            startActivity(mainIntent);
                            System.exit(0);

                        }
                        Toast.makeText(EditProfileActivity.this, "패스워드를 정확하게 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.show();

    }
}