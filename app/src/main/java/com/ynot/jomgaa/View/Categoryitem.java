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
import android.widget.TextView;

import com.ynot.jomgaa.Adapter.AllDataAdapter;
import com.ynot.jomgaa.Adapter.SubCatAdapter;
import com.ynot.jomgaa.Adapter.SubCategoryAdapter;
import com.ynot.jomgaa.FunctionModels.SubcategoryModel;
import com.ynot.jomgaa.Model.Subcategory;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Categoryitem extends AppCompatActivity {
    RecyclerView recyclerView;
    Toolbar toolbar;
    List<Subcategory> sub_model = new ArrayList<>();
    SubCatAdapter allDataAdapter;
    ProgressDialog progressDialog;
    String cat_id;
    TextView name;
    ImageView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoryitem);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        toolbar = findViewById(R.id.toolbar6);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        name = findViewById(R.id.textView19);
        nodata = findViewById(R.id.nodata);
        name.setText(getIntent().getStringExtra("name"));
        cat_id = getIntent().getStringExtra("cat_id");
        recyclerView = findViewById(R.id.catrec);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        get_sub_category();

    }

    private void get_sub_category() {
        progressDialog.show();
        sub_model = new ArrayList<>();
        Call<SubcategoryModel> call = RetrofitClient.getInstance().getApi().GetSubcategory(cat_id);
        call.enqueue(new Callback<SubcategoryModel>() {
            @Override
            public void onResponse(Call<SubcategoryModel> call, Response<SubcategoryModel> response) {
                progressDialog.dismiss();
                if (response.body().getStatus()) {
                    sub_model = response.body().getSubcategory();
                    if (sub_model.size() > 0) {
                        nodata.setVisibility(View.GONE);
                    } else {
                        nodata.setVisibility(View.VISIBLE);
                    }

                    allDataAdapter = new SubCatAdapter(getApplicationContext(), sub_model, new SubCatAdapter.ItemClick() {
                        @Override
                        public void Click(Subcategory list) {
                            Intent i = new Intent(getApplicationContext(), ProductsPage.class);
                            i.putExtra("list", list);
                            startActivity(i);
                        }


                    });
                    recyclerView.setAdapter(allDataAdapter);
                } else {
                    nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SubcategoryModel> call, Throwable t) {

            }
        });
    }
}