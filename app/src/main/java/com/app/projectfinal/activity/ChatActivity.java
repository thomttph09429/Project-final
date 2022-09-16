package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.AVATAR;
import static com.app.projectfinal.utils.Constant.PHONE;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.USER_NAME_SAVE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectfinal.R;
import com.app.projectfinal.adapter.ChatAdapter;
import com.app.projectfinal.utils.SharedPrefsSingleton;
import com.app.projectfinal.model.Chat;

import com.app.projectfinal.utils.ValidateForm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private String storeName, storeId, userId, phoneOfStore, phoneOfMe, avatar;
    private TextView tvNameShop;
    private ImageView btnSentMessage, ivBack;
    private EditText edtEnterMessage;
    private RecyclerView rvChat;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList;
    //getPhoneOfStore and getStoreName
    // receive  data from click item in list chat screen
    private String getPhoneOfStore, getStoreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initAction();
        exit();
        receiveInfoStoreFromClickItem();
        receiveInfoStore();


        if (getPhoneOfStore != null) {
            showInfoStore(getStoreName);
            readMessage(getPhoneOfStore);
            clickSendMessage(getPhoneOfStore, getStoreName);
            Toast.makeText(this, "getPhoneOfStore", Toast.LENGTH_LONG).show();

        } else {
            showInfoStore(storeName);
            readMessage(phoneOfStore);
            clickSendMessage(phoneOfStore, storeName);
            Toast.makeText(this, "PhoneOfStore", Toast.LENGTH_LONG).show();

        }

    }

    private void exit() {
        ivBack.setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * when click item in  list chat screen, receive phone of store from ListChatAdapter
     * <pre>
     *     author:ThomTT
     *     date:04/08/2022
     * </pre>
     */
    private void receiveInfoStoreFromClickItem() {
        Bundle bundle = getIntent().getExtras();
        getPhoneOfStore = bundle.getString("phoneOfStore");
        getStoreName = bundle.getString("storeName");


    }

    private void initAction() {
        chatList = new ArrayList<>();
        phoneOfMe = SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(PHONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        rvChat.setHasFixedSize(true);
        rvChat.setLayoutManager(layoutManager);

    }


    private void initView() {
        tvNameShop = findViewById(R.id.tvNameShop);
        btnSentMessage = findViewById(R.id.btnSentMessage);
        edtEnterMessage = findViewById(R.id.edtEnterMessage);
        rvChat = findViewById(R.id.rvChat);
        ivBack = findViewById(R.id.ivBack);

    }

    /**
     * receive information of store from user click "chat" at DetailProductActivity
     * pass data from DetailProductActivity
     * <pre>
     *     author:ThomTT1
     *     date: 08/03/2022
     * </pre>
     */
    private void receiveInfoStore() {
        Bundle data = getIntent().getExtras();
        storeName = data.getString(STORE_NAME_PRODUCT);
        storeId = data.getString(STORE_ID_PRODUCT);
        phoneOfStore = data.getString(PHONE);
        avatar = data.getString(AVATAR);
        Log.e("ChatActivity", storeId + "and" + phoneOfStore);


    }


    private void showInfoStore(String nameStore) {
        tvNameShop.setText(ValidateForm.capitalizeFirst(nameStore));

    }


    private void clickSendMessage(String phone, String store) {

        btnSentMessage.setOnClickListener(v -> {
            String message = edtEnterMessage.getText().toString();
            if (!message.equals("")) {
                sendMessage(phoneOfMe, phone, message, store);


            }
            edtEnterMessage.setText("");

        });
    }

    /**
     * send message
     *
     * @param sender
     * @param receiver
     * @param message
     */
    private void sendMessage(String sender, String receiver, String message, String store) {
        AddInfoOfStoreToFirebase(receiver, store);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isSeen", "false");
        hashMap.put("time", ValidateForm.getDateAndTime());
        reference.child("Chats").push().setValue(hashMap);
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(phoneOfMe)
                .child(receiver);


        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("phone_number").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(receiver)
                .child(phoneOfMe);
        chatRefReceiver.child("phone_number").setValue(phoneOfMe);
    }

    private void AddInfoOfStoreToFirebase(String phone, String store) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reference.child("name_store").setValue(store);
                reference.child("avatar").setValue(avatar);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference references = FirebaseDatabase.getInstance().getReference().child("Users").child(phoneOfMe);
        references.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = SharedPrefsSingleton.getInstance(getApplicationContext()).getStringValue(USER_NAME_SAVE);
                references.child("name_store").setValue(userName);
                references.child("avatar").setValue("yy");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessage(String phone) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(phoneOfMe) && chat.getSender().equals(phone)
                            || chat.getReceiver().equals(phone) && chat.getSender().equals(phoneOfMe)) {
                        chatList.add(chat);


                    }

                    chatAdapter = new ChatAdapter(ChatActivity.this, chatList);
                    rvChat.setAdapter(chatAdapter);
                }
//                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}