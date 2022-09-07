package com.dofury.foodguide.community;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class CommunityReplyAdapter extends RecyclerView.Adapter<CommunityReplyAdapter.ViewHolder> {
    private final UserAccount userAccount = UserAccount.getInstance();
    private final Context context;
    private List<Reply> replyList = new ArrayList<>();
    private final String key;

    public CommunityReplyAdapter(String stringJson, Context context, String key) {
        this.context = context;
        if(stringJson != null) {
            replyList = new Gson().fromJson(stringJson, new TypeToken<List<Reply>>(){}.getType());
        }
        this.key = key;
    }

    @NonNull
    @Override
    public CommunityReplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_community_reply, parent, false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityReplyAdapter.ViewHolder holder, int position) {
        int pos = position;
        if(replyList.isEmpty()) {
            return;
        }
        Reply reply = replyList.get(pos);
        holder.tv_reply.setText(reply.getReply());
        holder.tv_nickname.setText(reply.getNickname());
        holder.tv_date.setText(reply.getDate());

        if(userAccount.getIdToken().equals(reply.getUid())) {
            holder.tv_delete.setVisibility(View.VISIBLE);
        }

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(pos);

            }
        });
    }

    private void delete(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("댓글 삭제");
        dialog.setMessage("정말로 댓글을 삭제하시겠습니까?");
        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "댓글을 삭제하고 있습니다.", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");
                databaseReference.child("Community").child(key).child("reply").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            String reply = task.getResult().getValue().toString();
                            List<Reply> replyList = new Gson().fromJson(reply, new TypeToken<List<Reply>>(){}.getType());
                            replyList.remove(position);
                            reply = new Gson().toJson(replyList);
                            databaseReference.child("Community").child(key).child("reply").setValue(reply).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(context, "댓글을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        dialog.setNegativeButton("아니요", null);
        dialog.show();
    }



    @Override
    public int getItemCount() {
        return replyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_nickname;
        private final TextView tv_reply;
        private final TextView tv_date;
        private final TextView tv_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_reply = itemView.findViewById(R.id.tv_reply);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_delete = itemView.findViewById(R.id.tv_delete);


        }
    }
}
