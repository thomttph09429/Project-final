package com.app.projectfinal.adapter;

import static com.app.projectfinal.utils.Constant.PHONE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.ChatActivity;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.model.Chat;
import com.app.projectfinal.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListChatAdapter extends RecyclerView.Adapter<ListChatAdapter.MyViewHolder> {
    private Context context;
    private List<User> userChats;
    private String lastMessages;

    public ListChatAdapter(Context context, List<User> userChats) {
        this.context = context;
        this.userChats = userChats;
    }

    @NonNull
    @Override
    public ListChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_chat, parent, false);
        return new ListChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatAdapter.MyViewHolder holder, int position) {
        User userChat = userChats.get(position);
        Glide.with(context).load(userChat.getAvatar()).error(R.drawable.avatar_empty).into(holder.ivAvatarShop);
        holder.tvNameShop.setText(userChat.getName_store());
        checkLastMessage(userChat.getPhone_number(), holder.tvLastMessage);

    // send phone of store when click item in list chat screen
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("phoneOfStore", userChat.getPhone_number());
            bundle.putString("storeName", userChat.getName_store());
            intent.putExtras(bundle);
            context.startActivity(intent);

        });
    }

    private void checkLastMessage( String phoneOfStore, TextView tvLastMessage) {
         String phoneOfMe = SharedPrefsSingleton.getInstance(context.getApplicationContext()).getStringValue(PHONE);
        lastMessages = "default";
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(phoneOfMe) && chat.getSender().equals(phoneOfStore) ||
                            chat.getReceiver().equals(phoneOfStore) && chat.getSender().equals(phoneOfMe)) {
                        lastMessages = chat.getMessage();
                    }
                }
                switch (lastMessages) {
                    case "default":
                        tvLastMessage.setText("Nhắn tin nào");
                        break;

                    default:
                        tvLastMessage.setText(lastMessages);
                        break;
                }
                lastMessages = "default";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userChats.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAvatarShop;
        private TextView tvNameShop, tvLastMessage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatarShop = itemView.findViewById(R.id.ivAvatarShop);
            tvNameShop = itemView.findViewById(R.id.tvNameShop);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);

        }
    }
}
