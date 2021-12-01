package com.ynot.jomgaa.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.SharedPrefManager;
import com.ynot.jomgaa.Web.URLs;
import com.ynot.jomgaa.Web.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Checkout extends AppCompatActivity {
    Toolbar toolbar;
    Button chkout, code;
    ProgressDialog progressDialog;
    TextView items, subtotal, deliverycharge, discount, total, coupon_amount;
    EditText coupon_code;
    ImageView remove;
    LinearLayout coupon_layout;
    String cp_amount = "", d_charge = "", grand_total = "", cp_code = "";
    boolean apply = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        progressDialog = new ProgressDialog(Checkout.this);
        progressDialog.setMessage("Please Wait..");
        code = findViewById(R.id.code);
        remove = findViewById(R.id.remove);
        coupon_code = findViewById(R.id.coupon_code);
        coupon_layout = findViewById(R.id.coupon_layout);
        coupon_amount = findViewById(R.id.coupon_amount);

        toolbar = findViewById(R.id.toolbar2);

        chkout = findViewById(R.id.checkout);
        items = findViewById(R.id.itemss);
        subtotal = findViewById(R.id.subtotal);
        deliverycharge = findViewById(R.id.delivery);
        discount = findViewById(R.id.disc);
        total = findViewById(R.id.total);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        chkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ConfirmAddress.class);
                i.putExtra("cp_amount", cp_amount);
                i.putExtra("d_charge", d_charge);
                i.putExtra("grand_total", grand_total);
                i.putExtra("cp_code", cp_code);
                startActivity(i);


            }
        });
        checkout_details();
        coupon_code.setEnabled(true);
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coupon_code.getText().toString().isEmpty()) {
                    coupon_code.setError("Please Enter Coupon Code !!");
                    coupon_code.requestFocus();
                    return;
                }
                cp_code = coupon_code.getText().toString();
                ConfirmCouponCode();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveCoupon();
            }
        });

    }

    private void RemoveCoupon() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.REMOVE_COUPON_CODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("remove", response);
                try {
                    JSONObject ob = new JSONObject(response);

                    if (ob.getBoolean("status")) {
                        apply = false;
                        Toast.makeText(getApplicationContext(), ob.getString("message"), Toast.LENGTH_SHORT).show();
                        remove.setVisibility(View.GONE);
                        code.setVisibility(View.VISIBLE);
                        coupon_code.setEnabled(true);
                        coupon_code.getText().clear();
                        coupon_layout.setVisibility(View.GONE);
                        total.setText("Rs. " + String.format("%.2f", Double.parseDouble(ob.getString("grand_total"))));
                        deliverycharge.setText("Rs. " + String.format("%.2f", Double.parseDouble(ob.getString("delivery_charge"))));
                        cp_amount = "";
                        cp_code = "";
                        d_charge = ob.getString("delivery_charge");
                        grand_total = ob.getString("grand_total");
                    } else {
                        Toast.makeText(getApplicationContext(), ob.getString("message"), Toast.LENGTH_SHORT).show();
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
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());
                params.put("coupon_amount", cp_amount);
                params.put("delivery_charge", d_charge);
                params.put("grand_total", grand_total);

                Log.e("remove_params", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(90 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void ConfirmCouponCode() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.SET_COUPON_CODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject ob = new JSONObject(response);
                    Log.e("resp", response.toString());
                    if (ob.getBoolean("status")) {
                        apply = true;
                        remove.setVisibility(View.VISIBLE);
                        coupon_code.setEnabled(false);
                        if (ob.has("coupon_amount")) {
                            code.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), ob.getString("message"), Toast.LENGTH_SHORT).show();
                            coupon_layout.setVisibility(View.VISIBLE);
                            coupon_amount.setText("Rs. " + String.format("%.2f", Double.parseDouble(ob.getString("coupon_amount"))));
                            cp_amount = ob.getString("coupon_amount");
                            grand_total = ob.getString("grand_total");
                            d_charge = ob.getString("delivery_charge");
                            total.setText("Rs. " + String.format("%.2f", Double.parseDouble(ob.getString("grand_total"))));
                            deliverycharge.setText("Rs. " + String.format("%.2f", Double.parseDouble(ob.getString("delivery_charge"))));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), ob.getString("message"), Toast.LENGTH_SHORT).show();
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
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());
                params.put("code", coupon_code.getText().toString());
                Log.e("input", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(90 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void checkout_details() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.CHECK_OUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("checkout", response);
                        try {
                            JSONObject ob = new JSONObject(response);

                            if (ob.getBoolean("status")) {
                                // discount.setText("Rs." + ob.getString("discounts"));
                                // deliverycharge.setText("Rs. " + ob.getString("delivery_charge") + ".00");
                                deliverycharge.setText("Rs. " + String.format("%.2f", Double.parseDouble(ob.getString("delivery_charge"))));
                                discount.setText("Rs. " + String.format("%.2f", Double.parseDouble(ob.getString("discounts"))));
                                total.setText("Rs. " + String.format("%.2f", Double.parseDouble(ob.getString("estimated_total"))));
                                subtotal.setText("Rs. " + String.format("%.2f", Double.parseDouble(ob.getString("total"))));
                                items.setText("(" + ob.getString("total_items") + ") items");

                                d_charge = ob.getString("delivery_charge");
                                grand_total = ob.getString("estimated_total");

//                                if (ob.getString("pincode") != null) {
//                                    add_pincode.setText(ob.getString("pincode"));
//                                }
//                                if (ob.getString("landmark") != null) {
//                                    add_landmark.setText(ob.getString("landmark"));
//                                }
//
//                                if (ob.getString("address") != null) {
//                                    add_address.setText(ob.getString("address"));
//                                }
//                                if (ob.getString("address_id") != null) {
//                                    add_id = ob.getString("address_id");
//                                }
//                                if (ob.has("place") && ob.getString("place") != null) {
//                                    location.setText(ob.getString("place"));
//                                }

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(90 * 1000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }

}