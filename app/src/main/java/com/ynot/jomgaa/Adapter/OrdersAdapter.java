package com.ynot.jomgaa.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.ynot.jomgaa.Model.OrderModel;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.View.OrderDescription;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    Context context;
    ArrayList<OrderModel> model;
    Animation animation;
    ButtonClick listener;
    String id;
    int count;


    public OrdersAdapter(Context applicationContext, ArrayList<OrderModel> model, ButtonClick listener) {
        this.context = applicationContext;
        this.model = model;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.orders_layout2, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final OrderModel list = model.get(position);


        animation = AnimationUtils.loadAnimation(context, R.anim.left_to_right);
        holder.itemView.startAnimation(animation);
        holder.order_no.setText(list.getOrder_no());
        holder.statusbtn.setText(list.getStatus());
        holder.p_date.setText(list.getP_date());
        if (!list.getCoupon_amount().equals("0.00") || !list.getCoupon_code().isEmpty()) {
            holder.coupon_layout.setVisibility(View.VISIBLE);
            holder.coupon_code.setText(list.getCoupon_code());
            holder.coupon_amount.setText("Rs. " + list.getCoupon_amount());
        } else {
            holder.coupon_layout.setVisibility(View.GONE);
        }


//        if (list.getDelivery_charge().isEmpty()) {
//            holder.del_layout.setVisibility(View.GONE);
//        } else {
//            holder.del_layout.setVisibility(View.VISIBLE);
//            holder.d_charge.setText("Rs. " + list.getDelivery_charge());
//        }
        holder.delivery_charge.setText(list.getDelivery_charge());
        holder.qty.setText(list.getQty());
        holder.pay_mode.setText(list.getPayment_mode());
        holder.amount.setText("Rs. " + list.getAmount());
        if (!list.getD_date().isEmpty()) {
            holder.d_date.setText(list.getD_date());
            holder.d_date.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, OrderDescription.class);
                i.putExtra("id", list.getId());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        if (list.getStatus().equals("Pending")) {
            holder.cancel.setVisibility(View.VISIBLE);
        } else {
            holder.cancel.setVisibility(View.GONE);
        }

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Confirm Cancel Order !!");
                alert.setMessage("Do you want to cancel this order?");
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.cancel(position);
                    }
                });
                alert.show();


            }
        });
//        holder.statusbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(context, OrderStatusItem.class);
//                intent.putExtra("id",model.get(position).getId());
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        // return model.size();
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView order_no, p_date, d_date, status, pay_mode, amount, qty, delivery_charge;
        TextView cancel, statusbtn, coupon_code, coupon_amount;
        LinearLayout cancel_layout, date_layout, coupon_layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order_no = itemView.findViewById(R.id.textViewProductName);
            p_date = itemView.findViewById(R.id.pdate);
            d_date = itemView.findViewById(R.id.ddate);
            statusbtn = itemView.findViewById(R.id.button);
            status = itemView.findViewById(R.id.status);
            delivery_charge = itemView.findViewById(R.id.delivery_charge);
            qty = itemView.findViewById(R.id.qty);
            amount = itemView.findViewById(R.id.amount);
            cancel = itemView.findViewById(R.id.cancel);
            pay_mode = itemView.findViewById(R.id.pay_mode);
            cancel_layout = itemView.findViewById(R.id.cancel_layout);
            date_layout = itemView.findViewById(R.id.date_layout);
            coupon_code = itemView.findViewById(R.id.coupon_code);
            coupon_amount = itemView.findViewById(R.id.coupon_amount);
            coupon_layout = itemView.findViewById(R.id.coupon_layout);

        }
    }

    public interface ButtonClick {
        public void cancel(int position);
    }

}
