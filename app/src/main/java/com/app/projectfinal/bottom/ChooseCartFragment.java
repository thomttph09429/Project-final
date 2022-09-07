package com.app.projectfinal.bottom;

import static com.app.projectfinal.utils.Constant.BUY_NOW;
import static com.app.projectfinal.utils.Constant.CATEGORY_NAME;
import static com.app.projectfinal.utils.Constant.DESCRIPTION_PRODUCT;
import static com.app.projectfinal.utils.Constant.ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_PRODUCT;
import static com.app.projectfinal.utils.Constant.NAME_STORE;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;
import static com.app.projectfinal.utils.Constant.STORE_ID_PRODUCT;
import static com.app.projectfinal.utils.Constant.UNIT_NAME;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
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
import com.app.projectfinal.activity.order.OrderActivity;
import com.app.projectfinal.db.Cart;
import com.app.projectfinal.db.CartDatabase;
import com.app.projectfinal.utils.ValidateForm;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ChooseCartFragment extends BottomSheetDialogFragment {
    private View view;
    private ImageView ivProduct, ivClose;
    private TextView tvPrice, tvQuantity, tvRaiseAmount, tvAmount, tvReduceAmount, tvUnit;
    private String price, image, idProduct, idShop, nameProduct, quantity, nameStore, unitName, description, categoryName, isBuyNow;
    private AppCompatButton btnAddToCart;
    private List<Cart> cartListChecked = new ArrayList<>();


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
            int totalAmount = Integer.valueOf(amount)  *ValidateForm.getPriceToInt(price);
            cartListChecked.add(new Cart(idProduct, idShop, nameProduct, amount, price, image, nameStore, unitName, quantity, description, categoryName));
            if (!isBuyNow.equals(BUY_NOW)) {
                if (amount.equals("0")) {
                    showToast("Bạn vẫn chưa chọn số lượng", R.drawable.ic_priority);
                } else {
                    CartDatabase.getInstance(getContext()).cartDAO().insert(new Cart(idProduct, idShop, nameProduct, amount, price, image, nameStore, unitName, quantity, description, categoryName));
                    showToast("Đã thêm vào giỏ", R.drawable.ic_mark);
                    dismiss();
                }
            } else {
                if (amount.equals("0")) {
                    showToast("Bạn vẫn chưa chọn số lượng", R.drawable.ic_priority);
                } else {
                    dismiss();
                    Intent intent = new Intent(getContext(), OrderActivity.class);
                    intent.putParcelableArrayListExtra("cartListChecked", (ArrayList<? extends Parcelable>) cartListChecked);
                    intent.putExtra("totalAmount", ValidateForm.getDecimalFormattedString(String.valueOf(totalAmount)));
                    startActivity(intent);
                }
            }


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
        tvUnit = view.findViewById(R.id.tvUnit);
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
        unitName = getArguments().getString(UNIT_NAME);
        quantity = getArguments().getString(QUANTITY_PRODUCT);
        description = getArguments().getString(DESCRIPTION_PRODUCT);
        categoryName = getArguments().getString(CATEGORY_NAME);
        isBuyNow = getArguments().getString(BUY_NOW);
        tvQuantity.setText(quantity);
        tvPrice.setText(price);
        tvUnit.setText(unitName);
        Glide.with(getContext()).load(image).into(ivProduct);
        if (isBuyNow.equals(BUY_NOW)) {
            btnAddToCart.setText("Mua ngay");
        }

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


    private void showToast(String text, int src) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.toast_layout_root));

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);
        ImageView ivImage = (ImageView) layout.findViewById(R.id.ivImage);
        ivImage.setImageResource(src);

        Toast toast = new Toast(getContext().getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void checkStillInStock() {
        if (ValidateForm.getPriceToInt(price) <= 0) {
            tvAmount.setText("0");
        }
    }
}