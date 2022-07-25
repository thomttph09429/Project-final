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
import com.app.projectfinal.activity.myshop;
import com.app.projectfinal.activity.shop_settings;

public class UserFragment extends Fragment {
    private LinearLayout lnStartSell;
    private View view;

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();
        lnStartSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), AddProductActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView() {
        lnStartSell = view.findViewById(R.id.ln_start_sell);

    }

}