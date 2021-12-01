package com.ynot.jomgaa.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {
    //
//    Animation animation;
//    int appversion;
//    boolean check = false;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);
        Glide.with(getApplicationContext()).load(R.drawable.u).into(logo);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (SharedPrefManager.getInstatnce(getApplicationContext()).isLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), BannerActivity.class));
                    finish();
                }
            }
        }, 2000);

    }


}


