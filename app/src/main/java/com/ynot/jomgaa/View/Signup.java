package com.ynot.jomgaa.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ynot.jomgaa.Model.GetOTP;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup extends AppCompatActivity {
    Button signup;
    ImageView back;
    EditText password;
    TextInputEditText name, email, phone, pass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        signup = findViewById(R.id.signnnn);
        back = findViewById(R.id.back);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        pass = findViewById(R.id.pass);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckValiation()) {
                    getOTP();
                }
            }
        });
        password = findViewById(R.id.pass);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    password.setTextAppearance(R.style.normal);

                } else {
                    password.setTextAppearance(R.style.Textappierpass);
                    password.setLetterSpacing((float) .05);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void getOTP() {
        progressDialog.show();
        Call<GetOTP> call = RetrofitClient.getInstance().getApi().registration(email.getText().toString(), phone.getText().toString());
        call.enqueue(new Callback<GetOTP>() {
            @Override
            public void onResponse(Call<GetOTP> call, Response<GetOTP> response) {
                progressDialog.dismiss();
                if (response.body().isStatus()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name.getText().toString());
                    bundle.putString("email", email.getText().toString());
                    bundle.putString("phone", phone.getText().toString());
                    bundle.putString("pass", password.getText().toString());
                    bundle.putString("otp", response.body().getOtp());
                    Intent i = new Intent(getApplicationContext(), PhoneVerification.class);
                    i.putExtras(bundle);
                    startActivity(i);
                    Log.e("otp", response.body().getOtp());
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetOTP> call, Throwable t) {

            }
        });
    }

    private boolean CheckValiation() {
        if (name.getText().toString().isEmpty()) {
            name.requestFocus();
            name.setError("Please fill this field !!");
            return false;
        }
        if (!isValidEmail(email.getText().toString())) {
            email.requestFocus();
            email.setError("Please fill Valid Email-ID !!");
            return false;
        }
        if (phone.getText().length() != 10) {
            phone.requestFocus();
            phone.setError("Please Enter a Valid Phone number !!");
            return false;
        }
        if (password.getText().length() < 6) {
            password.requestFocus();
            password.setError("Password contains at least 6 Characters");
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}