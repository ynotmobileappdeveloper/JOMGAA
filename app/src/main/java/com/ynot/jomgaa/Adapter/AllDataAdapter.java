package com.ynot.jomgaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ynot.jomgaa.Model.Products;
import com.ynot.jomgaa.Model.Subcategory;
import com.ynot.jomgaa.R;

import java.util.List;

public class AllDataAdapter extends RecyclerView.Adapter<AllDataAdapter.MyViewHolder> {

    Context context;
    List<Products> model;
    ItemClick listener;
    String page;

    public AllDataAdapter(Context context, List<Products> model, ItemClick listener, String page) {
        this.context = context;
        this.model = model;
        this.listener = listener;
        this.page = page;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allproductlayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Products list = model.get(position);
        Glide.with(context).load(list.getImages()).into(holder.img);
        holder.brand.setText(list.getBrand());
        holder.price.setText("Rs." + String.format("%.2f", Double.parseDouble(list.getPrice())));
        //  Log.e("getFavStatus", model.get(position).getFavStatus());
        if (list.getFavStatus().equals("1")) {
            holder.fav.setImageResource(R.drawable.fav);
        } else {
            holder.fav.setImageResource(R.drawable.favgray);
        }
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.FavClick(list, position);
            }
        });
        holder.categ.setText(list.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.Click(list);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (page.equals("home")) {
            if (model.size() > 4) {
                return 4;
            } else {
                return model.size();
            }

        } else {
            return model.size();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img, fav;
        TextView brand, categ, price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.pic);
            fav = itemView.findViewById(R.id.fav);
            brand = itemView.findViewById(R.id.brand);
            categ = itemView.findViewById(R.id.category);
            price = itemView.findViewById(R.id.price);

        }
    }

    public interface ItemClick {
        void Click(Products list);

        void FavClick(Products list, int position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
