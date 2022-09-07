package com.app.projectfinal.fragment;

import static com.app.projectfinal.utils.Constant.ORDER;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.projectfinal.R;
import com.app.projectfinal.activity.CartActivity;
import com.app.projectfinal.activity.ListChatActivity;
import com.app.projectfinal.adapter.NotifyAdapter;
import com.app.projectfinal.model.order.DetailOrder;
import com.app.projectfinal.notification.NotifyPagerAdapter;
import com.app.projectfinal.order.myOrder.ViewPagerMyOrderAdapter;
import com.app.projectfinal.utils.ConstantData;
import com.app.projectfinal.utils.ProgressBarDialog;
import com.app.projectfinal.utils.VolleySingleton;
import com.google.android.material.tabs.TabLayout;


public class NotificationsFragment extends Fragment {
    private View view;

    private TabLayout tabLayout;
    private ViewPager viewPager2;
    private ImageView ivMessage, ivCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null)
            view = inflater.inflate(R.layout.fragment_notifications, container, false);

        initView();
        clickCart();
        clickMessage();


        NotifyPagerAdapter notifyAdapter = new NotifyPagerAdapter(getFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager2.setAdapter(notifyAdapter);
        tabLayout.setupWithViewPager(viewPager2);


        return view;
    }

    private void initView() {
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);
        ivMessage = view.findViewById(R.id.ivMessage);
        ivCart = view.findViewById(R.id.ivCart);

    }

    private void clickMessage() {
        ivMessage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListChatActivity.class);
            startActivity(intent);
        });


    }

    private void clickCart() {
        ivCart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });
    }

}