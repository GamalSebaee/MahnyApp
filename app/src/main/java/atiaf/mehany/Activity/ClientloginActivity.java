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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.R;

public class ClientloginActivity extends Activity {
    TextView back ;
    EditText username , password ;
    TextViewWithFont forget , regtxt , delegatelogin ;
    LinearLayout lin ;
    CardView login ;
    String na = "", pass = "", code = "";
    SharedPreferences storedata;
    private static String filename = "mlogin";
    Dialog layou;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientlogin);
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
                Intent i = new Intent(getApplicationContext(),ClientregActivity.class);
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
                layou = new Dialog(ClientloginActivity.this);
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
        final ProgressDialog progressDialog = ProgressDialog.show(ClientloginActivity.this, getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "sign_in", new Response.Listener<String>() {
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

                        Gdata.user_id = obj.getJSONObject("data").getString("user_id");
                        Log.e("555",  Gdata.user_id+"" );
                        storedata = getSharedPreferences(filename, 0);
                        SharedPreferences.Editor edit = storedata.edit();
                        edit.putString("id", obj.getJSONObject("data").getString("user_id"));
                        edit.putString("fname", obj.getJSONObject("data").getString("first_name"));
                        edit.putString("lname", obj.getJSONObject("data").getString("last_name"));
                        edit.putString("phone", obj.getJSONObject("data").getString("phone"));
                        edit.putString("img", obj.getJSONObject("data").getString("img"));
                        edit.putString("email", obj.getJSONObject("data").getString("email"));
                        edit.putString("username", obj.getJSONObject("data").getString("username"));
                        edit.putString("country_id", obj.getJSONObject("data").getString("country_id"));
                        edit.putString("pass", obj.getJSONObject("data").getString("password"));

                        edit.putString("type", "client");
                        edit.commit();

                        Gdata.user_fname = obj.getJSONObject("data").getString("first_name");
                        Gdata.user_lname = obj.getJSONObject("data").getString("last_name");
//                                Gdata.user_username = obj.getJSONObject("user").getString("username");
                        Gdata.user_email = obj.getJSONObject("data").getString("email");
                        Gdata.user_phone = obj.getJSONObject("data").getString("phone");
                        Gdata.user_img = obj.getJSONObject("data").getString("img");
                        Gdata.user_username = obj.getJSONObject("data").getString("username");
                        Gdata.user_pass = obj.getJSONObject("data").getString("password");
                        Gdata.user_cid = obj.getJSONObject("data").getString("country_id");
                            Intent profileIntent = new Intent(ClientloginActivity.this, SendorderActivity.class);
                            profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(profileIntent);
                            finish();

//                        userId=obj.getString("id");


                        // Session Manager

                    } else {

                        String msg;
                        if (Locale.getDefault().getLanguage().equals("ar")) {
                            msg = "تأكد من ان البيانات صحيحة";
                        } else {
                            msg = "Make sure that data is correct";
                        }
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
        final ProgressDialog progressDialog = ProgressDialog.show(ClientloginActivity.this, getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "forget_pass", new Response.Listener<String>() {
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
