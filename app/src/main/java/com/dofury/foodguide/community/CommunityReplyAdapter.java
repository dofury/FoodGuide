package com.dofury.foodguide.community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dofury.foodguide.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CommunityReplyAdapter extends RecyclerView.Adapter<CommunityReplyAdapter.ViewHolder> {

    private Context context;
    private List<Reply> replyList;

    public CommunityReplyAdapter(String stringJson, Context context) {
        this.context = context;
        replyList = new Gson().fromJson(stringJson, new TypeToken<List<Reply>>(){}.getType());
    }

    @NonNull
    @Override
    public CommunityReplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_community_reply, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityReplyAdapter.ViewHolder holder, int position) {
        Reply reply =replyList.get(position);
        holder.tv_reply.setText(reply.getReply());
        holder.tv_nickname.setText(reply.getNickname());
        holder.tv_date.setText(reply.getDate());
    }

    @Override
    public int getItemCount() {
        return replyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nickname, tv_reply, tv_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_reply = itemView.findViewById(R.id.tv_reply);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
