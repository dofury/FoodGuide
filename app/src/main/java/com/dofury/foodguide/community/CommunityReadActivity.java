package com.dofury.foodguide.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.dofury.foodguide.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
public class CommunityReadActivity extends AppCompatActivity {
    private TextView tv_title, tv_nickname, tv_date, tv_date_, tv_content;
    private EditText et_reply;
    private CircleImageView civ_profile;
    private ImageView iv_image;
    private RecyclerView rv_reply;
    private ImageButton btn_send;
    private String key;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    private CommunityDAO communityDAO = new CommunityDAO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_read);

        Intent intent = getIntent();
        key = intent.getStringExtra("id");

        tv_title = findViewById(R.id.tv_title);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_date = findViewById(R.id.tv_date);
        tv_date_ = findViewById(R.id.tv_date_);
        tv_content = findViewById(R.id.tv_content);
        et_reply = findViewById(R.id.et_reply);
        civ_profile = findViewById(R.id.civ_profile);
        iv_image = findViewById(R.id.iv_image);
        rv_reply = findViewById(R.id.rv_reply);
        btn_send = findViewById(R.id.btn_send);

        databaseReference.child("Community").child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("__FoodGuide__", "글 불러오기 실패");
                }
                else {
                    communityDAO = task.getResult().getValue(CommunityDAO.class);
                    tv_title.setText(communityDAO.getTitle());
                    tv_nickname.setText(communityDAO.getNickname());
                    tv_date.setText(communityDAO.getData());
                    tv_date_.setText(communityDAO.getData_());
                    tv_content.setText(communityDAO.getContent());

                    if(communityDAO.getImage() != null) {
                        Glide.with(CommunityReadActivity.this).load(communityDAO.getImage()).into(iv_image);
                    } else {
                        iv_image.setVisibility(View.GONE);
                    }

                }
            }
        });

    }




}

