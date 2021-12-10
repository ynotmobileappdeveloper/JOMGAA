package com.ynot.jomgaa.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.viewpagerindicator.CirclePageIndicator;
import com.ynot.jomgaa.Adapter.AllDataAdapter;
import com.ynot.jomgaa.Adapter.FeatureAdapter;
import com.ynot.jomgaa.Adapter.SubCategoryAdapter;
import com.ynot.jomgaa.Adapter.ViewPager_Adapter;
import com.ynot.jomgaa.FunctionModels.CategoryModel;
import com.ynot.jomgaa.FunctionModels.FeatureModel;
import com.ynot.jomgaa.FunctionModels.HomeSlider;
import com.ynot.jomgaa.FunctionModels.NormalData;
import com.ynot.jomgaa.FunctionModels.ProductsModel;
import com.ynot.jomgaa.FunctionModels.SubcategoryModel;
import com.ynot.jomgaa.Model.AllproductsModel;
import com.ynot.jomgaa.Model.Category;
import com.ynot.jomgaa.Model.FeatureList;
import com.ynot.jomgaa.Model.Images;
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
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DashboardFragment extends Fragment {
    MainActivity mainActivity;

    public DashboardFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public DashboardFragment() {

    }

    CirclePageIndicator wormDotsIndicator;
    int currentPage = 0;
    int NUM_PAGES;
    Timer timer;
    final long DELAY_MS = 1000;
    final long PERIOD_MS = 6000;
    ViewPager_Adapter viewPager_adpater;
    ViewPager viewPager;
    TabLayout tabLayout;
    RecyclerView recyclerView, todays_rec;
    SubCategoryAdapter allDataAdapter;
    List<AllproductsModel> allproductsModels = new ArrayList<>();
    Button seeall, today_see;
    List<Category> cat_model = new ArrayList<>();
    List<Subcategory> sub_model = new ArrayList<>();
    String cat_id, cat_name;
    ProgressDialog progressDialog;
    List<Images> imageModel = new ArrayList<>();
    CommonFunction commonFunction;
    CirclePageIndicator indicator;
    ImageView banner;

    List<Products> model = new ArrayList<>();
    AllDataAdapter adapter;
    TextView deals;
    RecyclerView feature_rec;
    List<FeatureList> featuremodel = new ArrayList<>();
    FeatureAdapter featureAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        commonFunction = new CommonFunction(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        viewPager = root.findViewById(R.id.viewpager);
        banner = root.findViewById(R.id.imageView6);
        today_see = root.findViewById(R.id.today_see);
        // wormDotsIndicator = root.findViewById(R.id.worm_dots_indicator);
        seeall = root.findViewById(R.id.seeall);
        final AppCompatActivity activity = (AppCompatActivity) root.getContext();
        activity.getSupportActionBar().show();
        tabLayout = root.findViewById(R.id.tabLayout);
        recyclerView = root.findViewById(R.id.recyclerView);
        todays_rec = root.findViewById(R.id.todays_rec);
        deals = root.findViewById(R.id.deals);
        feature_rec = root.findViewById(R.id.feature_rec);
        feature_rec.setLayoutManager(new LinearLayoutManager(getContext()));
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);
        todays_rec.setLayoutManager(new GridLayoutManager(getContext(), 2));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        //get_category_products(SharedPrefManager.getInstatnce(getContext()).getUser().getId(), "");
                        cat_id = "";
                        cat_name = tab.getText().toString();
                        get_sub_category();
                        break;
                    default:
                        //get_category_products(SharedPrefManager.getInstatnce(getContext()).getUser().getId(), model.get(tab.getPosition() - 1).getCat_id());
                        cat_id = cat_model.get(tab.getPosition() - 1).getCatId();
                        cat_name = tab.getText().toString();
                        get_sub_category();

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        seeall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Categoryitem.class);
                intent.putExtra("cat_id", cat_id);
                intent.putExtra("name", cat_name);
                getContext().startActivity(intent);
            }
        });
        today_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TodaysDeals.class));
            }
        });

//        viewPager_adpater = new ViewPager_Adapter(activity);
//        viewPager.setAdapter(viewPager_adpater);
//        wormDotsIndicator.setViewPager(viewPager);
//
//        final Handler handler = new Handler();
//        final Runnable update = new Runnable() {
//            @Override
//            public void run() {
//                if (currentPage == NUM_PAGES) {
//                    currentPage = 0;
//                }
//                viewPager.setCurrentItem(currentPage++, true);
//
//            }
//        };
//        timer = new Timer();
//        timer.schedule(new java.util.TimerTask() {
//            @Override
//            public void run() {
//                handler.post(update);
//            }
//        }, DELAY_MS, PERIOD_MS);
//        return root;
        indicator = (CirclePageIndicator) root.findViewById(R.id.indicator);
        getHomeSliders();
        TodaysDeal();


        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);

            }
        };
        timer = new Timer();
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);

        GetAppFeature();
        return root;


    }

    private void GetAppFeature() {
        Retrofit retrofit = commonFunction.createRetrofitObjectWithHeader(Constants.API_BASE_URL);
        Api api = retrofit.create(Api.class);
        api.GetAppFeature().enqueue(new Callback<FeatureModel>() {
            @Override
            public void onResponse(Call<FeatureModel> call, Response<FeatureModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        featuremodel = response.body().getList();
                        featureAdapter = new FeatureAdapter(getContext(), featuremodel, new FeatureAdapter.ItemClick() {
                            @Override
                            public void Click(FeatureList list) {
                                Intent i = new Intent(getContext(), Categoryitem.class);
                                i.putExtra("cat_id", list.getId());
                                i.putExtra("name", "Sub Category");
                                startActivity(i);
                            }
                        });
                        feature_rec.setAdapter(featureAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<FeatureModel> call, Throwable t) {

            }
        });


    }

    private void getHomeSliders() {
        Retrofit retrofit = commonFunction.createRetrofitObjectWithHeader(Constants.API_BASE_URL);
        Api api = retrofit.create(Api.class);
        api.getHomeSlider().enqueue(new Callback<HomeSlider>() {
            @Override
            public void onResponse(Call<HomeSlider> call, Response<HomeSlider> response) {
                if (response.body().isStatus()) {
                    imageModel = response.body().getData();
                    NUM_PAGES = imageModel.size();

                    viewPager_adpater = new ViewPager_Adapter(getContext(), imageModel);
                    viewPager.setAdapter(viewPager_adpater);

                    indicator.setViewPager(viewPager);
                    final float density = getResources().getDisplayMetrics().density;
                    //Set circle indicator radius
                    indicator.setRadius(4 * density);
                    Glide.with(getContext()).load(response.body().getBanner()).into(banner);
                }
            }

            @Override
            public void onFailure(Call<HomeSlider> call, Throwable t) {

            }
        });
    }

    private void get_sub_category() {
        progressDialog.show();
        sub_model = new ArrayList<>();
        Log.e("cat_id", cat_id);
        Call<SubcategoryModel> call = RetrofitClient.getInstance().getApi().GetSubcategory(cat_id);
        call.enqueue(new Callback<SubcategoryModel>() {
            @Override
            public void onResponse(Call<SubcategoryModel> call, Response<SubcategoryModel> response) {
                progressDialog.dismiss();
                if (response.body().getStatus()) {
                    seeall.setVisibility(View.VISIBLE);
                    sub_model = response.body().getSubcategory();
                    allDataAdapter = new SubCategoryAdapter(getActivity(), sub_model, new SubCategoryAdapter.ItemClick() {
                        @Override
                        public void Click(Subcategory list) {
                            Intent i = new Intent(getContext(), ProductsPage.class);
                            i.putExtra("list", list);
                            startActivity(i);
                        }


                    }, "home");
                    recyclerView.setAdapter(allDataAdapter);
                } else {
                    recyclerView.setAdapter(null);
                    seeall.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SubcategoryModel> call, Throwable t) {

            }
        });
    }

    private void getCategory() {
        Call<CategoryModel> call = RetrofitClient.getInstance().getApi().GetCategory();
        call.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                if (response.body().getStatus()) {
                    cat_model = new ArrayList<>();
                    tabLayout.removeAllTabs();
                    tabLayout.addTab(tabLayout.newTab().setText("All"));
                    cat_model = response.body().getCategory();
                    for (int i = 0; i < cat_model.size(); i++) {
                        tabLayout.addTab(tabLayout.newTab().setText(cat_model.get(i).getTitle()));
                    }
                }
            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getCategory();
    }

    private void TodaysDeal() {
        progressDialog.show();
        Retrofit retrofit = commonFunction.createRetrofitObjectWithHeader(Constants.API_BASE_URL);
        Api Apiinterface = retrofit.create(Api.class);
        Apiinterface.getTodaysDeals().enqueue(new Callback<ProductsModel>() {
            @Override
            public void onResponse(Call<ProductsModel> call, Response<ProductsModel> response) {
                progressDialog.dismiss();
                if (response.body().isStatus()) {
                    deals.setVisibility(View.VISIBLE);
                    model = new ArrayList<>();
                    model = response.body().getModel();
                    if (model.size() > 4) {
                        today_see.setVisibility(View.VISIBLE);
                    } else {
                        today_see.setVisibility(View.GONE);
                    }

                    adapter = new AllDataAdapter(getContext(), model, new AllDataAdapter.ItemClick() {
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
                    }, "home");
                    todays_rec.setAdapter(adapter);
                } else {
                    deals.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<ProductsModel> call, Throwable t) {
                Log.e("inside", "inside");
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