package com.app.projectfinal.order.shopOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.app.projectfinal.R;
import com.app.projectfinal.order.myOrder.ViewPagerMyOrderAdapter;
import com.google.android.material.tabs.TabLayout;

public class ShopOrderActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order);
        initView();

    }

    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        ViewPagerShopOrderAdapter viewPagerAdapter = new ViewPagerShopOrderAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager2);
    }
}