package com.ynot.jomgaa.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ynot.jomgaa.Adapter.OrdersAdapter;
import com.ynot.jomgaa.FunctionModels.OrderItems;
import com.ynot.jomgaa.Model.OrderData;
import com.ynot.jomgaa.Model.OrderModel;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.RetrofitClient;
import com.ynot.jomgaa.Web.SharedPrefManager;
import com.ynot.jomgaa.Web.URLs;
import com.ynot.jomgaa.Web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MyOrderDetails extends AppCompatActivity {
    RecyclerView orders_rec;
    OrdersAdapter adpater;
    Toolbar toolbar;
    ArrayList<OrderModel> model;
    ProgressDialog progressDialog;
    ImageView nodata;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        orders_rec = findViewById(R.id.orders);
        nodata = findViewById(R.id.nodata);
        toolbar = findViewById(R.id.toolbar8);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        orders_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        get_orders();


    }

   /* private void get_orders() {
        model = new ArrayList<>();
        Call<OrderItems> call = RetrofitClient.getInstance().getApi().GetOrderItems(SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());
        call.enqueue(new Callback<OrderItems>() {
            @Override
            public void onResponse(Call<OrderItems> call, Response<OrderItems> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        model = response.body().getOrderData();
                        adpater = new OrdersAdapter(getApplicationContext(), model);
                        orders_rec.setAdapter(adpater);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderItems> call, Throwable t) {

            }
        });

    }*/

    private void get_orders() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.GET_ORDERS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        model = new ArrayList<>();
                        Log.e("orders", response);
                        try {
                            JSONObject o = new JSONObject(response);
                            if (o.getBoolean("status")) {
                                nodata.setVisibility(View.GONE);
                                JSONArray array = o.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject ob = array.getJSONObject(i);

                                    OrderModel order = new OrderModel();
                                    order.setId(ob.getString("order_id"));
                                    order.setOrder_no(ob.getString("order_no"));
                                    order.setAmount(ob.getString("total_amount"));
                                    order.setQty(ob.getString("no_items"));
                                    order.setP_date(ob.getString("purchase_date"));
                                    order.setD_date(ob.getString("delivery_date"));
                                    order.setStatus(ob.getString("order_status"));
                                    order.setDelivery_charge(ob.getString("delivery_charge"));
                                    order.setCoupon_amount(ob.getString("coupon_amount"));
                                    order.setCoupon_code(ob.getString("coupon_code"));
                                    // order.setPayment_mode(ob.getString("payment_mode"));
                                    model.add(order);

                                }
                                adpater = new OrdersAdapter(MyOrderDetails.this, model, new OrdersAdapter.ButtonClick() {
                                    @Override
                                    public void cancel(int position) {
                                        cancel_order(model.get(position).getId(), position);
                                    }
                                });
                                orders_rec.setAdapter(adpater);

                            } else {
                                nodata.setVisibility(View.VISIBLE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());


                return params;
            }
        };
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancel_order(final String id, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.CANCEL_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject ob = new JSONObject(response);
                            if (ob.getBoolean("status")) {
                                /*model.remove(position);
                                adpater.notifyItemRemoved(position);
                                adpater.notifyItemRangeChanged(position,model.size());*/
                                get_orders();
                                Toast.makeText(MyOrderDetails.this, "Order Successfully Cancelled !!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MyOrderDetails.this, "Something Went Wrong !! Try again", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());
                params.put("order_id", id);

                return params;
            }
        };
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }
//    private void cancel_order(final String id, final int position) {
//
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.CANCEL_ORDER,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject ob=new JSONObject(response);
//                            if (ob.getBoolean("status"))
//                            {
//                                /*model.remove(position);
//                                adpater.notifyItemRemoved(position);
//                                adpater.notifyItemRangeChanged(position,model.size());*/
//                                get_orders();
//                                Toast.makeText(MyOrderDetails.this, "Order Successfully Cancelled !!", Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                            {
//                                Toast.makeText(MyOrderDetails.this, "Something Went Wrong !! Try again", Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String>params=new HashMap<>();
//                params.put("user_id",SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getId());
//                params.put("order_id",id);
//
//                return params;
//            }
//        };
//        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
//
//
//
//    }
}
