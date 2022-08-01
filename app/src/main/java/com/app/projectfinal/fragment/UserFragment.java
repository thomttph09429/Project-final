package com.app.projectfinal.fragment;

import static com.app.projectfinal.utils.Constant.STORE_ID;
import static com.app.projectfinal.utils.Constant.USER_NAME_SAVE;

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
import com.app.projectfinal.activity.MyShopActivity;
import com.app.projectfinal.activity.ProfileSettingActivity;
import com.app.projectfinal.activity.SignUpShopActivity;
import com.app.projectfinal.data.SharedPrefsSingleton;

public class UserFragment extends Fragment {
    private LinearLayout lnStartSell,lnSetting;
    private View view;
    private TextView tvMyShop, tvWhenNotSignUp, tvUserName;
    private String isSignUp;

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();
        openMyShop();
        getInformation();
        clickProfileSetting();
        return view;
    }

    /**
     * get user name of user
     */
    private void getInformation() {
        String userName = SharedPrefsSingleton.getInstance(getActivity().getApplicationContext()).getStringValue(USER_NAME_SAVE);
        tvUserName.setText(userName.toString());
    }

    private void openMyShop() {
        tvMyShop.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyShopActivity.class);
            startActivity(intent);
        });
    }

    /**
     * check if the user is registered to sell
     * <pre>
     *     author:ThomTT1
     *     date:31/07/2022
     * </pre>
     */
    private void isSignUpToBecomeSeller() {
        isSignUp = SharedPrefsSingleton.getInstance(getActivity().getApplicationContext()).getStringValue(STORE_ID);
        if (isSignUp.isEmpty()) {
            tvWhenNotSignUp.setVisibility(View.VISIBLE);
            tvMyShop.setVisibility(View.GONE);
            lnStartSell.setVisibility(View.VISIBLE);


        } else {
            lnStartSell.setVisibility(View.GONE);
            tvWhenNotSignUp.setVisibility(View.GONE);
            tvMyShop.setVisibility(View.VISIBLE);


        }

    }

    private void clickStartSell() {
        lnStartSell.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), SignUpShopActivity.class);
                startActivity(intent);

        });


    }

    /**
     * click setting profile
     */
    private void clickProfileSetting(){
        lnSetting.setOnClickListener(v->{
            Intent intent= new Intent(getContext(), ProfileSettingActivity.class);
            startActivity(intent);
        });

    }

    private void initView() {
        tvMyShop = view.findViewById(R.id.tvMyShop);
        lnStartSell = view.findViewById(R.id.lnStartSell);
        tvWhenNotSignUp = view.findViewById(R.id.tvWhenNotSignUp);
        tvUserName = view.findViewById(R.id.tvUserName);
        lnSetting = view.findViewById(R.id.lnSetting);
    }

    @Override
    public void onResume() {
        super.onResume();
        isSignUpToBecomeSeller();
        clickStartSell();
    }
}