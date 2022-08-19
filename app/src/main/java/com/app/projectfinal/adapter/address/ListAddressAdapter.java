package com.app.projectfinal.adapter.address;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.address.WardFragment;
import com.app.projectfinal.model.address.AddressUser;
import com.app.projectfinal.model.address.Districts;

import java.util.List;

public class ListAddressAdapter extends RecyclerView.Adapter<ListAddressAdapter.MyViewHolder> {
    private Context context;
    private List<AddressUser> addressUserList;

    public ListAddressAdapter(Context context, List<AddressUser> addressUserList) {
        this.context = context;
        this.addressUserList = addressUserList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvPhoneNumber.setText(addressUserList.get(position).getPhone());
        holder.tvAddress.setText(addressUserList.get(position).getLocation());
        holder.tvUserName.setText(addressUserList.get(position).getCustomerName());
        holder.itemView.setOnClickListener(v->{


        });


    }

    @Override
    public int getItemCount() {
        return addressUserList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserName,tvPhoneNumber,tvAddress;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tvUserName = v.findViewById(R.id.tvUserName);
            tvPhoneNumber= v.findViewById(R.id.tvPhoneNumber);
            tvAddress= v.findViewById(R.id.tvAddress);
        }
    }
}
