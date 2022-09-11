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
import android.widget.LinearLayout;
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
    private String totalAmount;
    private LinearLayout lnHide;

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
        lnHide = findViewById(R.id.lnHide);

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
        if (cartList.size()==0){
            rvCart.setVisibility(View.GONE);
            lnHide.setVisibility(View.VISIBLE);
        }else {
            rvCart.setVisibility(View.VISIBLE);
            lnHide.setVisibility(View.GONE);
        }
        cartAdapter = new CartAdapter(this, cartList, new CartAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(Cart cart, String storeId) {
                cartListChecked.add(cart);
                storeIdList.add(storeId);

                int sum = 0;
                for (int i = 0; i < cartListChecked.size(); i++) {
                    int price = ValidateForm.getPriceToInt(cartListChecked.get(i).getPrice());
                    int amount = Integer.parseInt(cartListChecked.get(i).getAmount());
                    int newPrice = price * amount;
                    sum += newPrice;

                }

                totalAmount = ValidateForm.getDecimalFormattedString(String.valueOf(sum));
                tvPrice.setText(totalAmount);

            }

            @Override
            public void onItemUncheck(Cart cart, String storeId) {
                cartListChecked.remove(cart);
                storeIdList.remove(storeId);

                int sum = 0;

                for (int i = 0; i < cartListChecked.size(); i++) {
                    int price = ValidateForm.getPriceToInt(cartListChecked.get(i).getPrice());
                    int amount = Integer.parseInt(cartListChecked.get(i).getAmount());
                    int newPrice = price * amount;
                    sum += newPrice;

                }
                totalAmount = ValidateForm.getDecimalFormattedString(String.valueOf(sum));
                tvPrice.setText(totalAmount);

            }
        });
        rvCart.setAdapter(cartAdapter);

    }

    /**
     * check if the selected products are in the same store
     * @return
     */
    private boolean compareStoreId() {
        for (int i = 0; i < cartListChecked.size(); i++) {
            for (int j = i + 1; j < cartListChecked.size(); j++) {
                if (!cartListChecked.get(i).getIdShop().equals(cartListChecked.get(j).getIdShop()))
                    return false;
            }
        }
        return true;
    }

    private void openOderScreen() {
        if (cartListChecked.size() == 0) {
            showToast("Bạn vẫn chưa chọn sản phẩm nào để mua", R.drawable.ic_priority);

        } else {


            if (!compareStoreId()) {
                showToast("Hãy chọn sản phẩm cùng cửa hàng", R.drawable.ic_priority);

            } else {
                Intent intent = new Intent(this, OrderActivity.class);
                intent.putParcelableArrayListExtra("cartListChecked", (ArrayList<? extends Parcelable>) cartListChecked);
                intent.putExtra("totalAmount", totalAmount);
                startActivity(intent);
            }

        }

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