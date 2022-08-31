package com.app.projectfinal.order.shopOrder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.projectfinal.order.myOrder.fragment.CompleteFragment;
import com.app.projectfinal.order.myOrder.fragment.DeliveryFragment;
import com.app.projectfinal.order.myOrder.fragment.WaitFragment;
import com.app.projectfinal.order.shopOrder.fragment.CancelConfirmFragment;
import com.app.projectfinal.order.shopOrder.fragment.CompleteConfirmFragment;
import com.app.projectfinal.order.shopOrder.fragment.DeliveryConfirmFragment;
import com.app.projectfinal.order.shopOrder.fragment.WaitConfirmFragment;

public class ViewPagerShopOrderAdapter extends FragmentStatePagerAdapter {


    public ViewPagerShopOrderAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WaitConfirmFragment();
            case 1:
                return new DeliveryConfirmFragment();
            case 2:
                return new CompleteConfirmFragment();
            case 3:
                return new CancelConfirmFragment();
            default:
                return new  WaitConfirmFragment();
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
               return  "Đang giao";
            case 2:
                return  "Đã giao";
            case 3:
                return  "Đã hủy";
        }
        return null;

    }
}
