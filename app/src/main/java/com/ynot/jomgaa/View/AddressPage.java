package com.ynot.jomgaa.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ynot.jomgaa.Model.AddressModel;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.SharedPrefManager;
import com.ynot.jomgaa.Web.URLs;
import com.ynot.jomgaa.Web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressPage extends AppCompatActivity {


    RecyclerView add_list;
    ArrayList<AddressModel> model = new ArrayList<>();
    Button add_address;
    AddressAdapter adapter;
    String address_id = "";
    ProgressDialog progress;
    int select_last_pos;
    String add_status = "";
    ProgressDialog progress_del;
    LinearLayout no_address;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_page);
        add_list = findViewById(R.id.address_list);
        add_address = findViewById(R.id.add);
        no_address = findViewById(R.id.no_address);
        add_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        toolbar = findViewById(R.id.toolbar4);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progress = new ProgressDialog(AddressPage.this);
        progress.setMessage("Please wait...");
        progress_del = new ProgressDialog(AddressPage.this);
        progress_del.setMessage("Deleting Address...");
        progress_del.setCancelable(false);


        if (getIntent().hasExtra("address")) {
            add_status = getIntent().getStringExtra("address");
        }


        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (add_status.equals("address")) {
                    Intent i = new Intent(getApplicationContext(), AddAddress.class);
                    i.putExtra("checkout", "checkout");
                    startActivity(i);
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), AddAddress.class));
                }

            }
        });


    }

    private void get_address() {
        progress.show();

        if (model.size() > 0) {
            model.clear();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ADDRESS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        Log.e("address_list", response);
                        try {
                            JSONObject ob = new JSONObject(response);

                            if (ob.getBoolean("status")) {
                                model = new ArrayList<>();
                                no_address.setVisibility(View.GONE);
                                JSONArray array = ob.getJSONArray("addresslist");
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject o = array.getJSONObject(i);

                                    AddressModel mo = new AddressModel();
                                    mo.setId(o.getString("add_id"));
                                    mo.setAddress(o.getString("address"));
                                    mo.setPincode(o.getString("pincode"));
                                    mo.setAddress_status(o.getString("address_status"));
                                    mo.setLandmark(o.getString("landmark"));
                                    mo.setLatitude(o.getString("latitude"));
                                    mo.setLongitude(o.getString("longitude"));

                                    model.add(mo);

                                }
                                adapter = new AddressAdapter(AddressPage.this, model);
                                adapter.address(add_status);
                                add_list.setAdapter(adapter);

                            } else {
                                no_address.setVisibility(View.VISIBLE);
                                Toast.makeText(AddressPage.this, "No Address !!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();

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

    private class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

        Context context;
        ArrayList<AddressModel> model;
        String status;


        public AddressAdapter(Context applicationContext, ArrayList<AddressModel> model) {
            this.context = applicationContext;
            this.model = model;

        }

        public void address(String status) {
            this.status = status;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.address_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final AddressModel list = model.get(position);
            if (Integer.valueOf(list.getAddress_status()) == 1) {
                holder.defaltststus.setChecked(true);
            } else {
                holder.defaltststus.setChecked(false);
            }

            if (status.equals("address")) {
                holder.remove.setText("Set Default");
            }

            holder.address.setText(list.getAddress() + "\n" + list.getLandmark());
            holder.pincode.setText("Pincode: " + list.getPincode());
            holder.add.setText(SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getName());
           /* if (list.getAddress_status().equals("1")) {
                holder.check.setChecked(true);
            }*/

           /* holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        holder.check.setChecked(true);
                        model.get(select_last_pos).setAddress_status("0");
                        notifyItemChanged(select_last_pos);
                        address_id = list.getId();
                        select_last_pos = holder.getAdapterPosition();

                    } else {
                        address_id = "";

                    }
                    model.get(holder.getAdapterPosition()).setAddress_status("1");
                }
            });*/
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EditAddress.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("id", list.getId());
                    i.putExtra("address", list.getAddress());
                    i.putExtra("pincode", list.getPincode());
                    i.putExtra("landmark", list.getLandmark());
                    i.putExtra("status", list.getAddress_status());
                    i.putExtra("latitude", list.getLatitude());
                    i.putExtra("longitude", list.getLongitude());
                    i.putExtra("edit", "edit");
                    context.startActivity(i);
                }
            });

            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("remove", "remove");
                    if (status.equals("address")) {
                        set_default(list.getId());
                    } else {
                        Log.e("remove", "remove");
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("Are You Sure To Delete ?");
                        alert.setTitle("Confirm");
                        alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                remove_address(list.getId(), position);
                            }
                        });
                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert.show();
                    }

                }
            });


            holder.defaltststus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.defaltststus.isChecked()) {
                        String sta = "1";
                        list.setAddress_status("1");
                        chage_defaltAddress(list.getAddress(), sta, list.getLandmark(), list.getPincode(), list.getLatitude(), list.getLongitude(), list.getId());
                    } else {
                        String sta = "0";
                        chage_defaltAddress(list.getAddress(), sta, list.getLandmark(), list.getPincode(), list.getLatitude(), list.getLongitude(), list.getId());
                    }

                }
            });

        }

        private void set_default(final String id) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.SET_DEFAULT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("default", response);
                            try {
                                JSONObject ob = new JSONObject(response);

                                if (ob.getBoolean("status")) {
                                    Toast.makeText(context, "Set as Default !!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(context, "Something Went Wrong ", Toast.LENGTH_SHORT).show();
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

                    params.put("add_id", id);
                    params.put("user_id", SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());

                    return params;
                }
            };
            VolleySingleton.getmInstance(context).addToRequestQueue(stringRequest);

        }

        @Override
        public int getItemCount() {
            return model.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            Button edit, remove;
            TextView name, phone, address, pincode, add;
            CheckBox check;
            RadioButton defaltststus;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                edit = itemView.findViewById(R.id.edit);
                remove = itemView.findViewById(R.id.remove);
                name = itemView.findViewById(R.id.name);
                phone = itemView.findViewById(R.id.phone);
                address = itemView.findViewById(R.id.name);
                pincode = itemView.findViewById(R.id.pincode);
                check = itemView.findViewById(R.id.check);
                add = itemView.findViewById(R.id.add);
                defaltststus = itemView.findViewById(R.id.defaultadd);
            }
        }

        private void remove_address(final String id, final int position) {

            progress_del.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.REMOVE_ADDRESS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progress_del.dismiss();
                            try {
                                JSONObject ob = new JSONObject(response);

                                if (ob.getBoolean("status")) {
                                    model.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), "Address Removed Successfully !", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Something Went Wrong !!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress_del.dismiss();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());
                    params.put("add_id", id);
                    return params;
                }
            };
            VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        }
    }

    private void chage_defaltAddress(String address, String sta, String landmark, String pincode, String latitude, String longitude, String id) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.EDIT_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        Log.e("edit_address", response);
                        try {
                            JSONObject ob = new JSONObject(response);
                            if (ob.getBoolean("status")) {

                                Toast.makeText(AddressPage.this, "Default Address changed Successfully ", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(), AddressPage.class));
//                                finish();
                                get_address();
                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(AddressPage.this, "Something Went Wrong !!", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("address", address);
                params.put("address_status", sta);
                params.put("landmark", landmark);
                params.put("pincode", pincode);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("user_id", SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());
                params.put("add_id", id);

                Log.e("edit_save", String.valueOf(params));

                return params;
            }
        };
        VolleySingleton.getmInstance(getApplicationContext()).addToRequestQueue(stringRequest);


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

    @Override
    protected void onResume() {
        super.onResume();
        get_address();
    }
}

