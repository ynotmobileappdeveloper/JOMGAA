package com.ynot.jomgaa.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ynot.jomgaa.Adapter.MyFavAdapter;
import com.ynot.jomgaa.FunctionModels.FavModel;
import com.ynot.jomgaa.FunctionModels.NormalData;
import com.ynot.jomgaa.Model.Favourite;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.RetrofitClient;
import com.ynot.jomgaa.Web.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopsFragment extends Fragment {
    MainActivity mainActivity;

    public ShopsFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    Toolbar toolbar;
    RecyclerView recyclerView;
    MyFavAdapter allDataAdapter;
    List<Favourite> model = new ArrayList<>();
    ProgressDialog progressDialog;
    ImageView nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_shops, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait..");
        final AppCompatActivity activity = (AppCompatActivity) root.getContext();
        activity.getSupportActionBar().hide();
        toolbar = root.findViewById(R.id.toolbar7);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        recyclerView = root.findViewById(R.id.myfavrec);
        nodata = root.findViewById(R.id.nodata);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        getfav();
        return root;

    }

    private void getfav() {
        progressDialog.show();

        Call<FavModel> call = RetrofitClient.getInstance().getApi().GetFav(SharedPrefManager.getInstatnce(getContext()).getUser().getUser_id());
        call.enqueue(new Callback<FavModel>() {
            @Override
            public void onResponse(Call<FavModel> call, Response<FavModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getstatus()) {
                        Log.e("in", "in");
                        nodata.setVisibility(View.GONE);
                        model = new ArrayList<>();
                        model = response.body().getFavourite();
                        if (model.size() > 0) {
                            nodata.setVisibility(View.GONE);
                        } else {
                            nodata.setVisibility(View.VISIBLE);
                        }
                        allDataAdapter = new MyFavAdapter(getActivity(), model, new MyFavAdapter.ItemClick() {
                            @Override
                            public void FavClick(Favourite list, int position) {
                                add_fav(list.getId(), "0");
                            }

                            @Override
                            public void Click(Favourite list) {
                               /* Intent i = new Intent(getContext(), ProductDescription.class);
                                i.putExtra("fav_list", list);
                                startActivity(i);*/
                            }
                        });
                        recyclerView.setAdapter(allDataAdapter);
                    } else {
                        Log.e("in", "else");
                        model = new ArrayList<>();
                        recyclerView.setAdapter(null);
                        nodata.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<FavModel> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void add_fav(String id, String status) {
        progressDialog.show();
        Log.e("id", id);
        Call<NormalData> call = RetrofitClient.getInstance().getApi().set_fav(id, SharedPrefManager.getInstatnce(getContext()).getUser().getUser_id(), "0");
        call.enqueue(new Callback<NormalData>() {
            @Override
            public void onResponse(Call<NormalData> call, Response<NormalData> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getfav();

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