package com.ynot.jomgaa.View;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputEditText;
import com.ynot.jomgaa.Model.PincodeModel;
import com.ynot.jomgaa.Model.PlaceModel;
import com.ynot.jomgaa.R;
import com.ynot.jomgaa.Web.SharedPrefManager;
import com.ynot.jomgaa.Web.URLs;
import com.ynot.jomgaa.Web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddAddress extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {


    TextInputEditText name, phone;
    EditText address, landmark, pincode;
    Button save;
    CheckBox check;
    String address_status = "0", checkout = "";
    ProgressDialog progress;
    String add_id;
    String latitiude, longititude, apiKey;
    ArrayList<PlaceModel> placeModel = new ArrayList<>();
    ArrayList<PincodeModel> pincodeModel = new ArrayList<>();
    List<String> placename = new ArrayList<>();
    List<String> pincodename = new ArrayList<>();
    ProgressDialog progressDialog;
    Toolbar toolbar;

    private GoogleMap map;
    SupportMapFragment mapFragment;
    Marker now;
    String cp_amount = "", d_charge = "", grand_total = "", cp_code = "";


    private static final int REQUEST_CHECK_SETTINGS = 102;
    private static final int PERMISSION_ID = 102;
    private FusedLocationProviderClient fusedLocationClient;
    LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        progressDialog = new ProgressDialog(AddAddress.this);
        progressDialog.setMessage("Please wait...");
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        save = findViewById(R.id.save);
        check = findViewById(R.id.check);
        landmark = findViewById(R.id.landmark);
        toolbar = findViewById(R.id.toolbar9);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(AddAddress.this);
        createLocationRequest();

        if (getIntent().hasExtra("checkout")) {
            checkout = getIntent().getStringExtra("checkout");
            cp_amount = getIntent().getStringExtra("cp_amount");
            cp_code = getIntent().getStringExtra("cp_code");
            d_charge = getIntent().getStringExtra("d_charge");
            grand_total = getIntent().getStringExtra("grand_total");
        }


        get_api();


        progress = new ProgressDialog(AddAddress.this);
        progress.setMessage("Saving address...");


        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    address_status = "1";
                } else {
                    address_status = "0";
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (address.getText().toString().isEmpty()) {
                    address.setError("Enter address !!");
                    address.requestFocus();
                    return;
                }

                if (landmark.getText().toString().isEmpty()) {
                    landmark.setError("Enter Landmark !!");
                    landmark.requestFocus();
                    return;
                }
                if (pincode.getText().toString().isEmpty()) {
                    pincode.setError("Enter Pincode !!");
                    pincode.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(latitiude) || TextUtils.isEmpty(longititude)) {
                    Toast.makeText(getApplicationContext(), "Please Choose Map Location !!", Toast.LENGTH_SHORT).show();
                    return;
                }
                save_address();

            }
        });


    }


    private void save_address() {

        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.SAVE_ADDRESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("add_address", response);
                        progress.dismiss();

                        try {
                            JSONObject ob = new JSONObject(response);

                            if (ob.getBoolean("status")) {

                                if (checkout.equals("checkout")) {
                                    Intent i = new Intent(getApplicationContext(), ConfirmAddress.class);
                                    i.putExtra("cp_amount", cp_amount);
                                    i.putExtra("d_charge", d_charge);
                                    i.putExtra("grand_total", grand_total);
                                    i.putExtra("cp_code", cp_code);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(AddAddress.this, "Address Successfully Saved  !!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            } else {
                                Toast.makeText(AddAddress.this, "Something Went Wrong !!", Toast.LENGTH_SHORT).show();
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

                params.put("address", address.getText().toString());
                params.put("address_status", address_status);
                params.put("landmark", landmark.getText().toString());
                params.put("pincode", pincode.getText().toString());
                params.put("latitude", latitiude);
                params.put("longitude", longititude);
                params.put("user_id", SharedPrefManager.getInstatnce(getApplicationContext()).getUser().getUser_id());
                Log.e("save_params", String.valueOf(params));
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

    private void common_method() {

        Places.initialize(getApplicationContext(), apiKey);
        final PlacesClient placesClient = Places.createClient(this);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.sv_location);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // txtVw.setText(place.getName()+place.getAddress());
                    map.clear();
                    latitiude = String.valueOf(place.getLatLng().latitude);
                    longititude = String.valueOf(place.getLatLng().longitude);
                    String location = place.toString();
                    List<Address> addressList = null;
                    if (location != null || !location.equals("")) {
                        Geocoder geocoder = new Geocoder(AddAddress.this);
                        try {
                            addressList = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                            if (addressList.size() != 0) {

                                map.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16));

                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                latitiude = String.valueOf(latLng.latitude);
                                longititude = String.valueOf(latLng.longitude);
                                map.clear();
                                now = map.addMarker(new MarkerOptions().position(latLng));
                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));


                                LatLng pp = now.getPosition();
                                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(pp.latitude, pp.longitude, 1);
                                    Address obj = addresses.get(0);


                                    String add = obj.getAddressLine(0);
                                    add = add + "\n" + obj.getCountryName();
                                    add = add + "\n" + obj.getCountryCode();
                                    add = add + "\n" + obj.getAdminArea();
                                    add = add + "\n" + obj.getPostalCode();
                                    add = add + "\n" + obj.getSubAdminArea();
                                    add = add + "\n" + obj.getLocality();
                                    add = add + "\n" + obj.getSubThoroughfare();
                                    Log.e("address", add);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                    }


                }

                @Override
                public void onError(Status status) {
                    // txtVw.setText(status.toString());
                    Log.e("error", String.valueOf(status));
                }
            });
        }
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        // location_set();
        map.setOnMapClickListener(this);
        askLocationSettings();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    //checkLocationAPI(location.getLatitude()+"",location.getLongitude()+"");
                    FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(AddAddress.this);
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // GPS location can be null if GPS is switched off
                                    if (location != null) {
                                        //lastLocation=location;
                                        try {
                                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            if (addresses != null && addresses.size() > 0) {
                                                String address = addresses.get(0).getAddressLine(0);
                                                String city = addresses.get(0).getLocality();


                                                latitiude = String.valueOf(location.getLatitude());
                                                longititude = String.valueOf(location.getLongitude());
                                                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                                                map.addMarker(new MarkerOptions().position(sydney).title("My Current location"));
                                                map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
                                                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                                    @Override
                                                    public void onMapClick(LatLng latLng) {
                                                        map.clear();
                                                        now = map.addMarker(new MarkerOptions().position(latLng));
                                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                                                        latitiude = String.valueOf(latLng.latitude);
                                                        longititude = String.valueOf(latLng.longitude);

                                                        LatLng pp = now.getPosition();
                                                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                                        try {
                                                            List<Address> addresses = geocoder.getFromLocation(pp.latitude, pp.longitude, 1);
                                                            Address obj = addresses.get(0);


                                                            String add = obj.getAddressLine(0);
                                                            add = add + "\n" + obj.getCountryName();
                                                            add = add + "\n" + obj.getCountryCode();
                                                            add = add + "\n" + obj.getAdminArea();
                                                            add = add + "\n" + obj.getPostalCode();
                                                            add = add + "\n" + obj.getSubAdminArea();
                                                            add = add + "\n" + obj.getLocality();
                                                            add = add + "\n" + obj.getSubThoroughfare();
                                                            Log.e("address", add);

                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        return;
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("MapDemoActivity", "Error trying to get last GPS location");
                                    e.printStackTrace();
                                }
                            });
                    fusedLocationClient.removeLocationUpdates(locationCallback);
                }
            }
        };

    }

    private void get_api() {
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.GET_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject ob = new JSONObject(response);

                            if (ob.getBoolean("status")) {
                                apiKey = ob.getString("api_key");
                                common_method();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                //params.put("")

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 1000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getmInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onMapClick(LatLng latLng) {
        map.clear();
        now = map.addMarker(new MarkerOptions().position(latLng));
        // now.setPosition(new latLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        now.getTitle();

        latitiude = String.valueOf(latLng.latitude);
        longititude = String.valueOf(latLng.longitude);
        Log.e("lat", latitiude);
        Log.e("lon", longititude);

        LatLng pp = now.getPosition();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(pp.latitude, pp.longitude, 1);
            Address obj = addresses.get(0);


            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();
            Log.e("address", add);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void location_set() {

        double la = Double.parseDouble(latitiude);
        double lo = Double.parseDouble(longititude);

        //  google_location.setText(landmark);
        LatLng sydney = new LatLng(la, lo);
        map.addMarker(new MarkerOptions().position(sydney).title("Item Delivered here"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));

    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void askLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(AddAddress.this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(AddAddress.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.e("Location", "ON");
                getLastLocation();
            }
        });

        task.addOnFailureListener(AddAddress.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Location", "OFF");
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(AddAddress.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private void getLastLocation() {
        map.clear();
        if (checkPermissions()) {
            map.clear();
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(AddAddress.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                //checkLocationAPI(location.getLatitude()+"",location.getLongitude()+"");
                                FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(AddAddress.this);
                                mFusedLocationClient.getLastLocation()
                                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {
                                                // GPS location can be null if GPS is switched off
                                                if (location != null) {
                                                    //lastLocation=location;
                                                    try {
                                                        Log.e("latitude", String.valueOf(location.getLatitude()));
                                                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                                        if (addresses != null && addresses.size() > 0) {
                                                            String address = addresses.get(0).getAddressLine(0);
                                                            String city = addresses.get(0).getLocality();


                                                            Log.e("sub_locality", String.valueOf(addresses.get(0).getSubLocality()));
                                                            Log.e("gextra", String.valueOf(addresses.get(0).getExtras()));
                                                            Log.e("feature_name", String.valueOf(addresses.get(0).getFeatureName()));


                                                            latitiude = String.valueOf(location.getLatitude());
                                                            longititude = String.valueOf(location.getLongitude());
                                                            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                                                            map.clear();
                                                            map.addMarker(new MarkerOptions().position(sydney).title("My Current location"));
                                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
                                                            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                                                @Override
                                                                public void onMapClick(LatLng latLng) {
                                                                    map.clear();
                                                                    now = map.addMarker(new MarkerOptions().position(latLng));
                                                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

                                                                    latitiude = String.valueOf(latLng.latitude);
                                                                    longititude = String.valueOf(latLng.longitude);

                                                                    LatLng pp = now.getPosition();
                                                                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                                                    try {
                                                                        List<Address> addresses = geocoder.getFromLocation(pp.latitude, pp.longitude, 1);
                                                                        Address obj = addresses.get(0);


                                                                        String add = obj.getAddressLine(0);
                                                                        add = add + "\n" + obj.getCountryName();
                                                                        add = add + "\n" + obj.getCountryCode();
                                                                        add = add + "\n" + obj.getAdminArea();
                                                                        add = add + "\n" + obj.getPostalCode();
                                                                        add = add + "\n" + obj.getSubAdminArea();
                                                                        add = add + "\n" + obj.getLocality();
                                                                        add = add + "\n" + obj.getSubThoroughfare();
                                                                        Log.e("address", add);

                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    return;
                                                }

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("MapDemoActivity", "Error trying to get last GPS location");
                                                e.printStackTrace();
                                            }
                                        });
                                startLocationUpdates();
                            } else {
                                startLocationUpdates();
                            }
                        }
                    });
        } else {
            requestPermissions();
        }

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                AddAddress.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    //check permission
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CHECK_SETTINGS) {
                Log.e("Location", "ON");
                getLastLocation();
                //startLocationUpdates();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
                //startLocationUpdates();
            }
        }
    }
}
