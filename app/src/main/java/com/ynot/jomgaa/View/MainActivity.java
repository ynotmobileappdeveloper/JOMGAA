package com.ynot.jomgaa.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.ynot.jomgaa.Notification.NotificationCountSetClass;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.SharedPrefManager;
import com.ynot.jomgaa.Web.URLs;
import com.ynot.jomgaa.Web.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView location;
    private BottomNavigationView btmnav;
    NavController navController;
    private AppBarConfiguration appBarConfiguration;
    TextView count;
    RelativeLayout cart, whatsapp_layout;
    TextView name, email, phone;
    TextView map_location;
    public String fragment = "main";
    Toolbar toolbar;
    private static final int REQUEST_CHECK_SETTINGS = 102;
    private static final int PERMISSION_ID = 102;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private LocationCallback locationCallback;
    NavigationView navigationView;
    ImageView slide;
    FrameLayout container;
    boolean isbackpressed = false;
    TextView home, profile, locations, mypurchase, myordrs, logout, changepswd;
    DrawerLayout drawer;
    int cart_count;

    //ActivityMainBinding binding;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count = findViewById(R.id.count);
        container = findViewById(R.id.container);
        toolbar = findViewById(R.id.toolbar5);
        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        name = findViewById(R.id.name);
        email = findViewById(R.id.gmail);
        home = findViewById(R.id.home);

        profile = findViewById(R.id.profile);
        locations = findViewById(R.id.location);
        mypurchase = findViewById(R.id.mypurchase);
        myordrs = findViewById(R.id.orders);
        changepswd = findViewById(R.id.changepwd);
        logout = findViewById(R.id.logout);
        btmnav = findViewById(R.id.bottemnav);
        btmnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Fragment myFragment = new DashboardFragment(MainActivity.this);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                        break;
                    case R.id.navigation_fav:
                        Fragment myFragment2 = new ShopsFragment(MainActivity.this);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment2).addToBackStack(null).commit();
                        break;
                    case R.id.navigation_catogory:
                        Fragment myFragment1 = new CategoriesFragment(MainActivity.this);
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment1).addToBackStack(null).commit();
                        break;
                    case R.id.navigation_search:
                        Fragment search = new Searchfrag();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, search).addToBackStack(null).commit();
                        break;
                }
                return true;
            }
        });
        setupDrawer();

        openFragment(new DashboardFragment());

        name.setText(SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getName());
        email.setText(SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getMail_id());

        home.setOnClickListener(this);
        profile.setOnClickListener(this);
        locations.setOnClickListener(this);
        mypurchase.setOnClickListener(this);
        myordrs.setOnClickListener(this);
        changepswd.setOnClickListener(this);
        logout.setOnClickListener(this);
        if (getIntent().hasExtra("cart")) {
            Fragment f = new CartFragment(MainActivity.this);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, f).addToBackStack(null).commit();
        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home:
                drawer.closeDrawer(navigationView);
                Fragment myFragment = new DashboardFragment(MainActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
                break;
            case R.id.profile:
                //startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
                drawer.closeDrawer(navigationView);
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.location:
                drawer.closeDrawer(navigationView);
                startActivity(new Intent(this, AddressPage.class));
                break;

            case R.id.mypurchase:
                drawer.closeDrawer(navigationView);
                Fragment myFragment4 = new PurchaseFragment(MainActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment4).addToBackStack(null).commit();
                break;
            case R.id.changepwd:
                drawer.closeDrawer(navigationView);
                startActivity(new Intent(this, Changepassword.class));
                break;
            case R.id.logout:
                SharedPrefManager.getInstatnce(getApplicationContext()).logout();
                drawer.closeDrawer(navigationView);
                break;

            case R.id.orders:
                drawer.closeDrawer(navigationView);
                startActivity(new Intent(this, MyOrderDetails.class));
                break;
         /*   case R.id.cart:

                drawer.closeDrawer(navigationView);
                Fragment f = new CartFragment(MainActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, f).addToBackStack(null).commit();
                break;*/

            case R.id.navigation_fav:
                Fragment myFragment2 = new ShopsFragment(MainActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment2).addToBackStack(null).commit();
                break;
            case R.id.navigation_catogory:
                // openFragment(new CategoriesFragment(DashboardActivity.this));
                Fragment myFragment3 = new CategoriesFragment(MainActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment3).addToBackStack(null).commit();
                break;
            case R.id.navigation_search:
                Fragment myFragment5 = new Searchfrag();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment5).addToBackStack(null).commit();
                break;

        }
    }


    private void setupDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,
                drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.ic_baseline_more_vert_24);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else {

            if (isbackpressed) {
                super.onBackPressed();
                return;
            }
            this.isbackpressed = true;
            Toast.makeText(this, "Please click BACK again to exit!!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isbackpressed = false;
                }
            }, 2000);
            return;
        }
    }

    public void openFragment(Fragment homeFragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, homeFragment).commit();

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.cart);
        NotificationCountSetClass.setAddToCart(MainActivity.this, item, cart_count);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cart:
                Fragment f = new CartFragment(MainActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, f).addToBackStack(null).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cart_count() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.CART_COUNT,
                new Response.Listener<String>() {
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
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        cart_count();
    }
}