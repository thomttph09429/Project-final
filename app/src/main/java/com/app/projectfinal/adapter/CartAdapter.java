package com.app.projectfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.projectfinal.R;
import com.app.projectfinal.db.Cart;
import com.bumptech.glide.Glide;

import java.time.temporal.Temporal;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private Context context;
    private List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        Cart cart= cartList.get(position);
        holder.tvNameProduct.setText(cart.getNameProduct());
        holder.tvNameShop.setText(cart.getNameShop());
        holder.tvAmount.setText(cart.getAmount());
        holder.tvPrice.setText(cart.getPrice());
        Glide.with(context).load(cart.getImageOfProduct()).centerCrop().into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNameShop, tvAmount, tvPrice, tvNameProduct;
        private ImageView ivImage;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tvNameShop = v.findViewById(R.id.tvNameShop);
            tvAmount = v.findViewById(R.id.tvAmount);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvNameProduct = v.findViewById(R.id.tvNameProduct);
            ivImage = v.findViewById(R.id.ivImage);
        }
    }
}
