package com.app.projectfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.listener.ListenerUnit;
import com.app.projectfinal.model.Unit;
import com.app.projectfinal.utils.ValidateForm;

import java.util.List;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.MyViewHolder> {
    private List<Unit> unitList;
    private Context context;
    private ListenerUnit listenerUnit;
    int selectedPosition=-1;

    public UnitAdapter(List<Unit> unitList, Context context, ListenerUnit listenerUnit) {
        this.unitList = unitList;
        this.context = context;
        this.listenerUnit= listenerUnit;
    }

    @NonNull
    @Override
    public UnitAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_unit, parent, false);

        return new UnitAdapter.MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull UnitAdapter.MyViewHolder holder, int position) {
        holder.tvUnit.setText(unitList.get(position).getUnitName());
        holder.itemView.setOnClickListener(v -> {
            int position1= holder.getAdapterPosition();
            listenerUnit.onItemClick(unitList.get(position1).getUnitName(), unitList.get(position).getId());
            selectedPosition=position1;
            notifyDataSetChanged();


        });
        if(selectedPosition==position)
        {
            holder.ivMark.setVisibility(View.VISIBLE);
        }
        else
        {

            holder.ivMark.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return unitList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUnit;
        private ImageView ivMark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            ivMark = itemView.findViewById(R.id.ivMark);


        }
    }
}
