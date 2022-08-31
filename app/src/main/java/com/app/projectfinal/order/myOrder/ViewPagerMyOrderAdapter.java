package com.app.projectfinal.order.myOrder;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.projectfinal.order.myOrder.fragment.CancelFragment;
import com.app.projectfinal.order.myOrder.fragment.CompleteFragment;
import com.app.projectfinal.order.myOrder.fragment.DeliveryFragment;
import com.app.projectfinal.order.myOrder.fragment.WaitFragment;

public class ViewPagerMyOrderAdapter extends FragmentStatePagerAdapter {


    public ViewPagerMyOrderAdapter(@NonNull FragmentManager fm, int behavior ) {
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
            case 3:
                return new CancelFragment();
            default:
                return new DeliveryFragment();


        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
                return "Chờ xác nhận";
            case 1:
                return "Đang giao";
            case 2:
                return "Đã giao";
            case 3:
                return "Đã hủy";
        }
        return null;

    }
}
