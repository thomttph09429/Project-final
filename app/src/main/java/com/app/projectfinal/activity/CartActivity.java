package com.app.projectfinal.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.projectfinal.R;
import com.app.projectfinal.adapter.CartAdapter;
import com.app.projectfinal.db.Cart;
import com.app.projectfinal.db.CartDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvCart;
    private List<Cart> cartList;
    private ImageView ivBack, ivChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();

        initAction();
        getListCartFromDB();
    }

    private void initAction() {
        cartList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvCart.setLayoutManager(manager);
        ivChat.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    private void initView() {
        rvCart = findViewById(R.id.rvCart);
        ivBack = findViewById(R.id.ivBack);
        ivChat = findViewById(R.id.ivChat);
    }

    private void exit() {
            finish();
    }

    private void openChatScreen() {
        Intent intent = new Intent(this, ListChatActivity.class);
        startActivity(intent);

    }

    private void getListCartFromDB() {
        cartList = CartDatabase.getInstance(this).cartDAO().getCards();
        CartAdapter cartAdapter = new CartAdapter(this, cartList);
        rvCart.setAdapter(cartAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivChat:
                openChatScreen();
                break;
            case R.id.ivBack:
                exit();
                break;
            default:

        }

    }
}