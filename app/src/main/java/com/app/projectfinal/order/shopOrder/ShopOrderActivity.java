package com.app.projectfinal.order.shopOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.ListChatActivity;
import com.app.projectfinal.order.myOrder.ViewPagerMyOrderAdapter;
import com.google.android.material.tabs.TabLayout;

public class ShopOrderActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager2;
    private int pos;
    private ImageView ivBack, ivMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order);
        Bundle bundle = getIntent().getExtras();
        pos = bundle.getInt("pos");
        initView();
        ivBack.setOnClickListener(v -> {
            finish();
        });
        ivMessage.setOnClickListener(v -> {
            startActivity(new Intent(this, ListChatActivity.class));
        });
    }

    private void initView() {
        ivMessage = findViewById(R.id.ivMessage);
        ivBack = findViewById(R.id.ivBack);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        ViewPagerShopOrderAdapter viewPagerAdapter = new ViewPagerShopOrderAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager2);
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        tab.select();
    }
}