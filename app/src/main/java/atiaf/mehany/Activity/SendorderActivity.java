package atiaf.mehany.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Activity.phase2.OtherServicesActivity;
import atiaf.mehany.Activity.phase2.ads.ActivityAdsContent;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Country;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class SendorderActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback,
        ResultCallback<LocationSettingsResult>, GoogleMap.OnMapClickListener {
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    long interval = 10 * 1000;   // 10 seconds, in milliseconds
    long fastestInterval = 1000;  // 1 second, in milliseconds
    float minDisplacement = 0;

    // Might be null if Google Play services APK is not available.

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    Marker marker;
    LatLng clientLatLng;
    GoogleMap mMap;
    String lat, lon;
    TextView menu, icon, t1, t2, icon1;
    TextViewWithFont loc, txt, txt1, senorder, view;
    LinearLayout lin, lin1, linsearch, lin_services, linsuccess, linteacher, lintechnical;
    Spinner spin, spin1, spintech;
    EditText details;
    String stage = "", study = "", jobid = "";
    ArrayList<Country> cities = new ArrayList<>();
    ArrayList<Country> stages = new ArrayList<>();
    ArrayList<Country> studies = new ArrayList<>();
    private DrawerLayout drawerLayout;
    CircleImageView img;
    TextViewWithFont name, email, login, reg;
    String rt = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendorder);

        startAdsContent();
        menu = (TextView) findViewById(R.id.menu);
        icon = (TextView) findViewById(R.id.icon);
        icon1 = (TextView) findViewById(R.id.icon1);
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        loc = (TextViewWithFont) findViewById(R.id.loc);
        txt = (TextViewWithFont) findViewById(R.id.txt);
        txt1 = (TextViewWithFont) findViewById(R.id.txt1);
        senorder = (TextViewWithFont) findViewById(R.id.sendorder);
        view = (TextViewWithFont) findViewById(R.id.view);
        lin = (LinearLayout) findViewById(R.id.lin);
        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin_services = (LinearLayout) findViewById(R.id.lin_services);
        linsearch = (LinearLayout) findViewById(R.id.linsearch);
        linsuccess = (LinearLayout) findViewById(R.id.linsuccess);
        linteacher = (LinearLayout) findViewById(R.id.linteacher);
        lintechnical = (LinearLayout) findViewById(R.id.lintechbical);
        spin = (Spinner) findViewById(R.id.spin);
        spin1 = (Spinner) findViewById(R.id.spin1);
        spintech = (Spinner) findViewById(R.id.spintech);
        details = (EditText) findViewById(R.id.details);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rt = "1";
                linteacher.setVisibility(View.VISIBLE);
                lintechnical.setVisibility(View.GONE);
                txt.setTextColor(Color.parseColor("#0070ba"));
                txt1.setTextColor(Color.parseColor("#99000000"));
                t1.setBackgroundColor(Color.parseColor("#0070ba"));
                t2.setBackgroundColor(Color.parseColor("#f2f2f2"));
                icon.setBackgroundResource(R.drawable.ic_teacher_2);
                icon1.setBackgroundResource(R.drawable.ic_worker_1);
            }
        });
        lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rt = "2";
                lintechnical.setVisibility(View.VISIBLE);
                linteacher.setVisibility(View.GONE);
                txt1.setTextColor(Color.parseColor("#0070ba"));
                txt.setTextColor(Color.parseColor("#99000000"));
                t2.setBackgroundColor(Color.parseColor("#0070ba"));
                t1.setBackgroundColor(Color.parseColor("#f2f2f2"));
                icon.setBackgroundResource(R.drawable.ic_teacher_1);
                icon1.setBackgroundResource(R.drawable.ic_worker_2);
            }
        });
        lin_services.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(), OtherServicesActivity.class);
            intent.putExtra("lat", "" + lat);
            intent.putExtra("lng", "" + lon);
            startActivity(intent);
        });
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (!gps_enabled && !network_enabled) {
            buildAlertMessageNoGps();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(interval)
                .setFastestInterval(fastestInterval)
                .setSmallestDisplacement(minDisplacement);

        // Check if has GPS by using Google play service
        buildLocationSettingsRequest();
        checkLocationSettings();
        if (isNetworkAvailable()) {
            getcity();
            getStages();
            spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    getStadies();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }
        img = (CircleImageView) findViewById(R.id.img);
        name = (TextViewWithFont) findViewById(R.id.name);
        final LinearLayout lin11 = (LinearLayout) findViewById(R.id.lin11);
        final LinearLayout lin2 = (LinearLayout) findViewById(R.id.lin2);
        final LinearLayout lin3 = (LinearLayout) findViewById(R.id.lin3);
        final LinearLayout lin4 = (LinearLayout) findViewById(R.id.lin4);
        final LinearLayout lin5 = (LinearLayout) findViewById(R.id.lin5);
        final LinearLayout lin6 = (LinearLayout) findViewById(R.id.lin6);
        name.setText(Gdata.user_fname + " " + Gdata.user_lname);

        if (Gdata.user_img != null && Gdata.user_img.equals("")) {
            Picasso.with(getApplicationContext()).load(Gdata.user_img).into(img, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }

            });
        }
        lin11.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), ClientordertabActivity.class);
            startActivity(i);
            drawerLayout.closeDrawers();
        });
        lin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ClientsettingActivity.class);
                startActivity(i);
                drawerLayout.closeDrawers();
            }
        });
        lin3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ClientcontactusActivity.class);
                startActivity(i);
                drawerLayout.closeDrawers();
            }
        });
        lin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ClientaboutappActivity.class);
                startActivity(i);
                drawerLayout.closeDrawers();
            }
        });
        lin5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ClientuseappActivity.class);
                startActivity(i);
                drawerLayout.closeDrawers();
            }
        });
        lin6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        senorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rt.equals("1")) {
                    if (stage.equals("") || study.equals("")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast t = null;
                                t = Toast.makeText(getApplicationContext(), getString(R.string.tr), Toast.LENGTH_LONG);
                                t.setGravity(Gravity.CENTER, 0, 0);
                                t.show();

                            }
                        });
                    } else {
                        if (isNetworkAvailable()) {
                            sendstages();
                        } else {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        }
                    }
                } else if (rt.equals("2")) {
                    if (jobid.equals("") || details.getText().toString().trim().equals("")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast t = null;
                                t = Toast.makeText(getApplicationContext(), getString(R.string.tr), Toast.LENGTH_LONG);
                                t.setGravity(Gravity.CENTER, 0, 0);
                                t.show();

                            }
                        });
                    } else {
                        if (isNetworkAvailable()) {
                            sendLognRequest();
                        } else {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        }
                    }
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ClientordertabActivity.class);
                startActivity(i);
                finish();
            }
        });
        Gdata.refreshedToken = FirebaseInstanceId.getInstance().getToken();
        try {
            if (!Gdata.refreshedToken.equals("") && !Gdata.refreshedToken.equals(null)) {
                sendLognReque();
            }
        } catch (NullPointerException e) {

        }
    }

    private void startAdsContent() {
        Intent intent = new Intent(this, ActivityAdsContent.class);
        startActivity(intent);
    }

    public void sendLognReque() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "edit_token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

//                    num.setText(obj.getString("notifications"));
//                    not_num.setText(obj.getString("notifications"));
//                    order_num.setText(obj.getString("current orders"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  progressDialog.dismiss();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", Gdata.user_id);
                params.put("token", Gdata.refreshedToken);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void sendLognRequest() {
        final ProgressDialog progressDialog = ProgressDialog.show(SendorderActivity.this, getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "send_request_worker", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");

                    if (status) {
                        linsuccess.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                linsuccess.setVisibility(View.GONE);
                                senorder.setVisibility(View.GONE);
                                linsearch.setVisibility(View.VISIBLE);
                            }
                        }, 4000);
                        Handler handle = new Handler();
                        handle.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getApplicationContext(), ClientordertabActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }, 24000);
                    } else {

                        String msg = obj.getString("msg");
                        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        Log.e("response ", msg + "");

                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", Gdata.user_id);
                params.put("job_id", jobid);
                params.put("details", details.getText().toString().trim());
                params.put("lat", lat);
                params.put("lng", lon);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void sendstages() {
        final ProgressDialog progressDialog = ProgressDialog.show(SendorderActivity.this, getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "send_request_teacher", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");

                    if (status) {
                        linsuccess.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                linsuccess.setVisibility(View.GONE);
                                senorder.setVisibility(View.GONE);
                                linsearch.setVisibility(View.VISIBLE);
                            }
                        }, 4000);
                        Handler handle = new Handler();
                        handle.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getApplicationContext(), ClientordertabActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }, 24000);
                    } else {

                        String msg = obj.getString("msg");
                        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        Log.e("response ", msg + "");

                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", Gdata.user_id);
                params.put("subject_id", study);
                params.put("lat", lat);
                params.put("lng", lon);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getcity() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "get_jobs", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");
                    cities.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("job_id");
                            countr.title = jo.getString("title");
                            cities.add(countr);
                        }
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.wreg3);
                        cities.add(countr);
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.wreg3);
                        cities.add(countr);
                        Country count = new Country();
                        count.id = "";
                        count.title = getString(R.string.wreg3);
                        cities.add(count);


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(SendorderActivity.this, android.R.layout.simple_spinner_dropdown_item, cities) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.spinner, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.image);
                            if (cities.size() > 0) {
                                try {
                                    txtTitle.setText(cities.get(position).title);
                                    jobid = cities.get(position).id;

                                } catch (IndexOutOfBoundsException e) {

                                }


                            }
                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.textspin, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            if (cities.size() > 0) {
                                txtTitle.setText(cities.get(position).title);
                            }
                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you dont display last item. It is used as hint.
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spintech.setAdapter(adapter);
                    spintech.setSelection(adapter.getCount());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, SendorderActivity.this);
            mGoogleApiClient.disconnect();
        }
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Get the map and register for the ready callback.
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.maps);
            mapFragment.getMapAsync(this);

            // mMap.setOnMapClickListener(this);

        }
    }


    private void handleNewLocation(Location location) {
//        Log.d(TAG, location.toString());

        double mCurrentLatitude = location.getLatitude();
        double mCurrentLongitude = location.getLongitude();
        loc.setText(Gdata.getAddress1(location.getLatitude(), location.getLongitude()));

        // assign the Latitude and Longitude
//        Geocoder gcd = new Geocoder(AddingaddressActivity.this, Locale.getDefault());
//        List<Address> addresses;
//        try {
//            addresses = gcd.getFromLocation(mCurrentLatitude, mCurrentLongitude, 1);
//            if (addresses.size() > 0)
//                Log.e(TAG, addresses.get(0).getAddressLine(0));
//            // print the full location in log
//            Log.e(TAG, "1: " + addresses.get(0).getAddressLine(1) + "2: " + addresses.get(0).getAddressLine(2) + "3: " + addresses.get(0).getAddressLine(3));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

// assign the Latitude and Longitude
        LatLng latLng = new LatLng(mCurrentLatitude, mCurrentLongitude);
        lat = Double.toString(mCurrentLatitude);
        lon = Double.toString(mCurrentLongitude);

// Make marker on the current location
        BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.mipmap.map);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(map_client)
                .title("my location");


        mMap.addMarker(options);
// move the camera zoom to the location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//        mMap.setOnMapClickListener(this);

    }

    //check the permission to access the location
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
//            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    // handel new location if the user change him location
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("please open gps provider")
                .setCancelable(false)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );

        result.setResultCallback(this);
    }


    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //unable to execute request
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are inadequate, and cannot be fixed here. Dialog not created
                break;
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        setUpMap();
    }

    public void setUpMap() {

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);


        // tarek edit
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();

                lat = Double.toString(latLng.latitude);
                lon = Double.toString(latLng.longitude);
                BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.mipmap.map);
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .icon(map_client);


                mMap.addMarker(options);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                loc.setText(Gdata.getAddress1(latLng.latitude, latLng.longitude));
            }
        });

    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            // Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        double mCurrentLatitude = latLng.latitude;
        double mCurrentLongitude = latLng.longitude;
        loc.setText(Gdata.getAddress1(latLng.latitude, latLng.longitude));

        // assign the Latitude and Longitude
//        Geocoder gcd = new Geocoder(AddingaddressActivity.this, Locale.getDefault());
//        List<Address> addresses;
//        try {
//            addresses = gcd.getFromLocation(mCurrentLatitude, mCurrentLongitude, 1);
//            if (addresses.size() > 0)
//                Log.e(TAG, addresses.get(0).getAddressLine(0));
//            // print the full location in log
//            Log.e(TAG, "1: " + addresses.get(0).getAddressLine(1) + "2: " + addresses.get(0).getAddressLine(2) + "3: " + addresses.get(0).getAddressLine(3));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

// assign the Latitude and Longitude
        latLng = new LatLng(mCurrentLatitude, mCurrentLongitude);
        lat = Double.toString(mCurrentLatitude);
        lon = Double.toString(mCurrentLongitude);

// Make marker on the current location
        BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.mipmap.map);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(map_client)
                .title("my location");


        mMap.addMarker(options);
// move the camera zoom to the location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }

    public void getStages() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "get_level", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");
                    stages.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("level_id");
                            countr.title = jo.getString("title");
                            stages.add(countr);
                        }
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder);
                        stages.add(countr);
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder);
                        stages.add(countr);
                        Country count = new Country();
                        count.id = "";
                        count.title = getString(R.string.sendorder);
                        stages.add(count);


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(SendorderActivity.this, android.R.layout.simple_spinner_dropdown_item, stages) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.spinner, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.image);
                            if (stages.size() > 0) {
                                try {
                                    txtTitle.setText(stages.get(position).title);
                                    stage = stages.get(position).id;

                                } catch (IndexOutOfBoundsException e) {

                                }


                            }
                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.textspin, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            if (stages.size() > 0) {
                                txtTitle.setText(stages.get(position).title);
                            }
                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you dont display last item. It is used as hint.
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(adapter);
                    spin.setSelection(adapter.getCount());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void getStadies() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "get_subject", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");
                    studies.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("subject_id");
                            countr.title = jo.getString("title");
                            studies.add(countr);
                        }
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder1);
                        studies.add(countr);
                        if (jsonArray.length() == 0) {
                            Country count = new Country();
                            count.id = "";
                            count.title = getString(R.string.sendorder1);
                            studies.add(count);
                        }
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder1);
                        studies.add(countr);
                        Country count = new Country();
                        count.id = "";
                        count.title = getString(R.string.sendorder1);
                        studies.add(count);


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(SendorderActivity.this, android.R.layout.simple_spinner_dropdown_item, studies) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.spinner, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.image);
                            if (studies.size() > 0) {
                                try {
                                    txtTitle.setText(studies.get(position).title);
                                    study = studies.get(position).id;

                                } catch (IndexOutOfBoundsException e) {

                                }


                            }
                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.textspin, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            if (studies.size() > 0) {
                                txtTitle.setText(studies.get(position).title);
                            }
                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you dont display last item. It is used as hint.
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin1.setAdapter(adapter);
                    spin1.setSelection(adapter.getCount());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("lang", Locale.getDefault().getLanguage());
                params.put("subject_id", stage);
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}
