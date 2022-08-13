package com.app.projectfinal.bottom;

import static com.app.projectfinal.utils.Constant.ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_STORE;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.DetailProductActivity;
import com.app.projectfinal.db.Cart;
import com.app.projectfinal.db.CartDatabase;
import com.app.projectfinal.utils.ValidateForm;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;


public class ChooseCartFragment extends BottomSheetDialogFragment {
    private View view;
    private ImageView ivProduct, ivClose;
    private TextView tvPrice, tvQuantity, tvRaiseAmount, tvAmount, tvReduceAmount;
    private String price, image, idProduct, idShop, nameProduct, quantity, nameStore;
    private AppCompatButton btnAddToCart;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_choose_cart, container, false);

        }
        initView();
        getInfo();
        checkStillInStock();
        clickImageClose();
        reduceAmount();
        raiseAmount();
        addToCart();
        return view;

    }

    /**
     * add to cart
     * <pre>
     *     author:ThomTT
     *     date: 11/08/2022
     *     todo
     * </pre>
     */
    private void addToCart() {
        btnAddToCart.setOnClickListener(v -> {
            String amount = tvAmount.getText().toString();
            CartDatabase.getInstance(getContext()).cartDAO().insert(new Cart(idProduct, idShop, nameProduct, amount, price, image, nameStore));
            showToast();
            dismiss();
        });


    }

    /**
     * init view
     * <pre>
     *     author:ThomTT
     *     date:01/08/2022
     * </pre>
     */
    private void initView() {
        tvPrice = view.findViewById(R.id.tvPrice);
        ivProduct = view.findViewById(R.id.ivProduct);
        tvQuantity = view.findViewById(R.id.tvQuantity);
        ivClose = view.findViewById(R.id.ivClose);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvRaiseAmount = view.findViewById(R.id.tvRaiseAmount);
        tvReduceAmount = view.findViewById(R.id.tvReduceAmount);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);

    }

    /**
     * get information of product (pass  from DetailProductActivity)
     * <pre>
     *     author:ThomTT
     *     date:01/08/2022
     * </pre>
     */
    private void getInfo() {
        price = getArguments().getString(PRICE_PRODUCT);
        image = getArguments().getString(IMAGE1_PRODUCT);
        idProduct = getArguments().getString(ID_PRODUCT);
        idShop = getArguments().getString(STORE_ID_PRODUCT);
        nameProduct = getArguments().getString(NAME_PRODUCT);
        nameStore = getArguments().getString(NAME_STORE);

        quantity = getArguments().getString(QUANTITY_PRODUCT);
        tvQuantity.setText(quantity);
        tvPrice.setText(price);
        Glide.with(getContext()).load(image).into(ivProduct);

    }

    private void clickImageClose() {
        ivClose.setOnClickListener(v -> {
            dismiss();
        });
    }

    /**
     * reduce amount when click (-)
     * <pre>
     *     author:ThomTT
     *     date:11/08/2022
     * </pre>
     */
    private void reduceAmount() {
        tvReduceAmount.setOnClickListener(v -> {
            int amount = Integer.parseInt(tvAmount.getText().toString());
            if (amount >= 1) {
                String afterClick = String.valueOf(amount - 1);
                tvAmount.setText(afterClick);

            }

        });

    }

    /**
     * raise amount when click (+)
     * <pre>
     *     author:ThomTT
     *     date:11/08/2022
     * </pre>
     */
    private void raiseAmount() {
        tvRaiseAmount.setOnClickListener(v -> {
            int amount = Integer.parseInt(tvAmount.getText().toString());
            int quantities = Integer.parseInt(quantity);
            if (amount <= (quantities - 1)) {
                String afterClick = String.valueOf(amount + 1);
                tvAmount.setText(afterClick);

            }
        });

    }

    private void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.toast_layout_root));

//        ImageView image = (ImageView) layout.findViewById(R.id.image);
//        image.setImageResource(R.drawable.android);
//        TextView text = (TextView) layout.findViewById(R.id.text);
//        text.setText("Hello! This is a custom toast!");

        Toast toast = new Toast(getContext().getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
    private  void  checkStillInStock(){
        if (ValidateForm.getPriceToInt(price)<=0){
            tvAmount.setText("0");
        }
    }
}