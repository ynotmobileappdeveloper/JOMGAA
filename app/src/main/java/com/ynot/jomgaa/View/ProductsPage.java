package com.ynot.jomgaa.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ynot.jomgaa.Adapter.AllDataAdapter;
import com.ynot.jomgaa.FunctionModels.NormalData;
import com.ynot.jomgaa.FunctionModels.ProductsModel;
import com.ynot.jomgaa.Model.Products;
import com.ynot.jomgaa.Model.Subcategory;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.Api;
import com.ynot.jomgaa.Web.CommonFunction;
import com.ynot.jomgaa.Web.Constants;
import com.ynot.jomgaa.Web.RetrofitClient;
import com.ynot.jomgaa.Web.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductsPage extends AppCompatActivity {
    Toolbar toolbar;
    List<Products> model = new ArrayList<>();
    Subcategory list;
    AllDataAdapter allDataAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    CommonFunction commonFunction;
    ImageView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_page);

        commonFunction = new CommonFunction(getApplicationContext());


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");


        recyclerView = findViewById(R.id.rec);
        nodata = findViewById(R.id.nodata);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        list = (Subcategory) getIntent().getSerializableExtra("list");
        Log.e("list", list.getSubcat_id());
        toolbar = findViewById(R.id.toolbar9);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void GetProducts() {
        Retrofit retrofit = commonFunction.createRetrofitObjectWithHeader(Constants.API_BASE_URL);
        Api Apiinterface = retrofit.create(Api.class);
        Apiinterface.getProducts(list.getSubcat_id(), SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id()).enqueue(new Callback<ProductsModel>() {
            @Override
            public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {

                if (response.body().isStatus()) {
                    nodata.setVisibility(View.GONE);
                    model = new ArrayList<>();
                    model = response.body().getModel();

                    allDataAdapter = new AllDataAdapter(getApplicationContext(), model, new AllDataAdapter.ItemClick() {
                        @Override
                        public void Click(Products list) {
                            Intent i = new Intent(getApplicationContext(), ProductDescription.class);
                            i.putExtra("list", list);
                            startActivity(i);
                        }

                        @Override
                        public void FavClick(Products list, int position) {
                            if (list.getFavStatus().equals("1")) {
                                list.setFavStatus("0");
                                add_fav(list.getProductId(), "0");
                            } else {
                                list.setFavStatus("1");
                                add_fav(list.getProductId(), "1");
                            }


                        }
                    },"");
                    recyclerView.setAdapter(allDataAdapter);
                } else {
                    nodata.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<ProductsModel> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetProducts();
    }

    private void add_fav(String id, String status) {
        progressDialog.show();
        Call<NormalData> call = RetrofitClient.getInstance().getApi().set_fav(id, SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id(), status);
        call.enqueue(new Callback<NormalData>() {
            @Override
            public void onResponse(Call<NormalData> call, Response<NormalData> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        allDataAdapter.notifyDataSetChanged();
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


}