package com.ynot.jomgaa.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ynot.jomgaa.Adapter.CartAdapter2;
import com.ynot.jomgaa.FunctionModels.CartItems;
import com.ynot.jomgaa.Model.CartData;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.RetrofitClient;
import com.ynot.jomgaa.Web.SharedPrefManager;
import com.ynot.jomgaa.Web.URLs;
import com.ynot.jomgaa.Web.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {
    CardView bootom_layout;
    Animation slide_up;
    RecyclerView cart_rec;
    List<CartData> model;
    CartAdapter2 adapter;
    Activity activity;
    RecyclerView.LayoutManager layoutManager;
    Button checkout;
    TextView total, dis, items, minimum, discounts, subtotal;
    int price_tot, counter;
    ProgressDialog progressDialog;
    MainActivity mainActivity;
    ImageView nodata;

    public CartFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        bootom_layout = root.findViewById(R.id.bottom_layout);
        slide_up = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        checkout = root.findViewById(R.id.checkout);
        total = root.findViewById(R.id.total);
        nodata = root.findViewById(R.id.nodata);

        cart_rec = root.findViewById(R.id.cart_rec);
        items = root.findViewById(R.id.items);
        discounts = root.findViewById(R.id.disc);
        minimum = root.findViewById(R.id.minimum);
        cart_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        counter = 0;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        final AppCompatActivity activity = (AppCompatActivity) root.getContext();
        activity.getSupportActionBar().show();

        return root;
    }

    private void get_cart_items() {
        //demo
        // checkout.setBackgroundResource(R.drawable.bggreenorderstatus2);

        Call<CartItems> call = RetrofitClient.getInstance().getApi().GetCartItems(SharedPrefManager.getInstatnce(getContext()).getUser().getUser_id());
        call.enqueue(new Callback<CartItems>() {
            @Override
            public void onResponse(Call<CartItems> call, Response<CartItems> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus()) {
                        bootom_layout.startAnimation(slide_up);
                        checkout.setEnabled(true);
                        bootom_layout.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                        model = new ArrayList<>();
                        model = response.body().getCartItem();
                        adapter = new CartAdapter2(getContext(), model, new CartAdapter2.Click() {
                            @Override
                            public void Delete(CartData list, int position) {
                                delete_item(list.getCartId(), position);
                            }

                            @Override
                            public void Plus(CartData list) {

                                int current_count = Integer.parseInt(list.getQuantity());
                                //   int price_total = Integer.parseInt(model.get(position).getPrice());
                                int newcount = current_count + 1;
                                int stock = Integer.parseInt(list.getStock());
                                if (newcount < stock) {
                                    countupdate(list.getCartId(), String.valueOf(newcount));
                                } else {
                                    Toast.makeText(getContext(), "Out of Stock !!", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void Minus(CartData list) {
                                int current_count = Integer.parseInt(list.getQuantity());
                                if (current_count > 1) {
                                    int newcount = current_count - 1;
                                    countupdate(list.getCartId(), String.valueOf(newcount));
                                }

                            }
                        });
                        cart_rec.setAdapter(adapter);
                    } else {
                        checkout.setEnabled(true);
                        bootom_layout.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<CartItems> call, Throwable t) {

            }
        });


    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), Checkout.class));

            }
        });


    }

    private void countupdate(final String id, final String count) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.UPDATE_COUNT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("update_count", response);

                        try {
                            JSONObject ob = new JSONObject(response);

                            if (ob.getBoolean("status")) {
                                adapter.notifyDataSetChanged();
                                Toast.makeText(activity, "Item Quantity Changed", Toast.LENGTH_SHORT).show();
                                get_cart_items();

                            } else {
                                adapter.notifyDataSetChanged();
                                Toast.makeText(activity, "Sorry Insufficient Stock", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("item_id", id);
                params.put("new_count", count);
                params.put("user_id", SharedPrefManager.getInstatnce(getContext()).getUser().getUser_id());

                Log.e("update_count_params", String.valueOf(params));
                return params;
            }
        };
        VolleySingleton.getmInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private void delete_item(final String id, final int position) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.REMOVE_CART,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("remove", response);
                        try {
                            JSONObject ob = new JSONObject(response);
                            if (ob.getBoolean("status")) {
                                model.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyItemRangeRemoved(position, model.size());
                                if (model.size() == 0) {
                                    get_cart_items();
                                }

                                ((MainActivity) getActivity()).cart_count();
                            } else {
                                Toast.makeText(activity, "Something Went Wrong !!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cart_id", id);
                params.put("user_id", SharedPrefManager.getInstatnce(getContext()).getUser().getUser_id());
                Log.e("params", String.valueOf(params));
                return params;
            }
        };
        VolleySingleton.getmInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        get_cart_items();
    }
}
