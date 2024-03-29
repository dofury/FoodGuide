package com.dofury.foodguide.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.dofury.foodguide.Food;
import com.dofury.foodguide.R;
import com.dofury.foodguide.login.UserAccount;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class CommunityReadActivity extends AppCompatActivity {
    private TextView tv_title, tv_nickname, tv_date, tv_date_, tv_content,tv_delete, tv_like;
    private EditText et_reply;
    private CircleImageView civ_profile;
    private ToggleButton tgb_like;
    private RecyclerView rv_reply;
    private ImageView btn_send,btn_more;
    private TextView cancel;
    private String key;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
    private RelativeLayout loaderLayout;
    private Boolean loadCheck = false;

    private final UserAccount userAccount = UserAccount.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_read_background);
        Intent intent = getIntent();
        key = intent.getStringExtra("id");
        init();

        loadContent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loaderLayout.setVisibility(View.GONE);
        loadCheck = true;
        Log.d("abc1","check2");
    }



    private void delete() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("게시글 삭제");
        dialog.setMessage("정말로 게시글을 삭제하시겠습니까?");
        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(CommunityReadActivity.this, "글을 삭제하고 있습니다.", Toast.LENGTH_SHORT).show();
                databaseReference.child("Community").child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CommunityReadActivity.this, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                });
            }
        });
        dialog.setNegativeButton("아니요", null);
        dialog.show();
    }

    private void loadContent() {
        databaseReference.child("Community").child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("__FoodGuide__", "글 불러오기 실패");
                }
                else {
                    CommunityDAO communityDAO = task.getResult().getValue(CommunityDAO.class);
                    tv_title.setText(communityDAO.getTitle());
                    tv_nickname.setText(communityDAO.getNickname());
                    tv_date.setText(communityDAO.getData());
                    tv_date_.setText(communityDAO.getData_());
                    tv_content.setText(communityDAO.getContent());

                    List<String>jsonLikeList = new ArrayList<>();
                    if(communityDAO.getLikes() != null) jsonLikeList = new Gson().fromJson(communityDAO.getLikes(), new TypeToken<List<String>>() {}.getType());

                    if(jsonLikeList.isEmpty()) tv_like.setText("0");
                    else tv_like.setText(String.valueOf(jsonLikeList.size()));

                    boolean flag = false;
                    for(String s : jsonLikeList) {
                        if(s.equals(userAccount.getIdToken())) {
                            flag = true;
                            break;
                        } else {
                            flag = false;
                        }
                    }
                    tgb_like.setChecked(flag);

                    tgb_like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseReference.child("Community").child(key).child("likes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    List<String>jsonLikeList = new ArrayList<>();
                                    String taskJson = String.valueOf(task.getResult().getValue());
                                    if(!taskJson.equals("null")) {
                                        jsonLikeList = new Gson().fromJson(taskJson, new TypeToken<List<String>>(){}.getType());
                                    }

                                    if(tgb_like.isChecked()) {
                                        jsonLikeList.add(userAccount.getIdToken());
                                    }
                                    else {
                                        jsonLikeList.remove(userAccount.getIdToken());
                                    }

                                    taskJson = new Gson().toJson(jsonLikeList);

                                    databaseReference.child("Community").child(key).child("likes").setValue(taskJson).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            update();
                                        }
                                    });
                                }
                            });
                        }
                    });

                    if(communityDAO.getImage() != null) {
                        ArrayList<String> imageList = new ArrayList<>();
                        imageList = new Gson().fromJson(communityDAO.getImage(), new TypeToken<List<String>>() {}.getType());
                        ArrayList<String> imageTextList = new ArrayList<>();
                        imageTextList = new Gson().fromJson(communityDAO.getImageContent(), new TypeToken<List<String>>() {}.getType());
                        LinearLayout layout = (LinearLayout) findViewById(R.id.cl_content);
                        // layout param 생성
                        for(int i=0;i<imageList.size();i++)
                        {
                            LinearLayout.LayoutParams layoutParams =

                                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT /* layout_width */, LinearLayout.LayoutParams.WRAP_CONTENT /* layout_height */, 1f /* layout_weight */);


                            layoutParams.topMargin = 100;


                            ImageView iv = new ImageView(getApplicationContext());  // 새로 추가할 imageView 생성
                            TextView tv = new TextView(getApplicationContext());


                            iv.setLayoutParams(layoutParams);  // imageView layout 설정
                            tv.setLayoutParams(layoutParams);


                            layout.addView(iv); // 기존 linearLayout에 imageView 추가
                            layout.addView(tv);
                            Glide.with(CommunityReadActivity.this).load(imageList.get(i)).into(iv);//0번사진 출력
                            iv.setBackground(getResources().getDrawable(R.drawable.custom_image_view1));
                            iv.setClipToOutline(true);
                            tv.setText(imageTextList.get(i));
                            tv.setTextColor(Color.parseColor("#000000"));
                            Typeface typeface = Typeface.createFromAsset(getApplication().getAssets(), "font/maruburi_regular.otf");
                            tv.setTypeface(typeface);
                        }
                    } else {
                    }

                    databaseReference.child("UserAccount").child(communityDAO.getUid()).child("profile").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()) {
                                String profile = String.valueOf(task.getResult().getValue());
                                if(!profile.equals("null")) Glide.with(CommunityReadActivity.this).load(profile).into(civ_profile);
                            }
                        }
                    });

                    String reply = communityDAO.getReply();
                    CommunityReplyAdapter communityReplyAdapter = new CommunityReplyAdapter(reply, CommunityReadActivity.this, key);
                    rv_reply.setHasFixedSize(true);
                    rv_reply.setAdapter(communityReplyAdapter);

                    if(communityDAO.getUid().equals(userAccount.getIdToken())) {
                        tv_delete.setVisibility(View.VISIBLE);
                    }

                    databaseReference.child("Community").child(key).child("reply").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String reply = snapshot.getValue(String.class);
                            CommunityReplyAdapter communityReplyAdapter = new CommunityReplyAdapter(reply, CommunityReadActivity.this, key);
                            rv_reply.setHasFixedSize(true);
                            rv_reply.setAdapter(communityReplyAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }


    private void sendReply() {
        String strReply = et_reply.getText().toString();
        if(strReply.isEmpty()) {
            Toast.makeText(CommunityReadActivity.this, "댓글이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        et_reply.setText(null);
        Toast.makeText(CommunityReadActivity.this, "댓글을 등록중입니다.", Toast.LENGTH_SHORT).show();
        databaseReference.child("Community").child(key).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    CommunityDAO communityDAO = task.getResult().getValue(CommunityDAO.class);
                    List<Reply> replyList = new ArrayList<>();
                    if(communityDAO.getReply() != null) {
                        replyList = new Gson().fromJson(communityDAO.getReply(), new TypeToken<List<Reply>>(){}.getType());
                    }
                    Reply reply = new Reply();
                    reply.setUid(userAccount.getIdToken());
                    reply.setNickname(userAccount.getNickname());
                    reply.setReply(strReply);
                    reply.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    replyList.add(0, reply);
                    String json = new Gson().toJson(replyList);
                    databaseReference.child("Community").child(key).child("reply").setValue(json).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(CommunityReadActivity.this, "댓글이 등록되었습니다.", Toast.LENGTH_SHORT);
                            update();
                        }
                    });
                }
            }
        });

    }

    private void init() {
        FrameLayout contentFrame = findViewById(R.id.community_read_frame);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_community_read,contentFrame,true);
        tv_title = findViewById(R.id.tv_title);
        tv_nickname = findViewById(R.id.tv_nickname);
        tv_date = findViewById(R.id.tv_date);
        tv_date_ = findViewById(R.id.tv_date_);
        tv_content = findViewById(R.id.tv_content);
        et_reply = findViewById(R.id.et_reply);
        civ_profile = findViewById(R.id.civ_profile);
        rv_reply = findViewById(R.id.rv_reply);
        btn_send = findViewById(R.id.iv_send);
        tv_delete = findViewById(R.id.tv_delete);
        tv_like = findViewById(R.id.tv_like);
        tgb_like = findViewById(R.id.tgb_like);
        loaderLayout = findViewById(R.id.loaderLayout);
        loaderLayout.setVisibility(View.VISIBLE);
        cancel = findViewById(R.id.community_top_menu_tv1);
        btn_more = findViewById(R.id.community_top_menu_button3);
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popEvent(v);
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReply();
            }
        });

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void popEvent(View v){
        PopupMenu popupMenu = new PopupMenu(this,v);

        MenuInflater inflater = popupMenu.getMenuInflater();

        Menu menu = popupMenu.getMenu();

        inflater.inflate(R.menu.community_read_menu,menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_tap1:
                        break;
                    case R.id.action_tap2:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    private void update()
    {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}

