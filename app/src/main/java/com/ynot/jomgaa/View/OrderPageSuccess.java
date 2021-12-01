package com.ynot.jomgaa.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.SharedPrefManager;


public class OrderPageSuccess extends AppCompatActivity {
    TextView order,amount,gmail;
    CardView cardView;
    Button home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page_success);
            order=findViewById(R.id.orderId);
            amount=findViewById(R.id.amt);
            gmail=findViewById(R.id.email);
            cardView=findViewById(R.id.card);
            home=findViewById(R.id.back);

            if (getIntent().hasExtra("order"))
            {
                order.setText(getIntent().getStringExtra("order"));
                amount.setText("Rs.  "+getIntent().getStringExtra("amount"));
                gmail.setText(SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getMail_id());
            }
            Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.pop);
            cardView.startAnimation(animation);
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finishAffinity();
                }
            });
        }

        @Override
        public void onBackPressed() {
            //super.onBackPressed();
        }
    }
