package com.app.projectfinal.bottom;

import static com.app.projectfinal.utils.Constant.IMAGE1_PRODUCT;
import static com.app.projectfinal.utils.Constant.PRICE_PRODUCT;
import static com.app.projectfinal.utils.Constant.QUANTITY_PRODUCT;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.DetailProductActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;


public class ChooseCartFragment extends BottomSheetDialogFragment {
    private View view;
    private ImageView ivProduct, ivClose;
    private TextView tvPrice, tvQuantity;


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
        clickImageClose();
        return view;

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

    }

    /**
     * get information of product (pass  from DetailProductActivity)
     * <pre>
     *     author:ThomTT
     *     date:01/08/2022
     * </pre>
     */
    private void getInfo() {
        String price = getArguments().getString(PRICE_PRODUCT);
        String image = getArguments().getString(IMAGE1_PRODUCT);
        String quantity = getArguments().getString(QUANTITY_PRODUCT);
        tvQuantity.setText(quantity);
        tvPrice.setText(price);
        Glide.with(getContext()).load(image).into(ivProduct);

    }

    private void clickImageClose() {
        ivClose.setOnClickListener(v -> {
            dismiss();
        });
    }


}