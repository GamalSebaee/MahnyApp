package atiaf.mehany.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.Workerimg;
import atiaf.mehany.R;

public class WorkerloginActivity extends Activity {
    TextView back ;
    EditText username , password ;
    TextViewWithFont forget , regtxt  ;
    LinearLayout lin ;
    CardView login ;
    String na = "", pass = "", code = "";
    SharedPreferences storedata;
    private static String filename = "mlogin";
    Dialog layou;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workerlogin);
        back = (TextView) findViewById(R.id.back);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        forget = (TextViewWithFont) findViewById(R.id.forget);
        regtxt = (TextViewWithFont) findViewById(R.id.reg1);
        lin = (LinearLayout) findViewById(R.id.reg);
        login = (CardView) findViewById(R.id.login);
        if (Locale.getDefault().getLanguage().equals("en")){
            back.setRotation(180);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),WorkerregActivity.class);
                startActivity(i);
            }
        });
        regtxt.setPaintFlags(regtxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (Locale.getDefault().getLanguage().equals("ar")) {
            username.setGravity(Gravity.CENTER | Gravity.RIGHT);
            password.setGravity(Gravity.CENTER | Gravity.RIGHT);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                na = username.getText().toString().trim();
                pass = password.getText().toString();
                if (na.equals("") || pass.equals("")) {
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
                    }else {
                        Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                }
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layou = new Dialog(WorkerloginActivity.this);
                layou.requestWindowFeature(Window.FEATURE_NO_TITLE);
                layou.setContentView(R.layout.forget);
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int width = display.getWidth();
                layou.getWindow().setLayout((width * 19 / 20), LinearLayout.LayoutParams.WRAP_CONTENT);
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                final EditText email = (EditText) layou.findViewById(R.id.email);
                final TextViewWithFont yes = (TextViewWithFont) layou.findViewById(R.id.yes);
                final TextViewWithFont no = (TextViewWithFont) layou.findViewById(R.id.no);
                if (Locale.getDefault().getLanguage().equals("ar")) {
                    email.setGravity(Gravity.CENTER | Gravity.RIGHT);
                }
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        layou.dismiss();
                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Gdata.emailValidator(email.getText().toString()) == false || email.getText().toString().equals("") || email.getText().toString().replaceAll(" ", "").equals("")) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast t = null;
                                    t = Toast.makeText(getApplicationContext(), getString(R.string.trr), Toast.LENGTH_LONG);
                                    t.setGravity(Gravity.CENTER, 0, 0);
                                    t.show();

                                }
                            });
                        } else {
                            code = email.getText().toString();
                            if (isNetworkAvailable()) {
                                forgetpass();
                            }else {
                                Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                                t.setGravity(Gravity.CENTER, 0, 0);
                                t.show();
                            }
                        }
                    }
                });
                layou.show();
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void sendLognRequest() {
        final ProgressDialog progressDialog = ProgressDialog.show(WorkerloginActivity.this, getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_worker + "sign_in", new Response.Listener<String>() {
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
                        Gdata.pager = 1 ;
                        Gdata.worker_id = obj.getJSONObject("data").getString("user_id");
                        storedata = getSharedPreferences(filename, 0);
                        SharedPreferences.Editor edit = storedata.edit();
                        edit.putString("id", obj.getJSONObject("data").getString("user_id"));
                        edit.putString("fname", obj.getJSONObject("data").getString("first_name"));
                        edit.putString("lname", obj.getJSONObject("data").getString("last_name"));
                        edit.putString("phone", obj.getJSONObject("data").getString("phone"));
                        edit.putString("img", obj.getJSONObject("data").getString("img"));
                        edit.putString("email", obj.getJSONObject("data").getString("email"));
                        edit.putString("username", obj.getJSONObject("data").getString("username"));


//                        edit.putString("password", obj.getJSONObject("data").getString("password"));

                        edit.putString("type", "worker");
                        edit.commit();

                        Gdata.worker_fname = obj.getJSONObject("data").getString("first_name");
                        Gdata.worker_lname = obj.getJSONObject("data").getString("last_name");
//                                Gdata.user_username = obj.getJSONObject("user").getString("username");
                        Gdata.worker_email = obj.getJSONObject("data").getString("email");
                        Gdata.worker_phone = obj.getJSONObject("data").getString("phone");
                        Gdata.worker_img = obj.getJSONObject("data").getString("img");
                        Gdata.worker_username = obj.getJSONObject("data").getString("username");
                        Gdata.worker_status = obj.getJSONObject("data").getBoolean("availabl");
                        Gdata.worker_pass = obj.getJSONObject("data").getString("password");
                        Gdata.worker_opernum = obj.getJSONObject("data").getString("operation");
                        Gdata.worker_cid = obj.getJSONObject("data").getString("country_id");

//                        Gdata.worker_job_id = obj.getJSONObject("data").getString("job_id");
for (int i =0 ; i<obj.getJSONObject("data").getJSONArray("note").length();i++){
    JSONObject j = obj.getJSONObject("data").getJSONArray("note").getJSONObject(i);
    if (i == obj.getJSONObject("data").getJSONArray("note").length()-1){
        Gdata.worker_jobs = Gdata.worker_jobs + j.getString("title");
    }else {
        Gdata.worker_jobs = Gdata.worker_jobs + j.getString("title") + " - ";

    }
}
                        JSONArray jsonArray = obj.getJSONObject("data").getJSONArray("all_image");
                        Gdata.array.clear();
                        if (jsonArray.length()==0){
                            Workerimg workerimg = new Workerimg();
                            workerimg.img = "";
                            Gdata.array.add(workerimg);
                        }else {
                            for (int z = 0 ; z<jsonArray.length();z++){
                                JSONObject jo = jsonArray.getJSONObject(z);
                                Workerimg workerim = new Workerimg();
                                workerim.img = jo.getString("img");
                                workerim.id = jo.getString("image_id");
                                Gdata.array.add(workerim);
                                if (z==jsonArray.length()-1){
                                    Workerimg workerimg = new Workerimg();
                                    workerimg.img = "";
                                    workerimg.id = "";
                                    Gdata.array.add(workerimg);
                                }
                            }
                        }

                        Intent profileIntent = new Intent(WorkerloginActivity.this, WorkertabActivity.class);
                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profileIntent);
                        finish();

//                        userId=obj.getString("id");


                        // Session Manager

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

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("text", username.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
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
    public void forgetpass() {
        final ProgressDialog progressDialog = ProgressDialog.show(WorkerloginActivity.this, getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_worker + "forget_pass", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = (obj.getBoolean("Success"));


                    if (status) {
//                        userId=obj.getString("id");

                        String msg;
                        if (Locale.getDefault().getLanguage().equals("ar")) {
                            msg = "تم ارسال كلمه السر على الايميل";
                        } else {
                            msg = "Password has been sent to your email";
                        }
                        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        layou.dismiss();
                        // Session Manager

                    } else {

                        String msg = (obj.getString("msg"));

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
                        //  progressDialog.dismiss();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {


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
                params.put("email", code);
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
