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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ynot.jomgaa.Model.Favourite;
import com.ynot.jomgaa.R;

import java.util.List;

public class MyFavAdapter extends RecyclerView.Adapter<MyFavAdapter.MyViewHolder> {
    Context context;
    List<Favourite> allproductsModels;
    LayoutInflater inflater;
    ItemClick listener;

    public MyFavAdapter(Context activity, List<Favourite> allproductsModels, ItemClick listener) {
        this.context = activity;
        this.allproductsModels = allproductsModels;
        this.inflater = LayoutInflater.from(activity);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.allproductlayout, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Favourite list = allproductsModels.get(position);
        holder.fav.setImageResource(R.drawable.fav);
        holder.price.setText(allproductsModels.get(position).getPrice());
        holder.brand.setText(allproductsModels.get(position).getBrand());
        holder.categ.setText(allproductsModels.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.Click(list);
            }
        });
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.FavClick(list, position);
            }
        });
        Glide.with(context).load(list.getImg()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return allproductsModels.size();
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
        void FavClick(Favourite list, int position);

        void Click(Favourite list);
    }
}
