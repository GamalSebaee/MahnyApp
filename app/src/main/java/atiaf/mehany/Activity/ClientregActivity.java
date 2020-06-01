package atiaf.mehany.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Country;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.R;

public class ClientregActivity extends Activity {
    TextView back ;
    EditText fname , lname , username , email , phone  , password;
    Spinner mant , hay ;
    CardView reg ;
    Spinner spin ;
    ArrayList<Country> countries = new ArrayList<>();
    String fna = "", lna = "" ,em = "", ph = "", pass = "", cityid = "" , ha = "" , repeat = "" , use = "" , file = "" , country_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientreg);
        back = (TextView) findViewById(R.id.back);
        fname = (EditText) findViewById(R.id.first);
        lname = (EditText) findViewById(R.id.last);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        reg = (CardView) findViewById(R.id.login);
        spin = (Spinner) findViewById(R.id.spin);
        if (Locale.getDefault().getLanguage().equals("en")) {
            back.setRotation(180);
        }
        if (Locale.getDefault().getLanguage().equals("ar")){
            fname.setGravity(Gravity.CENTER|Gravity.RIGHT);
            username.setGravity(Gravity.CENTER|Gravity.RIGHT);
            email.setGravity(Gravity.CENTER|Gravity.RIGHT);
            phone.setGravity(Gravity.CENTER|Gravity.RIGHT);
            password.setGravity(Gravity.CENTER|Gravity.RIGHT);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fna = fname.getText().toString().trim();
                lna = lname.getText().toString().trim();
                use = username.getText().toString().trim();
                em = email.getText().toString().trim();
                ph = phone.getText().toString().trim();
                pass = password.getText().toString().trim();
                if (fna.equals("") || lna.equals("") || use.equals("") || em.equals("") || ph.equals("") || pass.equals("")) {
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
                } else if (Gdata.emailValidator(email.getText().toString()) == false || email.getText().toString().equals("") || email.getText().toString().replaceAll(" ", "").equals("")) {
                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.trr), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }else {
                    if (isNetworkAvailable()) {
                        sendLognRequest();
                    }else {
                        Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                }
            }
        });

        if (isNetworkAvailable()) {
            getcountry();
        }else {
            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }
    }





    public void sendLognRequest() {
        final ProgressDialog progressDialog = ProgressDialog.show(ClientregActivity.this, getString(R.string.wait), getString(R.string.che), true);
        if (file.equals("")) {
            Ion.with(ClientregActivity.this)
                    .load(Gdata.url + "sign_up")
                    .progressDialog(progressDialog).progress(new ProgressCallback() {
                @Override
                public void onProgress(long downloaded, long total) {
                }
            })
                    .setMultipartParameter("username", use)
                    .setMultipartParameter("first_name", fna)
                    .setMultipartParameter("last_name", lna)
                    .setMultipartParameter("phone", ph)
                    .setMultipartParameter("email", em)
                    .setMultipartParameter("password", pass)
                    .setMultipartParameter("country_id", country_id)
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

                                    if (status) {

                                        Intent profileIntent = new Intent(ClientregActivity.this, ClientloginActivity.class);
                                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(profileIntent);
                                        finish();

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

    public void getcountry() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "get_country", new Response.Listener<String>() {
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
                    countries.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("counery_id");
                            countr.title = jo.getString("postal_code");
                            countr.img = jo.getString("img");
                            countries.add(countr);
                        }
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(ClientregActivity.this, android.R.layout.simple_spinner_dropdown_item, countries) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.country, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.code);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.img);
                            if (countries.size() > 0) {
                                try {
                                    txtTitle.setText(countries.get(position).title);
                                    country_id = countries.get(position).id;
                                    if (countries.get(position).img.equals("")) {

                                    } else {
                                        Picasso.with(getApplicationContext()).load(countries.get(position).img).into(img, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {

                                            }

                                        });
                                    }
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
                            if (countries.size() > 0) {
                                txtTitle.setText(countries.get(position).title);
                            }
                            return convertView;
                        }


                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(adapter);
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

}
