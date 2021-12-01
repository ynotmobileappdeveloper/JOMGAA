package com.ynot.jomgaa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ynot.jomgaa.Model.OrderDetails;
import com.ynot.jomgaa.R;

import java.util.ArrayList;

public class OrderDetail extends RecyclerView.Adapter<OrderDetail.ViewHolder> {

    Context context;
    ArrayList<OrderDetails> model;

    public OrderDetail(Context applicationContext, ArrayList<OrderDetails> model) {

        context = applicationContext;
        this.model = model;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.order_desc, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderDetails list = model.get(position);

        holder.name.setText(list.getName());
        holder.qty.setText(list.getQty());
        holder.price.setText("Rs. " + list.getAmount());
        Glide.with(context).load(list.getImage()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, qty;
        ImageView img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewProductName);
            qty = itemView.findViewById(R.id.textViewQuantity);
            price = itemView.findViewById(R.id.tv_sub_total);
            img = itemView.findViewById(R.id.imageViewProduct);
        }
    }
}
