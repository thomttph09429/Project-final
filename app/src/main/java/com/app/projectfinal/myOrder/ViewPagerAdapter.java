package com.app.projectfinal.myOrder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.projectfinal.myOrder.fragment.CompleteFragment;
import com.app.projectfinal.myOrder.fragment.DeliveryFragment;
import com.app.projectfinal.myOrder.fragment.WaitFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WaitFragment();
            case 1:
                return new DeliveryFragment();
            case 2:
                return new CompleteFragment();
            default:
                return new  WaitFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
               return "Chờ xác nhận";
            case 1:
               return  "Đang giao";
            case 2:
                return  "Hoàn thành";
        }
        return null;

    }
}
