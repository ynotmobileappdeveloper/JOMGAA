package com.ynot.jomgaa.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Searchfrag extends Fragment {
    TextView skip;
    MainActivity mainActivity;
    RecyclerView rec;
    List<Products> model = new ArrayList<>();
    AllDataAdapter allDataAdapter;
    EditText search_edit;
    ImageView search, nodata;
    CommonFunction commonFunction;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        commonFunction = new CommonFunction(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");

        final AppCompatActivity activity = (AppCompatActivity) root.getContext();
        activity.getSupportActionBar().hide();
        search_edit = root.findViewById(R.id.search_edit);
        search = root.findViewById(R.id.search);
        nodata = root.findViewById(R.id.nodata);
        rec = root.findViewById(R.id.rec);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        rec.setLayoutManager(linearLayoutManager);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_edit.getText().toString().isEmpty()) {
                    search_edit.requestFocus();
                    Toast.makeText(getContext(), "Please provide search hint !!", Toast.LENGTH_SHORT).show();
                    return;

                }
                search_Item();
            }
        });


        return root;
    }

    private void search_Item() {
        progressDialog.show();
        Retrofit retrofit = commonFunction.createRetrofitObjectWithHeader(Constants.API_BASE_URL);
        Api api = retrofit.create(Api.class);
        api.getSearchData(search_edit.getText().toString(), SharedPrefManager.getInstatnce(getContext()).getUser().getUser_id()).enqueue(new Callback<ProductsModel>() {
            @Override
            public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.body().isStatus()) {
                        nodata.setVisibility(View.GONE);
                        model = new ArrayList<>();
                        model = response.body().getModel();

                        allDataAdapter = new AllDataAdapter(getContext(), model, new AllDataAdapter.ItemClick() {
                            @Override
                            public void Click(Products list) {
                                Intent i = new Intent(getContext(), ProductDescription.class);
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
                        rec.setAdapter(allDataAdapter);
                    } else {
                        nodata.setVisibility(View.VISIBLE);
                        rec.setAdapter(null);
                    }
                }


            }

            @Override
            public void onFailure(Call<ProductsModel> call, Throwable t) {

            }
        });


    }

    private void add_fav(String id, String status) {
        progressDialog.show();
        Call<NormalData> call = RetrofitClient.getInstance().getApi().set_fav(id, SharedPrefManager.getInstatnce(getContext()).getUser().getUser_id(), status);
        call.enqueue(new Callback<NormalData>() {
            @Override
            public void onResponse(Call<NormalData> call, Response<NormalData> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        allDataAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<NormalData> call, Throwable t) {

            }
        });
    }
}