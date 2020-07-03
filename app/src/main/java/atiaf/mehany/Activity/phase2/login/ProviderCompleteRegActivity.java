package atiaf.mehany.Activity.phase2.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Activity.GPSTracker;
import atiaf.mehany.Activity.phase2.BaseActivity;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Country;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.GeneralModel;
import atiaf.mehany.R;
import atiaf.mehany.phase2.remote_data.ApiCallBack;
import atiaf.mehany.phase2.response.TypesResponse;

import static atiaf.mehany.Activity.TeachercompleteActivity.lat1;
import static atiaf.mehany.Activity.TeachercompleteActivity.long1;

public class ProviderCompleteRegActivity extends BaseActivity {


    TextView back;
    ImageView photo;
    Spinner spin;
    CardView reg;
    LinearLayout lin;
    TextViewWithFont name, log, num;
    String jobid = "", file = "";
    ArrayList<Country> cities = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_register_complete);
        initViews();
    }

    @Override
    public void initViews() {
        back = findViewById(R.id.back);
        spin = findViewById(R.id.spin);
        photo = findViewById(R.id.photo);
        reg = findViewById(R.id.login);
        lin = findViewById(R.id.linvis);
        name = findViewById(R.id.name);
        log = findViewById(R.id.log);
        num = findViewById(R.id.num);
        if (Locale.getDefault().getLanguage().equals("en")) {
            back.setRotation(180);
        }
        back.setOnClickListener(view -> onBackPressed());
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        log.setPaintFlags(p.getColor());
        log.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        name.setText(ProviderRegActivity.fna + " " + ProviderRegActivity.lna);
        log.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), OtherloginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
        photo.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);
        });
        if (isNetworkAvailable()) {
            getCity();

        } else {
            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }
        reg.setOnClickListener(v -> {
            if (jobid.equals("")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    Toast t = null;
                    t = Toast.makeText(getApplicationContext(), getString(R.string.tr), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();

                });
            } else {
                if (isNetworkAvailable()) {
                    //  sendLognRequest();
                    confirmRegister();
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            }
        });
        getnum();
    }

    private void confirmRegister() {
        HashMap<String, String> allFormData = new HashMap<>();
        allFormData.put("username", ProviderRegActivity.use);
        allFormData.put("first_name", ProviderRegActivity.fna);
        allFormData.put("last_name", ProviderRegActivity.lna);
        allFormData.put("phone", ProviderRegActivity.ph);
        allFormData.put("email", ProviderRegActivity.em);
        allFormData.put("country_id", ProviderRegActivity.country_id);
        allFormData.put("password", ProviderRegActivity.pass);
        allFormData.put("job_id", jobid);
        allFormData.put("type", jobid);
        allFormData.put("lang", Locale.getDefault().getLanguage());

        progressDialog.show();
        apiManager.createNewAccount(allFormData, new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.hide();
                GeneralModel generalModel= (GeneralModel) data;
                Toast.makeText(ProviderCompleteRegActivity.this, ""+generalModel.getMsg(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), OtherloginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
             //   finish();
            }

            @Override
            public void ResponseFail(Object data) {
                progressDialog.hide();
                Toast.makeText(ProviderCompleteRegActivity.this, ""+data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getnum() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_worker + "count_order_worker", new Response.Listener<String>() {
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
                        num.setText(obj.getJSONObject("data").getString("count_order_worker"));
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

    public void sendLognRequest() {
        final ProgressDialog progressDialog = ProgressDialog.show(ProviderCompleteRegActivity.this, getString(R.string.wait), getString(R.string.che), true);
        if (file.equals("")) {
            Ion.with(ProviderCompleteRegActivity.this)
                    .load(Gdata.url_worker + "sign_up")
                    .progressDialog(progressDialog).progress(new ProgressCallback() {
                @Override
                public void onProgress(long downloaded, long total) {
                }
            })
                    .setMultipartParameter("username", ProviderRegActivity.use)
                    .setMultipartParameter("first_name", ProviderRegActivity.fna)
                    .setMultipartParameter("last_name", ProviderRegActivity.lna)
                    .setMultipartParameter("phone", ProviderRegActivity.ph)
                    .setMultipartParameter("email", ProviderRegActivity.em)
                    .setMultipartParameter("country_id", ProviderRegActivity.country_id)
                    .setMultipartParameter("password", ProviderRegActivity.pass)
                    .setMultipartParameter("job_id", jobid)
                    .setMultipartParameter("type", jobid)
                    .setMultipartParameter("lang", Locale.getDefault().getLanguage())
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
                                    Gdata.TarekId = obj.getJSONObject("data").getString("user_id");
                                    if (status) {

                                        GPSTracker tracker = new GPSTracker(ProviderCompleteRegActivity.this);
                                        if (!tracker.canGetLocation()) {
                                            tracker.showSettingsAlert();
                                        } else {
                                            lat1 = tracker.getLatitude();
                                            long1 = tracker.getLongitude();

                                            Log.e("555_long1", lat1 + "--");
                                            Log.e("555_latitude1", long1 + "--");
                                        }
// connect new webservice


//
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "edit_lat_lng", new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.e("555_resppppp", response);

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

                                                }) {

                                            @Override
                                            protected Map<String, String> getParams() {

                                                Map<String, String> params = new HashMap<>();
                                                // params.put("login", loginJsonObject.toString());
                                                params.put("user_id", Gdata.TarekId);
                                                params.put("lat", String.valueOf(lat1));
                                                params.put("lng", String.valueOf(long1));
                                                params.put("lang", Locale.getDefault().getLanguage());
                                                Log.e("55_check", params.toString());

                                                return params;
                                            }
                                        };

                                        RequestQueue requestQueue = Volley.newRequestQueue(ProviderCompleteRegActivity.this);

                                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                        requestQueue.add(stringRequest);


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
        } else if (!file.equals("")) {
            Ion.with(ProviderCompleteRegActivity.this)
                    .load(Gdata.url_worker + "sign_up")
                    .progressDialog(progressDialog).progress(new ProgressCallback() {
                @Override
                public void onProgress(long downloaded, long total) {
                }
            })
                    .setMultipartParameter("username", ProviderRegActivity.use)
                    .setMultipartParameter("first_name", ProviderRegActivity.fna)
                    .setMultipartParameter("last_name", ProviderRegActivity.lna)
                    .setMultipartParameter("phone", ProviderRegActivity.ph)
                    .setMultipartParameter("email", ProviderRegActivity.em)
                    .setMultipartParameter("password", ProviderRegActivity.pass)
                    .setMultipartParameter("job_id", jobid)
                    .setMultipartParameter("country_id", ProviderRegActivity.country_id)
                    .setMultipartParameter("lang", Locale.getDefault().getLanguage())
                    .setMultipartFile("img", new File(file))
                    .asJsonObject()
                    .setCallback((e, result) -> {
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
                    });
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getCity() {
        progressDialog.show();
        apiManager.getAllTypes(new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.hide();
                TypesResponse typesResponse = (TypesResponse) data;
                cities.clear();
                if (typesResponse.getAllTypes() != null && typesResponse.getAllTypes().size() > 0) {

                    for (int i = 0; i < typesResponse.getAllTypes().size(); i++) {
                        Country countr = new Country();
                        countr.id = typesResponse.getAllTypes().get(i).getId();
                        countr.title = typesResponse.getAllTypes().get(i).getName();
                        cities.add(countr);
                    }
                    Country countr = new Country();
                    countr.id = "";
                    countr.title = getString(R.string.wreg3);
                    cities.add(countr);
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
                setSpinnerData();
            }

            @Override
            public void ResponseFail(Object data) {
                progressDialog.hide();
                Toast.makeText(ProviderCompleteRegActivity.this, "" + data, Toast.LENGTH_SHORT).show();
            }
        });

        //  progressDialog.dismiss();

    }

    private void setSpinnerData() {
        ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(ProviderCompleteRegActivity.this,
                android.R.layout.simple_spinner_dropdown_item, cities) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.spinner, null, false);
                TextViewWithFont txtTitle = convertView.findViewById(R.id.title);
                txtTitle.setTextColor(Color.BLACK);
                final ImageView img = convertView.findViewById(R.id.image);
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
                txtTitle.setTextColor(Color.BLACK);
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
        spin.setAdapter(adapter);
        spin.setSelection(adapter.getCount());
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
}
