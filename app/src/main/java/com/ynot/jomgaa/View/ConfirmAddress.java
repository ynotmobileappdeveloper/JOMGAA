package com.ynot.jomgaa.View;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmAddress extends AppCompatActivity {
    Button checkout, addnewAddress;
    Button cod, gpay;
    String pmode = "1";
    Button cancel, order;
    TextView total, discount, delivery, est_total, count;
    String lat, lon, place;

    String pickup_status = "1";
    Button change_address;
    TextView add_address, add_pincode, add_landmark, location, discounts;
    String add_id;
    ProgressDialog progress;
    TextView address;
    String data_name, data_mob;
    float cnt;
    double pay_price;
    String payment_mode = "COD";
    TextView slots;
    List<String> slot_model = new ArrayList<>();
    String current_time;
    Dialog dialog;
    RecyclerView time_rec;
    //    TimeAdapter adapter;
    String slot_id = "", slot_date = "";
    LinearLayout cod_layout, gpay_layout;
    String cp_amount = "", d_charge = "", grand_total = "", cp_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        progress = new ProgressDialog(ConfirmAddress.this);
        progress.setMessage("Please Wait..");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        address = findViewById(R.id.name);

        cod_layout = findViewById(R.id.cod_layout);
        gpay_layout = findViewById(R.id.gpay_layout);
        cod = findViewById(R.id.cod);
        gpay = findViewById(R.id.gpay);

        if (getIntent().hasExtra("cp_amount")) {
            cp_amount = getIntent().getStringExtra("cp_amount");
            cp_code = getIntent().getStringExtra("cp_code");
            d_charge = getIntent().getStringExtra("d_charge");
            grand_total = getIntent().getStringExtra("grand_total");
        }


        checkout_details();
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pmode = "1";
                cod.setBackgroundResource(R.drawable.btnbg);
                gpay.setBackgroundResource(R.drawable.btnbgstrock2);

            }
        });

        cod_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pmode = "1";
                cod.setBackgroundResource(R.drawable.btnbg);
                gpay.setBackgroundResource(R.drawable.btnbgstrock2);
            }
        });
        gpay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pmode = "2";
                gpay.setBackgroundResource(R.drawable.btnbg);
                cod.setBackgroundResource(R.drawable.btnbgstrock2);
            }
        });
        gpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pmode = "2";
                gpay.setBackgroundResource(R.drawable.btnbg);
                cod.setBackgroundResource(R.drawable.btnbgstrock2);

            }
        });
        checkout = findViewById(R.id.chkout);
        addnewAddress = findViewById(R.id.addnewaddress);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add_id.isEmpty()) {
                    Toast.makeText(ConfirmAddress.this, "You must choose a delivery Address !!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    order_item();
                }

            }
        });
        addnewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddAddress.class);
                i.putExtra("checkout", "checkout");
                i.putExtra("cp_amount", cp_amount);
                i.putExtra("d_charge", d_charge);
                i.putExtra("grand_total", grand_total);
                i.putExtra("cp_code", cp_code);
                startActivity(i);
            }
        });


    }


    private void order_item() {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.CHECKOUT_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  order.setEnabled(true);
                        progress.dismiss();
                        Log.e("chekout", response);

                        try {
                            JSONObject ob = new JSONObject(response);

                            if (ob.getBoolean("status")) {

                                Intent i = new Intent(getApplicationContext(), OrderPageSuccess.class);
                                i.putExtra("order", ob.getString("order_id"));
                                i.putExtra("amount", ob.getString("estimated_total"));
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(ConfirmAddress.this, "Something Went Wrong !!", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  order.setEnabled(true);
                progress.dismiss();
                Log.e("error", new String(error.networkResponse.data));

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("address_id", add_id);
                params.put("payment_id", "0");
                params.put("coupon_code", cp_code);
                params.put("payment_mode", pmode);
                params.put("coupon_amount", cp_amount);
                params.put("delivery_charge", d_charge);
                params.put("grand_total", grand_total);
                params.put("user_id", SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());
                Log.e("params", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(90 * 4000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getmInstance(this).addToRequestQueue(stringRequest);

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

    private void checkout_details() {
//        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.CHECK_OUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //   progress.dismiss();
                        Log.e("checkout", response);
                        try {
                            JSONObject ob = new JSONObject(response);

                            if (ob.getBoolean("status")) {
//                                total.setText("Rs." + Float.valueOf(ob.getString("total")) + "0");
//                                // discount.setText("Rs." + ob.getString("discounts"));
//                                delivery.setText("Rs." + ob.getString("delivery_charge") + ".00");
//                                est_total.setText("Rs." + ob.getString("estimated_total"));
//                                count.setText("Subtotal ( " + ob.getString("total_items") + " items)");
//                                discounts.setText("Rs." + ob.getString("discounts") + ".00");

                                pay_price = Double.parseDouble(ob.getString("estimated_total"));

                                cnt = Float.valueOf(ob.getString("discounts"));
                                if (ob.getString("address") != null) {
                                    address.setText(address.getText().toString() + ob.getString("address"));
                                }
                                if (ob.getString("landmark") != null) {
                                    address.setText(address.getText().toString() + "\n" + ob.getString("landmark"));
                                }

                                if (ob.has("place") && ob.getString("place") != null) {
                                    address.setText(address.getText().toString() + "\n" + ob.getString("place"));
                                }
                                if (ob.getString("pincode") != null) {
                                    address.setText(address.getText().toString() + "\n" + ob.getString("pincode"));
                                }

                                if (ob.getString("address_id") != null) {
                                    add_id = ob.getString("address_id");
                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   progress.dismiss();

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