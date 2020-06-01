package atiaf.mehany.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Customecalss.parser;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherorderFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback,
        ResultCallback<LocationSettingsResult> {
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
    LatLng latLng , latLn;
    LatLng clientLatLng;
    GoogleMap mMap;
    View v ;
    LinearLayout lin , lincan ;
    MapView maps ;
    TextViewWithFont name , loc1 , sertype1 , details1 ;
    CircleImageView img ;
    FrameLayout call , msg  ;
    LinearLayout map1 ;
    CardView cancel , arriave , end ;
    String lat = "" , lon = "" , ph = "" , id = "" , client_id = "" , worker_lat = "" , worker_lon = "";
    SupportMapFragment mMapFragment ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v==null){
            v = inflater.inflate(R.layout.fragment_order, container, false);
            lin = (LinearLayout) v.findViewById(R.id.lin);
            lincan = (LinearLayout) v.findViewById(R.id.lincan);
            maps = (MapView) v.findViewById(R.id.maps);
            name = (TextViewWithFont) v.findViewById(R.id.name);
            loc1 = (TextViewWithFont) v.findViewById(R.id.loc1);
            sertype1 = (TextViewWithFont) v.findViewById(R.id.sertype1);
            details1 = (TextViewWithFont) v.findViewById(R.id.details1);
            img = (CircleImageView) v.findViewById(R.id.img);
            call = (FrameLayout) v.findViewById(R.id.call);
            msg = (FrameLayout) v.findViewById(R.id.msg);
            map1 = (LinearLayout) v.findViewById(R.id.map);
            call = (FrameLayout) v.findViewById(R.id.call);
            cancel = (CardView) v.findViewById(R.id.cancel);
            arriave = (CardView) v.findViewById(R.id.arriave);
            end = (CardView) v.findViewById(R.id.end);
            setUpMapIfNeeded();
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
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
                loaddata();
            } else {
                Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
            }
        }
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + ph));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "sms:" + ph));
                startActivity(intent);
            }
        });
        map1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String format = "geo:0,0?q=" + worker_lat + "," + worker_lon + "atiaf apps";
                Uri uri = Uri.parse(format);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                   makeaction("cancel_order");
                } else {
                    Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            }
        });
        arriave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeaction("home_arrive");

            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeaction("end_order");
            }
        });
        return v ;

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Get the map and register for the ready callback.
            mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.workerMap);
            mMapFragment.getMapAsync(this);

            // mMap.setOnMapClickListener(this);

        }
    }


    private void handleNewLocation(Location location) {
//        Log.d(TAG, location.toString());

        double mCurrentLatitude = location.getLatitude();
        double mCurrentLongitude = location.getLongitude();
lat = String.valueOf(mCurrentLatitude);
        lon = String.valueOf(mCurrentLongitude);

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


// Make marker on the current location
//        BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.mipmap.map);
//        MarkerOptions options = new MarkerOptions()
//                .position(latLng)
//                .icon(map_client)
//                .title("my location");
//
//
//        mMap.addMarker(options);
//// move the camera zoom to the location
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        mMap.addMarker(new MarkerOptions().position(latLng).title(
                "FROM LOCATION").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map)).draggable(true));
        CameraPosition cameraPositio = new CameraPosition.Builder()
                .target(latLng).zoom(10).build();
        mMap.moveCamera(CameraUpdateFactory
                .newCameraPosition(cameraPositio));

    }

    //check the permission to access the location
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                connectionResult.startResolutionForResult((Activity) getContext(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
     * Uses a {@link LocationSettingsRequest.Builder} to build
     * a {@link LocationSettingsRequest} that is used for checking
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

                    status.startResolutionForResult((Activity) getContext(), REQUEST_CHECK_SETTINGS);

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
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }
    public void loaddata(){
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "get_all_call_currant", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("data");
                    if (jsonArray.length()==0){
                        lin.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0 ; i <jsonArray.length() ; i++){
                        lin.setVisibility(View.VISIBLE);
                        JSONObject jo = jsonArray.getJSONObject(i);
//                        Not charge = new Not() ;
//                        charge.id = jo.getString("order_id");
//                        charge.loc = Gdata.getAddress1(jo.getDouble("lat"),jo.getDouble("lng"));
//                        charge.lat = jo.getDouble("lat") ;
//                        charge.lon = jo.getDouble("lng") ;
//                        charge.clientid = jo.getString("id_client") ;
//                        charge.km = jo.getInt("disticat") + " " + getString(R.string.detailsorder4);
//                        charge.sertype = jo.getString("title_job");
//                        charge.details = jo.getString("order_details");
//                        charge.clientname = jo.getString("firstname_client") + " " + jo.getString("lastname_client");
//                        charge.details = jo.getString("order_details");
//                        charge.clientimg = jo.getString("img_client");
//                        charge.status = jo.getString("status");
                        loc1.setText(Gdata.getAddress1(jo.getDouble("lat") , jo.getDouble("lng")));
                        name.setText(jo.getString("firstname_client") + " " + jo.getString("lastname_client"));
                        details1.setText(jo.getString("subject_title"));
                        sertype1.setText(jo.getString("level_title"));
                        ph = jo.getString("phone_client");
                        worker_lat = jo.getString("lat");
                        worker_lon = jo.getString("lng");
                        id = jo.getString("order_id");
                        client_id = jo.getString("id_client");
                        if (jo.getString("status").equals("2")){
                            end.setVisibility(View.GONE);
                            lincan.setVisibility(View.VISIBLE);
                        }else if (jo.getString("status").equals("3")){
                            lincan.setVisibility(View.GONE);
                            end.setVisibility(View.VISIBLE);
                            mMapFragment.getView().setVisibility(View.GONE);
                            call.setVisibility(View.GONE);
                            map1.setVisibility(View.GONE);
                            msg.setVisibility(View.GONE);
                        }
                         latLn = new LatLng(jo.getDouble("lat"), jo.getDouble("lng"));


// Make marker on the current location
                        Picasso.with(getContext()).load(jo.getString("img_client")).into(img, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }

                        });
                        mMap.addMarker(new MarkerOptions().position(latLn).title(
                                "FROM LOCATION").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map2)).draggable(true));
                        CameraPosition cameraPositio = new CameraPosition.Builder()
                                .target(latLn).zoom(10).build();
                        mMap.moveCamera(CameraUpdateFactory
                                .newCameraPosition(cameraPositio));
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String url = getDirectionsUrl(latLng, latLn);
                                    DownloadTask downloadTask = new DownloadTask();
                                    downloadTask.execute(url);
                                }catch (NullPointerException e){

                                }

                            }
                        },5000);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("worker_id", Gdata.teacher_id);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    public void makeaction(String url ){
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), getString(R.string.wait), getString(R.string.che), true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("Success")){
                        if (isNetworkAvailable()) {
                            loaddata();
                        } else {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        }
                    }else {
                        String msg = obj.getString("msg");
                        Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
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
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", client_id);
                params.put("teacher_id", Gdata.teacher_id);
                params.put("order_id",  id);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            final JSONObject json;
            try {
                json = new JSONObject(result);
                JSONArray routeArray = json.getJSONArray("routes");
                JSONObject routes = routeArray.getJSONObject(0);

                JSONArray newTempARr = routes.getJSONArray("legs");
                JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

                JSONObject distOb = newDisTimeOb.getJSONObject("distance");
                JSONObject timeOb = newDisTimeOb.getJSONObject("duration");
                Log.i("Diatance :", distOb.getString("text"));
                Log.i("Time :", timeOb.getString("text"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ParserTask3 parserTask = new ParserTask3();
            parserTask.execute(result);
        }
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
    private class ParserTask3 extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                parser parser = new parser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            Log.e("results", result + "");
            if (result.size() < 1) {
                Toast.makeText(getContext(), "No Points", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                Log.e("points", path + "");
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if (j == 0) {

                    } else if (j == 1) {
                    } else if (j > 1) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                }
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.parseColor("#313d4d"));
            }
            mMap.addPolyline(lineOptions);
        }
    }

    //check the permission to access the location
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false&language=" + Locale.getDefault().getLanguage();
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;
        return url;
    }
}
