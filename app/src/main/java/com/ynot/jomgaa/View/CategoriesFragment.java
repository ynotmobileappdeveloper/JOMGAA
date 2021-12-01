package com.ynot.jomgaa.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ynot.jomgaa.Adapter.CategoryAdapter;
import com.ynot.jomgaa.FunctionModels.CategoryModel;
import com.ynot.jomgaa.Model.AllproductsModel;
import com.ynot.jomgaa.Model.Category;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoriesFragment extends Fragment {
    MainActivity mainActivity;
    RecyclerView recyclerView;
    Toolbar toolbar;
    List<AllproductsModel> allproductsModels = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    List<Category> cat_model = new ArrayList<>();
    ProgressDialog progressDialog;

    public CategoriesFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        final AppCompatActivity activity = (AppCompatActivity) root.getContext();
        activity.getSupportActionBar().hide();
        toolbar = root.findViewById(R.id.toolbar6);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        // skip=root.findViewById(R.id.skip);
        recyclerView = root.findViewById(R.id.catrec);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        getCategory();

        return root;
    }

    private void getCategory() {
        progressDialog.show();
        Call<CategoryModel> call = RetrofitClient.getInstance().getApi().GetCategory();
        call.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                progressDialog.dismiss();
                if (response.body().getStatus()) {
                    cat_model = response.body().getCategory();
                    categoryAdapter = new CategoryAdapter(getContext(), cat_model, new CategoryAdapter.ItemClick() {
                        @Override
                        public void Click(Category list) {
                            Intent i = new Intent(getContext(), Categoryitem.class);
                            i.putExtra("cat_id", list.getCatId());
                            i.putExtra("name", list.getTitle());
                            startActivity(i);
                        }
                    });
                    recyclerView.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });
    }
}