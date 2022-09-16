package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.PHONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.app.projectfinal.R;
import com.app.projectfinal.adapter.ListChatAdapter;
import com.app.projectfinal.utils.SharedPrefsSingleton;
import com.app.projectfinal.model.ChatList;
import com.app.projectfinal.model.User;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListChatActivity extends AppCompatActivity {
    private ListChatAdapter listChatAdapter;
    List<ChatList> userList;
    List<User> mUser;
    private RecyclerView rvListChat;
    private String phoneOfMe;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);
        initView();
        initAction();
        addUser();
        exit();

    }

    private void initView() {
        rvListChat = findViewById(R.id.rvListChat);
        ivBack = findViewById(R.id.ivBack);

    }
    private  void exit(){
        ivBack.setOnClickListener(v->{
            finish();
        });
    }

    private void initAction() {
        userList = new ArrayList<>();
        rvListChat.setLayoutManager(new LinearLayoutManager(ListChatActivity.this));
        rvListChat.setHasFixedSize(true);
        phoneOfMe = SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(PHONE);
    }

    private void addUser() {
        ProgressBarDialog.getInstance(this).showDialog("Đợi một lát!", this);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatList").child(phoneOfMe);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChatList chatList = dataSnapshot.getValue(ChatList.class);
                    userList.add(chatList);
                    Log.e("menu", userList.size()+"");
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     *
     */
    private void chatList() {
        mUser = new ArrayList<>();
   DatabaseReference     reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (ChatList chatlist : userList) {
                        if (user.getPhone_number().equals(chatlist.getPhone_number())) {
                            mUser.add(user);
                        }
                    }
                }
                listChatAdapter = new ListChatAdapter(ListChatActivity.this, mUser);
                rvListChat.setAdapter(listChatAdapter);
                ProgressBarDialog.getInstance(ListChatActivity.this).closeDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}