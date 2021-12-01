package com.ynot.jomgaa.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ynot.jomgaa.FunctionModels.NormalData;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.Api;
import com.ynot.jomgaa.Web.CommonFunction;
import com.ynot.jomgaa.Web.Constants;
import com.ynot.jomgaa.Web.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Changepassword extends AppCompatActivity {
    ImageView back;
    EditText oldpassword, newpassword, conform;
    Button change;
    CommonFunction commonFunction;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        commonFunction = new CommonFunction(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");

        back = findViewById(R.id.backkk);
        oldpassword = findViewById(R.id.old);
        change = findViewById(R.id.button2);
        newpassword = findViewById(R.id.newpwd);
        conform = findViewById(R.id.connew);
        oldpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    oldpassword.setTextAppearance(R.style.normal);

                } else {
                    oldpassword.setTextAppearance(R.style.normal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        newpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    newpassword.setTextAppearance(R.style.normal);

                } else {
                    newpassword.setTextAppearance(R.style.Textappierpass);
                    newpassword.setLetterSpacing((float) .05);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        conform.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    conform.setTextAppearance(R.style.normal);

                } else {
                    conform.setTextAppearance(R.style.Textappierpass);
                    conform.setLetterSpacing((float) .05);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldpassword.getText().toString().isEmpty()) {
                    oldpassword.setError("Please enter old password !!");
                    oldpassword.requestFocus();
                    return;
                }
                if (newpassword.getText().toString().isEmpty()) {
                    newpassword.setError("Please enter New password !!");
                    newpassword.requestFocus();
                    return;
                }
                if (!conform.getText().toString().equals(newpassword.getText().toString())) {
                    conform.setError("Password miss match !!");
                    conform.requestFocus();
                    return;
                }
                ChangePassword();
            }
        });


    }

    private void ChangePassword() {
        progressDialog.show();
        Retrofit retrofit = commonFunction.createRetrofitObjectWithHeader(Constants.API_BASE_URL);
        Api api = retrofit.create(Api.class);
        api.ChangePassword(oldpassword.getText().toString(), newpassword.getText().toString(), SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id()).enqueue(new Callback<NormalData>() {
            @Override
            public void onResponse(Call<NormalData> call, Response<NormalData> response) {
                progressDialog.dismiss();
                if (response.body().isStatus()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NormalData> call, Throwable t) {

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
}
