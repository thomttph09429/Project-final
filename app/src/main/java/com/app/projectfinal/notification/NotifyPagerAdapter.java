package com.app.projectfinal.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.projectfinal.order.myOrder.fragment.CancelFragment;
import com.app.projectfinal.order.myOrder.fragment.CompleteFragment;
import com.app.projectfinal.order.myOrder.fragment.DeliveryFragment;
import com.app.projectfinal.order.myOrder.fragment.WaitFragment;

public class NotifyPagerAdapter extends FragmentStatePagerAdapter {


    public NotifyPagerAdapter(@NonNull FragmentManager fm, int behavior ) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new MyNotifiFragment();
            case 1:
                return new ShopNotifiFragment();

            default:
                return new MyNotifiFragment();


        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
                return "Thông báo của tôi";
            case 1:
                return "Cập nhật người bán";

        }
        return null;

    }
}
