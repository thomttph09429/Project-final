package com.app.projectfinal.adapter.address;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.listener.ListenerChooseAddress;
import com.app.projectfinal.listener.ListenerUnit;
import com.app.projectfinal.model.address.AddressUser;

import java.util.List;

public class ChooseAddressAdapter extends RecyclerView.Adapter<ChooseAddressAdapter.MyViewHolder> {
    private Context context;
    private List<AddressUser> addressUserList;
    private ListenerChooseAddress listenerChooseAddress;

    public ChooseAddressAdapter(Context context, List<AddressUser> addressUserList, ListenerChooseAddress listenerChooseAddress) {
        this.context = context;
        this.addressUserList = addressUserList;
        this.listenerChooseAddress = listenerChooseAddress;
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
            int position1= holder.getAdapterPosition();
            listenerChooseAddress.onItemClick(addressUserList.get(position1).getLocation(), addressUserList.get(position1).getId());
            notifyDataSetChanged();

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
