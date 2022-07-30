package com.app.projectfinal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.AddProductActivity;
import com.app.projectfinal.activity.SignUpShopActivity;

public class UserFragment extends Fragment {
    private LinearLayout lnSignUpSell;
    private View view;
    private TextView tvStartSell;

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();
        signupToBecomeSeller();
        openMyShop();
        return view;
    }

    private void signupToBecomeSeller() {
        lnSignUpSell.setOnClickListener(v->{
            Intent intent= new Intent(getActivity(), SignUpShopActivity.class);
            startActivity(intent);
        });


    }
    private void openMyShop() {
        tvStartSell.setOnClickListener(v->{
            Intent intent= new Intent(getActivity(), AddProductActivity.class);
            startActivity(intent);
        });


    }
    private void initView() {
        tvStartSell=view.findViewById(R.id.tv_start_sell);
        lnSignUpSell = view.findViewById(R.id.ln_sign_up_sell);
    }

}