package com.app.projectfinal.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.app.projectfinal.fragment.HomeFragment;
import com.app.projectfinal.fragment.NotificationsFragment;
import com.app.projectfinal.fragment.UserFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new NotificationsFragment();
            default:
                return new UserFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
