package com.ynot.jomgaa.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ynot.jomgaa.Model.LoginUser;
import com.ynot.jomgaa.Model.User;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.RetrofitClient;
import com.ynot.jomgaa.Web.SharedPrefManager;
import com.ynot.jomgaa.Web.URLs;
import com.ynot.jomgaa.Web.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    Button login, signup;
    ImageButton back;
    TextView fogot;
    TextInputEditText email, password;
    ProgressDialog progressDialog;
    LoginUser user;
    String token = "", device_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signuplog);
        back = findViewById(R.id.backbtn);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

        fogot = findViewById(R.id.forgottt);
        device_id = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        token = task.getResult();
                        Log.e("token", token);
                    }
                });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().length() != 10) {
                    email.setError("Please Enter a Valid Phone Number !");
                    email.requestFocus();
                }
                UserLogin();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Signup.class));

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fogot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().length() > 0) {
                    if (!isValidEmail(email.getText().toString())) {
                        email.setError("enter a valid Email id");
                    } else {
                        send_mob();
                    }
                } else {
                    email.setError("email field empty");
                }
            }
        });
    }

    private void UserLogin() {
        progressDialog.show();
        Call<LoginUser> call = RetrofitClient.getInstance().getApi().userLogin(email.getText().toString(), token, device_id);
        call.enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                progressDialog.dismiss();
                if (response.body().getStatus()) {
                    user = response.body();
                    Intent i = new Intent(getApplicationContext(), LoginOTPPhoneVerification.class);
                    i.putExtra("user", user);
                    i.putExtra("mob", email.getText().toString());
                    i.putExtra("token", token);
                    i.putExtra("device_id", device_id);
                    Log.e("otp", response.body().getOtp());
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), response.body().getErorrMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {

            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    private void send_mob() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.SEND_OTP,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("snd", response);
                        progressDialog.dismiss();

                        try {
                            JSONObject ob = new JSONObject(response);
                            if (ob.getBoolean("status")) {
                                Intent i = new Intent(getApplicationContext(), PhoneVerification.class);
                                i.putExtra("otp", ob.getString("otp"));
                                i.putExtra("email", email.getText().toString());
                                i.putExtra("from", "forgot");

                                startActivity(i);

                            } else {
                                email.setError(ob.getString("message"));
                                email.requestFocus();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("error", new String(error.networkResponse.data));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(90 * 1000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getmInstance(this).addToRequestQueue(stringRequest);
    }
}