package com.ynot.jomgaa.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.ynot.jomgaa.Model.GetOTP;
import com.ynot.jomgaa.Model.RegisterUser;
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

public class PhoneVerification extends AppCompatActivity implements View.OnKeyListener {
    ImageButton back;
    String from = "";
    Button verify;
    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    String name, email, address, pass, otp, new_otp, mob;
    TextView mob_txt, resend;
    ProgressDialog progressDialog;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        resend = findViewById(R.id.resend);
        mob_txt = findViewById(R.id.mob_txt);
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        if (getIntent().hasExtra("from")) {
            from = getIntent().getStringExtra("from");
        }

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        mob = getIntent().getStringExtra("phone");
        pass = getIntent().getStringExtra("pass");
        otp = getIntent().getStringExtra("otp");

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
        //    mob_txt.setText(" "+email);


        back = findViewById(R.id.backkk);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        verify = findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from.equals("forgot")) {
                    if (otp1.getText().toString().equals("") || otp2.getText().toString().equals("") || otp3.getText().toString().equals("") || otp4.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please enter 4 digit OTP", Toast.LENGTH_LONG).show();
                        otp1.setError("invalid OTP");
                        otp1.requestFocus();
                        otp1.setCursorVisible(true);
                    } else {
                        new_otp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
                        if (new_otp.equals(otp)) {
                            Intent i = new Intent(getApplicationContext(), Forgotpassword.class);
                            i.putExtra("email", email);
                            i.putExtra("change", "change");
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "OTP Verification failed !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (otp1.getText().toString().equals("") || otp2.getText().toString().equals("") || otp3.getText().toString().equals("") || otp4.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please enter 4 digit OTP", Toast.LENGTH_LONG).show();
                        otp1.setError("invalid OTP");
                        otp1.requestFocus();
                        otp1.setCursorVisible(true);
                    } else {
                        new_otp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
                        if (new_otp.equals(otp)) {
                            verification(new_otp);
                        } else {
                            Toast.makeText(getApplicationContext(), "OTP Verification failed !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
        otp1.addTextChangedListener(new GenericTextWatcher(otp1));
        otp2.addTextChangedListener(new GenericTextWatcher(otp2));
        otp3.addTextChangedListener(new GenericTextWatcher(otp3));
        otp4.addTextChangedListener(new GenericTextWatcher(otp4));

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("forgot")) {
                    send_mob();
                } else {
                    getOTP();
                }

            }
        });
    }

    private void verification(String new_otp) {
        progressDialog.show();
        Call<RegisterUser> call = RetrofitClient.getInstance().getApi().Registeruser(name, otp, new_otp, mob, email, pass, token);
        call.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                progressDialog.dismiss();
                if (response.body().isStatus()) {
                    Log.e("resp", String.valueOf(response.body()));
                    User user = new User(response.body().getUser_id(), "", name, email, mob);
                    SharedPrefManager.getInstatnce(getApplicationContext()).userLogin(user);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    Toast.makeText(getApplicationContext(), "Registration Successful !!", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {

            }
        });


    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {


                case R.id.otp1:
                    if (text.length() == 1)
                        otp2.requestFocus();
                    otp2.setCursorVisible(true);

                    break;
                case R.id.otp2:
                    if (text.length() == 1)
                        otp3.requestFocus();
                    otp3.setCursorVisible(true);

                    break;
                case R.id.otp3:
                    if (text.length() == 1)
                        otp4.requestFocus();
                    otp4.setCursorVisible(true);

                    break;
                case R.id.otp4:
                    if (text.length() == 1) {
                        otp4.setCursorVisible(false);

                        new_otp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
                        if (!new_otp.equals(otp)) {
                            otp4.setError("invalid OTP");
                            otp4.setCursorVisible(true);

                        }
                    }

                    break;


            }

        }

    }

    private void getOTP() {
        progressDialog.show();
        Call<GetOTP> call = RetrofitClient.getInstance().getApi().registration(email, mob);
        call.enqueue(new Callback<GetOTP>() {
            @Override
            public void onResponse(Call<GetOTP> call, Response<GetOTP> response) {
                progressDialog.dismiss();
                if (response.body().isStatus()) {
                    otp = response.body().getOtp();
                    Log.e("otp", response.body().getOtp());
                    Toast.makeText(getApplicationContext(), "OTP Sending Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "OTP Sending Failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetOTP> call, Throwable t) {

            }
        });
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
                                otp = ob.getString("otp");
                                Toast.makeText(getApplicationContext(), "OTP Resend successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "OTP Verification failed", Toast.LENGTH_SHORT).show();
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
                params.put("email", email);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(90 * 1000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getmInstance(this).addToRequestQueue(stringRequest);
    }
}