package com.ynot.jomgaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ynot.jomgaa.FunctionModels.CartItems;
import com.ynot.jomgaa.Model.CartData;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.View.ProductDescription;

import java.util.List;

public class CartAdapter2 extends RecyclerView.Adapter<CartAdapter2.MyCViewHolder> {
    Context context;
    List<CartData> model;
    Click listener;

    public CartAdapter2(Context context, List<CartData> model, Click listener) {
        this.context = context;
        this.model = model;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlayout, parent, false);
        return new MyCViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCViewHolder holder, @SuppressLint("RecyclerView") int position) {

        CartData list = model.get(position);
        holder.size.setText("Size :" + list.getSize());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.Delete(model.get(position), position);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.Minus(model.get(position));
            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.Plus(model.get(position));
            }
        });

        holder.productname.setText(list.getName());
        holder.price.setText("Rs." + list.getPrice());
        holder.count.setText(list.getQuantity());
        holder.name.setText(list.getBrand());
        Glide.with(context).load(list.getImage()).into(holder.image);
    }


    @Override
    public int getItemCount() {
        return model.size();
    }

    public class MyCViewHolder extends RecyclerView.ViewHolder {

        ImageView delete, image;
        TextView minus, count, plus, price, productname, name, size;

        public MyCViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            minus = itemView.findViewById(R.id.minus);
            count = itemView.findViewById(R.id.count);
            plus = itemView.findViewById(R.id.plus);
            price = itemView.findViewById(R.id.org_price);
            productname = itemView.findViewById(R.id.productname);
            name = itemView.findViewById(R.id.name);
            image = itemView.findViewById(R.id.image);
            size = itemView.findViewById(R.id.size);
        }
    }

    public interface Click {
        void Delete(CartData list, int position);

        void Plus(CartData list);

        void Minus(CartData list);
    }
}
