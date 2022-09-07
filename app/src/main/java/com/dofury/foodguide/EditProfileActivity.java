package com.dofury.foodguide;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = firebaseStorage.getReference();
    private final UserAccount userAccount = UserAccount.getInstance();
    private EditText et_nickname, et_email;
    private Button btn_save, btn_delete;
    private TextView tv_change_profile;
    private CircleImageView civ_profile;

    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        civ_profile = findViewById(R.id.civ_profile);

        if(!userAccount.getProfile().equals("null")) {
            Glide.with(EditProfileActivity.this).load(userAccount.getProfileM()).into(civ_profile);
        }

        et_nickname = findViewById(R.id.et_nickname);
        et_email = findViewById(R.id.et_email);
        tv_change_profile = findViewById(R.id.tv_change_profile);
        tv_change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfile();
            }
        });

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

    private void changeProfile() {
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
                Glide.with(EditProfileActivity.this).load(uri).into(civ_profile);
            }
        }
    });

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

        if(uri != null)
        {
            storageReference.child(userAccount.getIdToken()).child("Profile").child("profile.png").putFile(uri);
            storageReference.child(userAccount.getIdToken()).child("Profile/profile.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri u) {
                    userAccount.setProfile(u.toString());
                }
            });
            // 내부 사진 경로가 바뀌면 사진도 바뀜 그러니 이거 나중에 디풀더 만들어서 고쳐야함
            userAccount.setProfileM(uri.toString());
        }

        String key = userAccount.getIdToken();
        Map<String, Object> postValues = userAccount.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/FoodGuide/UserAccount/" + key, postValues);
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