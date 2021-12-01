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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.SharedPrefManager;
import com.ynot.jomgaa.Web.URLs;
import com.ynot.jomgaa.Web.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Forgotpassword extends AppCompatActivity {
    EditText newpass, confirmpass;
    Button save;
    String email, ch = "";
    TextInputLayout ne, connew;
    ImageButton backkk;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        newpass = findViewById(R.id.newpwd);
        confirmpass = findViewById(R.id.connew);
        backkk = findViewById(R.id.backkk);

        ne = findViewById(R.id.textInputLayout6);
        connew = findViewById(R.id.textInputLayout7);
        if (getIntent().hasExtra("change")) {
            ch = getIntent().getStringExtra("change");
        }


        if (getIntent().hasExtra("email")) {
            email = getIntent().getStringExtra("email");
        }
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onClick(View view) {

                if (newpass.getText().toString().isEmpty() || newpass.getText().toString().length() < 6) {
                    newpass.setError("Password contains atleast 6 characters");
                    ne.setPasswordVisibilityToggleEnabled(false);
                    newpass.requestFocus();
                    Log.e("reg", "pass");
                    return;

                }

                if (newpass.getText().toString().length() > 20) {

                    newpass.setError("Password is too long.....");
                    ne.setPasswordVisibilityToggleEnabled(false);
                    newpass.requestFocus();
                    Log.e("reg", "pass");
                    return;

                } else if (newpass.getText().toString().equals(confirmpass.getText().toString())) {
                    change_pass();
                } else {
                    confirmpass.setError("Password Mismatch");
                    connew.setPasswordVisibilityToggleEnabled(false);
                }
            }
        });
        newpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                ne.setPasswordVisibilityToggleEnabled(true);

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    ne.setPasswordVisibilityToggleEnabled(true);
                } else {
                    ne.setPasswordVisibilityToggleEnabled(true);
                    newpass.setLetterSpacing((float) .2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        confirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (newpass.getText().toString().isEmpty() || newpass.getText().toString().length() < 6) {
                    newpass.setError("Password contains atleast 6 characters");
                    ne.setPasswordVisibilityToggleEnabled(false);
                    newpass.requestFocus();
                    Log.e("reg", "pass");
                    return;

                }

                if (newpass.getText().toString().length() > 20) {

                    newpass.setError("Password is too long try some other");
                    ne.setPasswordVisibilityToggleEnabled(false);
                    newpass.requestFocus();
                    Log.e("reg", "pass");
                    return;

                }

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    connew.setPasswordVisibilityToggleEnabled(true);


                } else {
                    confirmpass.setLetterSpacing((float) .2);
                    connew.setPasswordVisibilityToggleEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        backkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void change_pass() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.RESET_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("change", response);
                        try {
                            JSONObject ob = new JSONObject(response);
                            if (ob.getBoolean("status")) {

                                if (ch.equals("change")) {
                                    SharedPrefManager.getInstatnce(getApplicationContext()).logout();
                                    finishAffinity();
                                    Toast.makeText(Forgotpassword.this, "Password Successfully Changed !! ", Toast.LENGTH_SHORT).show();
                                } else {
                                    SharedPrefManager.getInstatnce(getApplicationContext()).logout();
                                    finishAffinity();
                                    Toast.makeText(Forgotpassword.this, "Password Successfully Changed !!", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(Forgotpassword.this, ob.getString("error_message"), Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", confirmpass.getText().toString());
                params.put("email", email);
                Log.e("changepasswordinput", params.toString());

                return params;
            }
        };
        VolleySingleton.getmInstance(this).addToRequestQueue(stringRequest);
    }
}