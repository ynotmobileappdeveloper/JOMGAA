package com.ynot.jomgaa.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ynot.jomgaa.Model.AllproductsModel;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.View.ProductDescription;

import java.util.List;

public class CategoryAdapter2 extends RecyclerView.Adapter<CategoryAdapter2.MyViewHolder> {
    Context context;

      List<AllproductsModel> allproductsModels;
      LayoutInflater inflater;
       public CategoryAdapter2(Context context, List<AllproductsModel> allproductsModels) {
        this.context=context;
           inflater=LayoutInflater.from(context);
        this.allproductsModels=allproductsModels;
      }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.allproductlayout2,null);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {   if (position==1){
        holder.fav.setImageResource(R.drawable.fav);
    }
        holder.price.setText(allproductsModels.get(position).getPrice());
        holder.img.setBackgroundResource(allproductsModels.get(position).getImg());
        holder.brand.setText(allproductsModels.get(position).getBrand());
        holder.categ.setText(allproductsModels.get(position).getCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ProductDescription.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return allproductsModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img,fav;
        TextView brand,categ,price;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.pic);
            fav=itemView.findViewById(R.id.fav);
            brand=itemView.findViewById(R.id.brand);
            categ=itemView.findViewById(R.id.category);
            price=itemView.findViewById(R.id.price);

        }
    }
}
