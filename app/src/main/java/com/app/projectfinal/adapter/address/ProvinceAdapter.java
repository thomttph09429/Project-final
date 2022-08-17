package com.app.projectfinal.adapter.address;

import static com.app.projectfinal.utils.Constant.CODE_PROVINCE;
import static com.app.projectfinal.utils.Constant.PROVINCE_NAME;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.address.AddAddressActivity;
import com.app.projectfinal.activity.address.DistrictFragment;
import com.app.projectfinal.model.address.Districts;
import com.app.projectfinal.model.address.Province;

import java.util.List;

public class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.MyViewHolder> {
    private Context context;
    private List<Province> provinceList;

    public ProvinceAdapter(Context context, List<Province> provinceList) {
        this.context = context;
        this.provinceList = provinceList;
    }

    @NonNull
    @Override
    public ProvinceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_province, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText(provinceList.get(position).getName());
        int code = provinceList.get(position).getCode();
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt(CODE_PROVINCE, code);
            bundle.putString(PROVINCE_NAME,provinceList.get(position).getName());
            DistrictFragment districtFragment = new DistrictFragment();
            districtFragment.setArguments(bundle);
            districtFragment.show(  ((AppCompatActivity) context).getSupportFragmentManager(),"districtFragment");


        });

    }

    @Override
    public int getItemCount() {
        return provinceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
        }
    }
}
