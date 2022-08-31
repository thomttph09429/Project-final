package com.app.projectfinal.order.myOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.app.projectfinal.R;
import com.google.android.material.tabs.TabLayout;

public class MyOrderActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager2;
    private int pos;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initView();
        exist();

    }

    private void exist() {
        ivBack.setOnClickListener(v->{
            finish();
        });
    }

    private void initView() {
        ivBack = findViewById(R.id.ivBack);
        Bundle bundle = getIntent().getExtras();
        pos = bundle.getInt("pos");
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        ViewPagerMyOrderAdapter viewPagerAdapter = new ViewPagerMyOrderAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager2);
        TabLayout.Tab tab = tabLayout.getTabAt(pos);
        tab.select();
    }
}