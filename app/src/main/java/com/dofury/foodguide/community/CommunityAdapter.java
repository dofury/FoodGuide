package com.dofury.foodguide.community;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dofury.foodguide.R;
import com.dofury.foodguide.login.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    private final Context context;
    private final UserAccount userAccount = UserAccount.getInstance();
    private final List<CommunityDAO> communityDAOList;

    public CommunityAdapter(List<CommunityDAO> communityDAOList, Context context) {
        this.context = context;
        this.communityDAOList = communityDAOList;
    }

    @NonNull
    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_community, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.ViewHolder holder, int position) {
        CommunityDAO communityDAO = communityDAOList.get(position);
        holder.tv_title.setText(communityDAO.getTitle());
        holder.tv_nickname.setText(communityDAO.getNickname());
        holder.tv_content.setText(communityDAO.getContent());
        holder.tv_time.setText(communityDAO.getData());


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
        databaseReference.child("Community").child(communityDAO.getcUid()).child("likes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                List<String> likes = new ArrayList<>();
                if(task.getResult().getValue() != null) {
                    likes = new Gson().fromJson(task.getResult().getValue().toString(), new TypeToken<List<String>>(){}.getType());
                    holder.tv_like.setText(String.valueOf(likes.size()));
                } else {
                    holder.tv_like.setText(String.valueOf(likes.size()));
                }
                holder.tv_like.setVisibility(View.VISIBLE);

                boolean flag = false;
                for(String s : likes) {
                    if(s.equals(userAccount.getIdToken())) {
                        flag = true;
                        break;
                    }
                    else {
                        flag = false;
                    }
                }
                holder.tgb_like.setChecked(flag);
                holder.tgb_like.setVisibility(View.VISIBLE);
            }
        });

        int pos = position;

        holder.tgb_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Community").child(communityDAO.getcUid()).child("likes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        List<String> jsonLikesList = new Gson().fromJson(task.getResult().getValue().toString(), new TypeToken<List<String>>(){}.getType());
                        if(holder.tgb_like.isChecked()) {
                            jsonLikesList.add(userAccount.getIdToken());
                        } else {
                            jsonLikesList.remove(userAccount.getIdToken());
                        }

                        String likesJson = new Gson().toJson(jsonLikesList);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
                        databaseReference.child("Community").child(communityDAO.getcUid()).child("likes").setValue(likesJson).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    notifyItemChanged(pos);
                                }
                            }
                        });
                    }
                });
            }
        });

        // 이미지가 있으면 설정
        if(communityDAO.getImage() != null) {
            Glide.with(context).load(communityDAO.getImage()).into(holder.iv_image);
        }

    }

    @Override
    public int getItemCount() {
        return communityDAOList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_title;
        private final TextView tv_nickname;
        private final TextView tv_content;
        private final TextView tv_like;
        private final TextView tv_time;
        private final ImageView iv_image;
        private final ToggleButton tgb_like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_like = itemView.findViewById(R.id.tv_like);
            iv_image = itemView.findViewById(R.id.iv_image);
            tgb_like = itemView.findViewById(R.id.tgb_like);
            tv_time = itemView.findViewById(R.id.tv_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommunityDAO communityDAO = communityDAOList.get(getAdapterPosition());
                    Intent intent = new Intent(context, CommunityReadActivity.class);
                    intent.putExtra("id", communityDAO.getcUid());
                    context.startActivity(intent);
                }
            });
        }
    }
}
