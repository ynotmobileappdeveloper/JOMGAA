package com.ynot.jomgaa.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ynot.jomgaa.FunctionModels.ProfileModel;
import com.ynot.jomgaa.Model.User;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.RetrofitClient;
import com.ynot.jomgaa.Web.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    ImageView back;
    TextInputEditText name, email, mobile;
    Button save;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        back = findViewById(R.id.back);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        save = findViewById(R.id.button4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        name.setText(SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getName());
        email.setText(SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getMail_id());
        mobile.setText(SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getMobile());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (save.getText().toString().equals("Save")) {
                    if (name.getText().toString().isEmpty()) {
                        name.setError("Please fill this field !!");
                        name.requestFocus();
                        return;
                    }
                    if (!isValidEmail(email.getText().toString())) {
                        email.setError("Please Enter a Valid Email number !!");
                        email.requestFocus();
                        return;
                    }
                    Edit_profile();
                } else {
                    name.setEnabled(true);
                    email.setEnabled(true);
                    mobile.setEnabled(false);
                    save.setText("Save");
                }
            }
        });

    }

    private void Edit_profile() {
        progressDialog.show();
        Call<ProfileModel> call = RetrofitClient.getInstance().getApi().EditProfile(
                SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id(),
                email.getText().toString(),
                name.getText().toString());
        call.enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        User user = new User(SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id(),
                                SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getRef_code(),
                                name.getText().toString(), SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getMail_id(),
                                mobile.getText().toString());
                        SharedPrefManager.getInstatnce(getApplicationContext()).userLogin(user);
                        Toast.makeText(getApplicationContext(), "Profile Updated Successfully !!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went Wrong !!", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}