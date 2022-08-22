package com.app.projectfinal.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.order.OrderActivity;
import com.app.projectfinal.adapter.CartAdapter;
import com.app.projectfinal.db.Cart;
import com.app.projectfinal.db.CartDatabase;
import com.app.projectfinal.utils.ValidateForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView rvCart;
    private List<Cart> cartList;
    private ImageView ivBack, ivChat;
    private AppCompatButton btnBuy;
    private TextView tvPrice;
    private List<Cart> cartListChecked = new ArrayList<>();
    private CartAdapter cartAdapter = null;
    private List<String> storeIdList;
    private  List<Integer> positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();

        initAction();
        getListCartFromDB();

    }


    @Override
    protected void onStop() {
        super.onStop();


    }

    private void initAction() {
        cartList = new ArrayList<>();
        storeIdList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        rvCart.setLayoutManager(manager);
        ivChat.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        btnBuy.setOnClickListener(this);
    }

    private void initView() {
        rvCart = findViewById(R.id.rvCart);
        ivBack = findViewById(R.id.ivBack);
        ivChat = findViewById(R.id.ivChat);
        btnBuy = findViewById(R.id.btnBuy);
        tvPrice = findViewById(R.id.tvPrice);
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
        cartAdapter = new CartAdapter(this, cartList, new CartAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(Cart cart) {
                cartListChecked.add(cart);
                int sum = 0;
                for (int i = 0; i < cartListChecked.size(); i++) {
                    int price = ValidateForm.getPriceToInt(cartListChecked.get(i).getPrice());
                    int amount = Integer.parseInt(cartListChecked.get(i).getAmount());
                    int newPrice = price * amount;
                    sum += newPrice;

                }

                String totalAmount = ValidateForm.getDecimalFormattedString(String.valueOf(sum));
                tvPrice.setText(totalAmount);

            }

            @Override
            public void onItemUncheck(Cart cart) {
                cartListChecked.remove(cart);
                int sum = 0;

                for (int i = 0; i < cartListChecked.size(); i++) {
                    int price = ValidateForm.getPriceToInt(cartListChecked.get(i).getPrice());
                    int amount = Integer.parseInt(cartListChecked.get(i).getAmount());
                    int newPrice = price * amount;
                    sum += newPrice;
                }
                String totalAmount = ValidateForm.getDecimalFormattedString(String.valueOf(sum));
                tvPrice.setText(totalAmount);

            }
        });
        rvCart.setAdapter(cartAdapter);

    }

    private void openOderScreen() {
        if (cartListChecked.size() == 0) {
            showToast("Bạn vẫn chưa chọn sản phẩm nào để mua", R.drawable.ic_priority);

        } else {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putParcelableArrayListExtra("cartListChecked", (ArrayList<? extends Parcelable>) cartListChecked);
            startActivity(intent);        }

    }

    private void showToast(String text, int src) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_layout_root));

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        ImageView ivImage = (ImageView) layout.findViewById(R.id.ivImage);
        ivImage.setImageResource(src);

        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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
            case R.id.btnBuy:
                openOderScreen();
                break;
            default:

        }

    }

}