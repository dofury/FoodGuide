package com.dofury.foodguide.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.dofury.foodguide.Activity;
import com.dofury.foodguide.Food;
import com.dofury.foodguide.Main;
import com.dofury.foodguide.R;
import com.dofury.foodguide.inform.FoodInform;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.internal.Sleeper;

import java.util.List;

public class LoginActivity extends AppCompatActivity{

    private UserAccount userAccount = UserAccount.getInstance();

    private FirebaseAuth firebaseAuth;  // 파이어베이스 인증처리
    private FirebaseUser firebaseUser;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 900;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;    // 실시간 데이터베이스
    private Integer registerCheck;
    private EditText et_email, et_pw;
    private Button btn_login;
    private TextView tv_register;
    private Switch swch_auto_login;
    private RelativeLayout loaderLayout;
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
        googleLogin();
    }
    private void googleLogin()
    {
        signInButton = findViewById(R.id.signInButton);
        /*if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), Activity.class);
            startActivity(intent);
            finish();
        }*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    registerCheck = 0;
        FirebaseUser user = firebaseAuth.getCurrentUser();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            updateUI(user);

                            readData(databaseReference.child("UserAccount"), new OnGetDataListener() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    // 데이터베이스 연동에 성공하여 로그인이 성공함
                                    firebaseUser = firebaseAuth.getCurrentUser();
                                                String key = dataSnapshot.child("idToken").getValue().toString();
                                                if(key.equals(firebaseUser.getUid()))
                                                {
                                                    registerCheck = 2;
                                                }
                                                registerCheck = 1;
                                            }

                                @Override
                                public void onStart() {
                                    //when starting
                                    Log.d("ONSTART", "Started");
                                }

                                @Override
                                public void onFailure() {
                                    Log.d("onFailure", "Failed");
                                }
                            });










                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                    }
                });

        if(registerCheck == 2)
        {
            Log.d("ssd","ssd");
            databaseReference.child("UserAccount").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userAccount.setIdToken(dataSnapshot.getValue(UserAccount.class).getIdToken());
                    userAccount.setEmail(dataSnapshot.getValue(UserAccount.class).getEmail());
                    userAccount.setNickname(dataSnapshot.getValue(UserAccount.class).getNickname());
                    userAccount.setProfile(dataSnapshot.getValue(UserAccount.class).getProfile());
                    userAccount.setProfileM(dataSnapshot.getValue(UserAccount.class).getProfileM());
                    userAccount.setFoodLogs(dataSnapshot.getValue(UserAccount.class).getFoodLogs());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    loaderLayout.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else if(registerCheck==1)
        {
            UserAccount userAccount = UserAccount.getInstance();
            userAccount.setIdToken(user.getUid());
            // setEmail에는 연동된 이메일을 set
            userAccount.setEmail(acct.getEmail());
            userAccount.setNickname(acct.getDisplayName());
            userAccount.setProfile("null");
            userAccount.setProfileM("null");
            userAccount.setFoodLogs("[]");
            // 데이터 베이스 삽입
            databaseReference.child("UserAccount").child(user.getUid()).setValue(userAccount);
            Toast.makeText(LoginActivity.this, "회원가입에 성공했습니다", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuthWithGoogle(acct);
        }
    }
    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Intent intent = new Intent(this, Activity.class);
            startActivity(intent);
            finish();
        }
    }
    private void login(int type) {
        String email;
        String pw;
        loaderLayout = findViewById(R.id.loaderLayout);
        loaderLayout.setVisibility(View.VISIBLE);

        if(type == 1) {
            email = sharedPreferences.getString("auto_id", "");
            pw = sharedPreferences.getString("auto_pw", "");

        } else {
            email = et_email.getText().toString();
            pw = et_pw.getText().toString();

            if(email.isEmpty() || pw.isEmpty()) {
                loaderLayout.setVisibility(View.GONE);
                Log.d("112", "입력안된데이터가 있음");
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
                    loaderLayout.setVisibility(View.GONE);
                    //Toast.makeText(LoginActivity.this, "환영합니다", Toast.LENGTH_SHORT).show();



                    firebaseUser = firebaseAuth.getCurrentUser();
                    databaseReference.child("UserAccount").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userAccount.setIdToken(dataSnapshot.getValue(UserAccount.class).getIdToken());
                            userAccount.setEmail(dataSnapshot.getValue(UserAccount.class).getEmail());
                            userAccount.setNickname(dataSnapshot.getValue(UserAccount.class).getNickname());
                            userAccount.setProfile(dataSnapshot.getValue(UserAccount.class).getProfile());
                            userAccount.setProfileM(dataSnapshot.getValue(UserAccount.class).getProfileM());
                            userAccount.setFoodLogs(dataSnapshot.getValue(UserAccount.class).getFoodLogs());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            loaderLayout.setVisibility(View.GONE);
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
                    loaderLayout.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "로그인에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void readData(DatabaseReference ref,final OnGetDataListener listener) {
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();
            }

        });

    }


    private void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

}
interface OnGetDataListener {
    //this is for callbacks
    void onSuccess(DataSnapshot dataSnapshot);
    void onStart();
    void onFailure();
}
