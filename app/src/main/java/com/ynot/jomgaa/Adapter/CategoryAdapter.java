package com.ynot.jomgaa.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ynot.jomgaa.Model.Category;
import com.ynot.jomgaa.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    Context context;
    List<Category> allproductsModels;
    LayoutInflater inflater;
    ItemClick listener;

    public CategoryAdapter(Context context, List<Category> allproductsModels, ItemClick listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.allproductsModels = allproductsModels;
        this.listener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_layout, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Category list = allproductsModels.get(position);
        Glide.with(context).load(list.getImg()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.nodata.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.img);
        holder.title.setText(allproductsModels.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.Click(list);
            }
        });


    }

    @Override
    public int getItemCount() {
        return allproductsModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img, nodata;
        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.pic);
            title = itemView.findViewById(R.id.category);
            nodata = itemView.findViewById(R.id.nodata);

        }
    }

    public interface ItemClick {
        void Click(Category list);
    }
}
