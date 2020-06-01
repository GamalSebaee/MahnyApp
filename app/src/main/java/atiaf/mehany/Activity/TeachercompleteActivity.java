package atiaf.mehany.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Country;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.R;

public class TeachercompleteActivity extends Activity {
    public  static double lat1 ;
    public  static double long1 ;

//    public static int check = 0;
    TextView back;
    ImageView photo;
    Spinner spin, spin1;
    CardView reg;
    LinearLayout lin;
    TextViewWithFont name, log, num;
    String file = "";
    String stage = "", study = "", jobid = "";
    ArrayList<Country> cities = new ArrayList<>();
    ArrayList<Country> stages = new ArrayList<>();
    ArrayList<Country> studies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachercomplete);
        statusCheck();
        back = (TextView) findViewById(R.id.back);
        spin = (Spinner) findViewById(R.id.spin);
        spin1 = (Spinner) findViewById(R.id.spin1);
        photo = (ImageView) findViewById(R.id.photo);
        reg = (CardView) findViewById(R.id.login);
        lin = (LinearLayout) findViewById(R.id.linvis);
        name = (TextViewWithFont) findViewById(R.id.name);
        log = (TextViewWithFont) findViewById(R.id.log);
        num = (TextViewWithFont) findViewById(R.id.num);
        if (Locale.getDefault().getLanguage().equals("en")) {
            back.setRotation(180);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        log.setPaintFlags(p.getColor());
        log.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        name.setText(TeacherregActivity.fna + " " + TeacherregActivity.lna);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), techerlogin.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });
        if (isNetworkAvailable()) {
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
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (study.equals("")) {
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
        });
        getnum();
    }

    public void sendLognRequest() {
        final ProgressDialog progressDialog = ProgressDialog.show(TeachercompleteActivity.this, getString(R.string.wait), getString(R.string.che), true);
        if (file.equals("")) {
            Ion.with(TeachercompleteActivity.this)
                    .load(Gdata.url_teacher + "sign_up")
                    .progressDialog(progressDialog).progress(new ProgressCallback() {
                @Override
                public void onProgress(long downloaded, long total) {
                }
            })
                    .setMultipartParameter("username", TeacherregActivity.use)
                    .setMultipartParameter("first_name", TeacherregActivity.fna)
                    .setMultipartParameter("last_name", TeacherregActivity.lna)
                    .setMultipartParameter("phone", TeacherregActivity.ph)
                    .setMultipartParameter("email", TeacherregActivity.em)
                    .setMultipartParameter("password", TeacherregActivity.pass)
                    .setMultipartParameter("country_id", TeacherregActivity.country_id)
                    .setMultipartParameter("subject_id", study)
                    .setMultipartParameter("lang", Locale.getDefault().getLanguage())
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, final JsonObject result) {

                            Log.e("555_result", result.toString());
                            if (result == null) {

                            } else {
                                try {
                                    // JSON Object
                                    JSONObject obj = new JSONObject(result.toString());

                                    boolean status = obj.getBoolean("Success");
                                    Gdata.TarekId = obj.getJSONObject("data").getString("user_id");
                                    if (status) {


//                                        try {
//                                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                                        if (ActivityCompat.checkSelfPermission(TeachercompleteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TeachercompleteActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                                            // TODO: Consider calling
//                                            //    ActivityCompat#requestPermissions
//                                            // here to request the missing permissions, and then overriding
//                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                            //                                          int[] grantResults)
//                                            // to handle the case where the user grants the permission. See the documentation
//                                            // for ActivityCompat#requestPermissions for more details.
//                                            return;
//                                        }
//                                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                                        double longitude = location.getLongitude();
//                                        double latitude = location.getLatitude();
//                                            Log.e("555_long", latitude +"" );
//                                            Log.e("555_latitude", longitude+"" );
//                                        }catch (NullPointerException a) {a.printStackTrace();}

                                            GPSTracker tracker = new GPSTracker(TeachercompleteActivity.this);
                                            if (!tracker.canGetLocation()) {
                                                tracker.showSettingsAlert();
                                            } else {
                                               lat1 = tracker.getLatitude();
                                                long1  = tracker.getLongitude();

                                                Log.e("555_long1", lat1 +"--" );
                                                Log.e("555_latitude1", long1+"--" );
                                            }
// connect new webservice



//
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher+"edit_lat_lng", new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                     Log.e("555_resppppp",response);

                                                    //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                                    //   Log.e("response ", response + "");


                                                }
                                            },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                                                                Log.e("555", error.getMessage() + "");

                                                            } else if (error instanceof AuthFailureError) {
                                                                //TODO
                                                            } else if (error instanceof ServerError) {
                                                                //TODO
                                                            } else if (error instanceof NetworkError) {
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
                                                    // params.put("login", loginJsonObject.toString());
                                                    params.put("user_id",Gdata.TarekId);
                                                    params.put("lat", String.valueOf(lat1) );
                                                    params.put("lng",String.valueOf(long1));
                                                    params.put("lang",Locale.getDefault().getLanguage());
                                                    Log.e("55_check",params.toString());

                                                    return params;
                                                }
                                            };

                                            RequestQueue requestQueue = Volley.newRequestQueue(TeachercompleteActivity.this);

                                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                            requestQueue.add(stringRequest);






//                                        startService(new Intent(getApplicationContext(), SendLocationService.class));
                                        lin.setVisibility(View.VISIBLE);
                                        reg.setEnabled(false);

                                    } else {
                                        String msg = obj.getString("msg");
                                        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();
                                        Log.e("response ", msg + "");
                                    }
                                    progressDialog.dismiss();
                                } catch (JSONException ee) {
                                    // TODO Auto-generated catch block
                                    // _errorMsg.setText(e.getMessage());
                                    ee.printStackTrace();
                                }
                            }
                        }
                    });
        } else if (!file.equals("")){
            Ion.with(TeachercompleteActivity.this)
                    .load(Gdata.url_teacher + "sign_up")
                    .progressDialog(progressDialog).progress(new ProgressCallback() {
                @Override
                public void onProgress(long downloaded, long total) {
                }
            })
                    .setMultipartParameter("username", TeacherregActivity.use)
                    .setMultipartParameter("first_name", TeacherregActivity.fna)
                    .setMultipartParameter("last_name", TeacherregActivity.lna)
                    .setMultipartParameter("phone", TeacherregActivity.ph)
                    .setMultipartParameter("email", TeacherregActivity.em)
                    .setMultipartParameter("password", TeacherregActivity.pass)
                    .setMultipartParameter("country_id", TeacherregActivity.country_id)
                    .setMultipartParameter("subject_id", study)
                    .setMultipartParameter("lang", Locale.getDefault().getLanguage())
                    .setMultipartFile("img", new File(file))
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, final JsonObject result) {
                            if (result == null) {
                            } else {
                                try {
                                    // JSON Object
                                    JSONObject obj = new JSONObject(result.toString());
                                    boolean status = obj.getBoolean("Success");
                                    if (status) {
                                        lin.setVisibility(View.VISIBLE);
                                        reg.setEnabled(false);
                                    } else {

                                        String msg = obj.getString("msg");

                                        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();
                                        Log.e("response ", msg + "");

                                    }
                                    progressDialog.dismiss();
                                } catch (JSONException ee) {
                                    // TODO Auto-generated catch block
                                    // _errorMsg.setText(e.getMessage());

                                    ee.printStackTrace();

                                }
                            }
                        }
                    });
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void getStages() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "get_level", new Response.Listener<String>() {
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
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(TeachercompleteActivity.this, android.R.layout.simple_spinner_dropdown_item, stages) {

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

                })

        {

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
    public void getnum() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "count_order_teacher", new Response.Listener<String>() {
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
                        num.setText(obj.getJSONObject("data").getString("count_order_teacher"));
                    }

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

                })

        {

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "get_subject", new Response.Listener<String>() {
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
                        if (jsonArray.length()==0){
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
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(TeachercompleteActivity.this, android.R.layout.simple_spinner_dropdown_item, studies) {

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

                })

        {

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photo.setImageBitmap(bitmap);
                //Setting the Bitmap to ImageView
//
            } catch (IOException e) {
                e.printStackTrace();
            }
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            getSystemService(WINDOW_SERVICE);
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            final int width = options.outWidth;
            final int height = options.outHeight;
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            file = cursor.getString(columnIndex);
            cursor.close();
        }
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("please enable gps")
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }
}
