package com.app.projectfinal.adapter;

import static com.app.projectfinal.utils.Constant.MSG_LEFT;
import static com.app.projectfinal.utils.Constant.MSG_RIGHT;
import static com.app.projectfinal.utils.Constant.PHONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.utils.SharedPrefsSingleton;
import com.app.projectfinal.model.Chat;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private Context context;
    private List<Chat> chatList;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_right, parent, false);
            return new ChatAdapter.MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_left, parent, false);
            return new ChatAdapter.MyViewHolder(view);
        }    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.MyViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.tvMessage.setText(chat.getMessage());
        holder.tvMessage.setOnClickListener(v -> {
            if (holder.tvTime.getVisibility()== View.VISIBLE){
                holder.tvTime.setVisibility(View.GONE);

            }else {
                holder.tvTime.setText(chat.getTime());
                holder.tvTime.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage, tvTime;
        private CircleImageView  ivAvatar;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage=itemView.findViewById(R.id.tv_message);
            ivAvatar=itemView.findViewById(R.id.iv_avartar);
            tvTime=itemView.findViewById(R.id.tv_time);
        }
    }
    @Override
    public int getItemViewType(int position) {
         String phoneOfMe= SharedPrefsSingleton.getInstance(context.getApplicationContext()).getStringValue(PHONE);
        if (chatList.get(position).getSender().equals(phoneOfMe)) {
            return MSG_RIGHT;
        } else {
            return MSG_LEFT;
        }

    }
}
