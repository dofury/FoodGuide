package com.dofury.foodguide.foodComment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dofury.foodguide.R;
import com.dofury.foodguide.login.UserAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodCommentAdapter extends RecyclerView.Adapter<FoodCommentAdapter.ViewHolder> {
    private final Context context;
    private final List<FoodComment> foodCommentList;
    private final UserAccount userAccount = UserAccount.getInstance();
    private final String foodName;
    public FoodCommentAdapter(Context context, List<FoodComment> foodCommentList, String foodName) {
        this.context = context;
        this.foodCommentList = foodCommentList;
        this.foodName = foodName;
    }

    @NonNull
    @Override
    public FoodCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_one_word_item, parent, false);
        return new FoodCommentAdapter.ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCommentAdapter.ViewHolder holder, int position) {
        FoodComment foodComment = foodCommentList.get(position);
        holder.tv_nickname.setText(foodComment.getName());
        holder.tv_content.setText(foodComment.getContent());
        holder.tv_date.setText(foodComment.getDate());

        if(foodComment.getUid().equals(userAccount.getIdToken())) {
            holder.tv_delete.setVisibility(View.VISIBLE);

            holder.tv_delete.setOnClickListener(view -> {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodGuide");

                databaseReference.child("Food").child(foodName).child("comment").get().addOnCompleteListener(task -> {
                    List<FoodComment> list = new Gson().fromJson(task.getResult().getValue().toString(), new TypeToken<List<FoodComment>>(){}.getType());
                    list.remove(position);

                    String json = new Gson().toJson(list);

                    databaseReference.child("Food").child(foodName).child("comment").setValue(json).addOnCompleteListener(task1 -> {
                        Toast.makeText(context, "한 마디를 삭제 했습니다.", Toast.LENGTH_SHORT).show();
                        notifyItemChanged(position);
                    });
                });
            });
        }

    }

    @Override
    public int getItemCount() {
        return foodCommentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_nickname;
        private final TextView tv_content;
        private final TextView tv_date;
        private final TextView tv_delete;
        private final CircleImageView civ_profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_delete = itemView.findViewById(R.id.tv_delete);
            civ_profile = itemView.findViewById(R.id.civ_profile);
        }
    }
}
