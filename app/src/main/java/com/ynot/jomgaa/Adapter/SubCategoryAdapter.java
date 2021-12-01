package com.ynot.jomgaa.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ynot.jomgaa.Model.Subcategory;
import com.ynot.jomgaa.R;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {
    FragmentActivity context;
    List<Subcategory> model;
    ItemClick listener;
    String page;

    public SubCategoryAdapter(FragmentActivity context, List<Subcategory> model, ItemClick listener, String page) {
        this.context = context;
        this.model = model;
        this.listener = listener;
        this.page = page;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_cat_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Subcategory list = model.get(position);
        Glide.with(context).load(list.getImages()).into(holder.img);
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
        ImageView img;
        TextView categ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.pic);
            categ = itemView.findViewById(R.id.category);

        }
    }

    public interface ItemClick {
        void Click(Subcategory list);

    }
}
