package com.app.projectfinal.activity;

import static com.app.projectfinal.utils.Constant.ADD_STORES;
import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.PHONE;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRODUCTS;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.UPDATE_USER;
import static com.app.projectfinal.utils.Constant.USER_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.adapter.ChatAdapter;
import com.app.projectfinal.adapter.ProductAdapter;
import com.app.projectfinal.data.SharedPrefsSingleton;
import com.app.projectfinal.model.Chat;
import com.app.projectfinal.model.Product;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.ValidateForm;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private String storeName, storeId, idProduct, userId, phoneOfStore, phoneOfMe;
    private TextView tvNameShop;
    private ImageView btnSentMessage;
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
        receiveInfoStoreFromClickItem();
        receiveInfoStore();
        getDetailProduct();
        showInfoStore();
        clickSendMessage();


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
        idProduct = data.getString(ID_PRODUCT);
        Log.e("ChatActivity", storeId + "and" + idProduct);


    }

    private void getDetailProduct() {
    String url = PRODUCTS + "/" + idProduct;
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (response != null) {
                try {
                    JSONObject jsonObject = response.getJSONObject("data");
                    JSONObject data = jsonObject.getJSONObject("product");
                    phoneOfStore = data.getString(PHONE);
                    readMessage();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ChatActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                }
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(ChatActivity.this, error.toString(), Toast.LENGTH_LONG).show();

        }
    });
    VolleySingleton.getInstance(ChatActivity.this).getRequestQueue().add(jsonObjectRequest);



    }

    private void showInfoStore() {
            tvNameShop.setText(ValidateForm.capitalizeFirst(storeName));

    }


    private void clickSendMessage() {

        btnSentMessage.setOnClickListener(v -> {
            String message = edtEnterMessage.getText().toString();
            if (!message.equals("")) {
                    sendMessage(phoneOfMe, phoneOfStore, message);



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
    private void sendMessage(String sender, String receiver, String message) {
        AddInfoOfStoreToFirebase();
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
                .child(phoneOfStore);


        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("phone_number").setValue(phoneOfStore);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(phoneOfStore)
                .child(phoneOfMe);
        chatRefReceiver.child("phone_number").setValue(phoneOfMe);
    }

    private void AddInfoOfStoreToFirebase() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(phoneOfStore);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reference.child("name_store").setValue(storeName);
                reference.child("avatar").setValue("yy");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void readMessage() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                        if (chat.getReceiver().equals(phoneOfMe) && chat.getSender().equals(phoneOfStore)
                                || chat.getReceiver().equals(phoneOfStore) && chat.getSender().equals(phoneOfMe)) {
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