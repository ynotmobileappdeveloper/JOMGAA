package com.ynot.jomgaa.View;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ynot.jomgaa.Adapter.OrderDetail;
import com.ynot.jomgaa.Model.OrderDetails;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.SharedPrefManager;
import com.ynot.jomgaa.Web.URLs;
import com.ynot.jomgaa.Web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDescription extends AppCompatActivity {
    ArrayList<OrderDetails> model;
    OrderDetail adpater;
    RecyclerView order_rec;
    TextView total;


    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_description);
        order_rec = findViewById(R.id.order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        total = findViewById(R.id.tv_bill_amount);
        order_rec.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        order_rec.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        if (getIntent().hasExtra("id")) {
            id = getIntent().getStringExtra("id");
        }

        get_details();


    }

    private void get_details() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.GET_ORDER_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        model = new ArrayList<>();
                        Log.e("orders_desc", response);
                        try {
                            JSONObject o = new JSONObject(response);
                            if (o.getBoolean("status")) {

                                total.setText("Total Amount : Rs. " + o.getString("total_amount"));

                                JSONArray array = o.getJSONArray("order_items");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject ob = array.getJSONObject(i);
                                    if (Integer.parseInt(ob.getString("qty")) > 0) {
                                        model.add(new OrderDetails("1", ob.getString("item_name"), ob.getString("qty"), ob.getString("price"), ob.getString("img")));
                                    }

                                }
                                adpater = new OrderDetail(getApplicationContext(), model);
                                order_rec.setAdapter(adpater);

                            } else {
                                Toast.makeText(getApplicationContext(), "No Orders !!", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
