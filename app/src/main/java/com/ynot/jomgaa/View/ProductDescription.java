package com.ynot.jomgaa.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ynot.jomgaa.FunctionModels.NormalData;
import com.ynot.jomgaa.FunctionModels.SizeSpinner;
import com.ynot.jomgaa.FunctionModels.productDetailModel;
import com.ynot.jomgaa.Model.CurrentSize;
import com.ynot.jomgaa.Model.Products;
import com.ynot.jomgaa.Model.Subcategory;
import com.ynot.jomgaa.Notification.NotificationCountSetClass;
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

public class ProductDescription extends AppCompatActivity {
    Toolbar toolbar;
    TextView name, price, brand;
    Products list;
    ImageView nodata, image, fav;
    Spinner size_spinner;
    List<SizeSpinner> model = new ArrayList<>();
    List<String> size_name = new ArrayList<>();
    String size_id, fav_status;
    Button addtocart;
    ProgressDialog progressDialog;
    int quantity = 0;
    TextView minus, plus, count, details, original, color;
    LinearLayout count_layout;
    String current_size = "";
    List<CurrentSize> currentSizes = new ArrayList<>();
    int pos = -1;
    int cart_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        toolbar = findViewById(R.id.toolbar);

        name = findViewById(R.id.cat);
        original = findViewById(R.id.original);
        color = findViewById(R.id.color);
        addtocart = findViewById(R.id.addtocart);
        price = findViewById(R.id.price);
        brand = findViewById(R.id.brand);
        count_layout = findViewById(R.id.count_layout);
        nodata = findViewById(R.id.nodata);
        image = findViewById(R.id.imageView9);
        minus = findViewById(R.id.minus);
        details = findViewById(R.id.details);
        plus = findViewById(R.id.plus);
        count = findViewById(R.id.count);
        size_spinner = findViewById(R.id.size_spinner);
        fav = findViewById(R.id.fav);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        list = (Products) getIntent().getSerializableExtra("list");
        toolbar.setTitle(list.getTitle());
        name.setText(list.getTitle());
        brand.setText(list.getBrand());
        toolbar.setTitle(list.getTitle());
        setSupportActionBar(toolbar);
        Glide.with(getApplicationContext()).load(list.getImages()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                nodata.setVisibility(View.GONE);
                return false;
            }
        }).into(image);
        price.setText("Rs." + String.format("%.2f", Double.parseDouble(list.getPrice())));
        if (list.getFavStatus().equals("1")) {
            fav.setImageResource(R.drawable.fav);
            fav_status = "1";
        } else {
            fav.setImageResource(R.drawable.favgray);
            fav_status = "0";
        }

        getDescription();

        size_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size_id = model.get(position).getId();
                Log.e("selected_size", size_id);
                count_layout.setVisibility(View.GONE);
                addtocart.setVisibility(View.VISIBLE);
                quantity = 1;

                if (currentSizes.size() > 0) {
                    for (int i = 0; i < currentSizes.size(); i++) {
                        if (size_id.equals(currentSizes.get(i).getId())) {
                            Log.e("same", "inside");
                            count.setText(currentSizes.get(i).getCart_quantity());
                            count_layout.setVisibility(View.VISIBLE);
                            addtocart.setVisibility(View.GONE);
                            quantity = Integer.parseInt(currentSizes.get(i).getCart_quantity());
                            pos = i;
                            return;

                        } /*else {
                            Log.e("else", "inside");
                            count_layout.setVisibility(View.GONE);
                            addtocart.setVisibility(View.VISIBLE);
                            quantity = 1;
                            return;
                        }*/
                    }
                } else {
                    Log.e("size_0", "inside");
                    count_layout.setVisibility(View.GONE);
                    addtocart.setVisibility(View.VISIBLE);
                    quantity = 1;
                    return;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = 1;
                Log.e("quantity", String.valueOf(quantity));
                add_to_cart();
                count_layout.setVisibility(View.VISIBLE);
                addtocart.setVisibility(View.GONE);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                Log.e("quantity", String.valueOf(quantity));
                add_to_cart();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 1) {
                    quantity--;
                    add_to_cart();
                }
            }
        });


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav_status.equals("1")) {
                    fav.setImageResource(R.drawable.favgray);
                    fav_status = "0";
                    add_fav();
                } else {
                    fav.setImageResource(R.drawable.fav);
                    fav_status = "1";
                    add_fav();
                }

            }
        });
    }

    private void add_fav() {
        Log.e("fav_status", fav_status);
        progressDialog.show();
        Call<NormalData> call = RetrofitClient.getInstance().getApi().set_fav(list.getProductId(), SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id(), fav_status);
        call.enqueue(new Callback<NormalData>() {
            @Override
            public void onResponse(Call<NormalData> call, Response<NormalData> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<NormalData> call, Throwable t) {

            }
        });
    }

    private void add_to_cart() {

        progressDialog.show();
        int coun = quantity;
        Log.e("quantity", String.valueOf(quantity));
        Log.e("user_id", SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());
        Log.e("product_id", list.getProductId());
        Log.e("size_id", size_id);
        Call<NormalData> call = RetrofitClient.getInstance().getApi().add_to_cart(SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id(), list.getProductId(), size_id, String.valueOf(coun));
        call.enqueue(new Callback<NormalData>() {
            @Override
            public void onResponse(Call<NormalData> call, Response<NormalData> response) {
                progressDialog.dismiss();
                if (response.body().isStatus()) {
                    Toast.makeText(getApplicationContext(), "Item Quantity Changed !", Toast.LENGTH_SHORT).show();
                    count.setText(String.valueOf(quantity));
                    getDescription();
                    cart_count();

                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<NormalData> call, Throwable t) {

            }
        });
    }

    private void getDescription() {
        progressDialog.show();
        Call<productDetailModel> call = RetrofitClient.getInstance().getApi().getProductDetails(list.getProductId(), SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());
        call.enqueue(new Callback<productDetailModel>() {
            @Override
            public void onResponse(Call<productDetailModel> call, Response<productDetailModel> response) {
                progressDialog.dismiss();
                if (response.body().isStatus()) {
                    model = new ArrayList<>();
                    currentSizes = new ArrayList<>();
                    size_name = new ArrayList<>();
                    model = response.body().getSize_model();

                    for (int i = 0; i < model.size(); i++) {
                        size_name.add(model.get(i).getSize());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, size_name);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    size_spinner.setAdapter(adapter);
                   /* int qq = Integer.parseInt(response.body().getCart_quantity());
                    quantity = Integer.parseInt(response.body().getCart_quantity());
                    count.setText(response.body().getCart_quantity());
                    Log.e("cou", response.body().getCart_quantity());
                    if (qq > 0) {
                        addtocart.setVisibility(View.GONE);
                        count_layout.setVisibility(View.VISIBLE);
                    } else {
                        addtocart.setVisibility(View.VISIBLE);
                        count_layout.setVisibility(View.GONE);
                    }*/

                    if (!response.body().getDetails().isEmpty()) {
                        details.setText(response.body().getDetails());
                        details.setVisibility(View.VISIBLE);
                    }
                    currentSizes = response.body().getCurrentSizes();

                    double org = Double.valueOf(response.body().getOriginal_price());
                    double pr = Double.valueOf(response.body().getPrice());

                    if (org > pr) {
                        original.setVisibility(View.VISIBLE);
                        price.setText("Rs." + String.format("%.2f", Double.parseDouble(response.body().getPrice())));
                        original.setPaintFlags(original.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        original.setText("Rs." + String.format("%.2f", Double.parseDouble(response.body().getOriginal_price())));
                    } else {
                        original.setVisibility(View.GONE);
                        price.setText("Rs." + String.format("%.2f", Double.parseDouble(response.body().getPrice())));
                    }
                    color.setText(response.body().getColor());

                    // current_size = response.body().getCurrent_size();

                }
            }

            @Override
            public void onFailure(Call<productDetailModel> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cart_count();
    }

    public void cart_count() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.CART_COUNT,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("cart_count", response);
                        try {
                            JSONObject ob = new JSONObject(response);
                            if (ob.getBoolean("status")) {
                                cart_count = Integer.parseInt(ob.getString("cart_count"));
                                invalidateOptionsMenu();
                            } else {
                                cart_count = 0;
                                invalidateOptionsMenu();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
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
            case R.id.cart:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.putExtra("cart", "cart");
                startActivity(i);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.cart);
        NotificationCountSetClass.setAddToCart(ProductDescription.this, item, cart_count);
        return super.onCreateOptionsMenu(menu);
    }

}