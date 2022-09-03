package com.dofury.foodguide.community;

import android.content.Context;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    private Context context;
    private UserAccount userAccount = UserAccount.getInstance();
    private List<CommunityDAO> communityDAOList;

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
        // 좋아요 누른 사람들의 닉네임을 List에 넣음
        List<String> likes = new ArrayList<>();
        if(communityDAO.getLikes() != null) {
            likes = new Gson().fromJson(communityDAO.getLikes(), new TypeToken<List<String>>(){}.getType());
            holder.tv_like.setText(likes.size());
        }


        
        // 일단 false 하고 자기 닉네임이 있으면 true
        holder.tgb_like.setChecked(false);
        for(String s : likes) {
            if(s.equals(userAccount.getNickname())) {
                holder.tgb_like.setChecked(true);
                break;
            }
        }
        
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
        private TextView tv_title, tv_nickname, tv_content, tv_like, tv_time;
        private ImageView iv_image;
        private ToggleButton tgb_like;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_like = itemView.findViewById(R.id.tv_like);
            iv_image = itemView.findViewById(R.id.iv_image);
            tgb_like = itemView.findViewById(R.id.tgb_like);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
