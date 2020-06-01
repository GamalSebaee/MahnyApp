package atiaf.mehany.Activity.phase2.login;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Activity.ClientregActivity;
import atiaf.mehany.Activity.SendorderActivity;
import atiaf.mehany.Activity.TeachertabActivity;
import atiaf.mehany.Activity.WorkertabActivity;
import atiaf.mehany.Activity.phase2.BaseActivity;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.R;
import atiaf.mehany.phase2.body.UserLoginBody;
import atiaf.mehany.phase2.remote_data.ApiCallBack;
import atiaf.mehany.phase2.response.LoginBody;
import atiaf.mehany.phase2.response.LoginResponse;

public class OtherloginActivity extends BaseActivity {
    TextView back;
    EditText username, password;
    TextViewWithFont forget, regtxt, delegatelogin;
    LinearLayout lin;
    CardView login;
    String na = "", pass = "", code = "";
    SharedPreferences storedata;
    Dialog layou;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    @Override
    public void initViews() {
        back = findViewById(R.id.back);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forget = findViewById(R.id.forget);
        regtxt = findViewById(R.id.reg1);
        lin = findViewById(R.id.reg);
        login = findViewById(R.id.login);
        if (Locale.getDefault().getLanguage().equals("en")) {
            back.setRotation(180);
        }
        back.setOnClickListener(v -> onBackPressed());
        lin.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), ClientregActivity.class);
            startActivity(i);
        });
        regtxt.setPaintFlags(regtxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (Locale.getDefault().getLanguage().equals("ar")) {
            username.setGravity(Gravity.CENTER | Gravity.RIGHT);
            password.setGravity(Gravity.CENTER | Gravity.RIGHT);
        }
        login.setOnClickListener(view -> {
            na = username.getText().toString().trim();
            pass = password.getText().toString();
            if (na.equals("") || pass.equals("")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    Toast t = null;
                    t = Toast.makeText(getApplicationContext(), getString(R.string.tr), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();

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
        });
        forget.setOnClickListener(view -> {
            layou = new Dialog(OtherloginActivity.this);
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
            no.setOnClickListener(view12 -> layou.dismiss());
            yes.setOnClickListener(view1 -> {
                if (!Gdata.emailValidator(email.getText().toString()) || email.getText().toString().equals("") || email.getText().toString().replaceAll(" ", "").equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> {
                        Toast t = null;
                        t = Toast.makeText(getApplicationContext(), getString(R.string.trr), Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();

                    });
                } else {
                    code = email.getText().toString();
                    if (isNetworkAvailable()) {
                        forgetpass();
                    } else {
                        Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                }
            });
            layou.show();
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void sendLognRequest() {
        progressDialog.show();
        UserLoginBody userLoginBody = new UserLoginBody();
        userLoginBody.setLoginText(username.getText().toString().trim());
        userLoginBody.setLoginPassword(password.getText().toString().trim());

        apiManager.loginToApp(userLoginBody, new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.dismiss();
                LoginResponse loginResponse = (LoginResponse) data;
                setUserData(loginResponse);
            }

            @Override
            public void ResponseFail(Object data) {
                progressDialog.dismiss();
                Toast.makeText(OtherloginActivity.this, "" + data, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUserData(LoginResponse loginResponse) {
        if (loginResponse.success) {


            Gdata.user_id = loginResponse.getLoginBody().getUserId();
            String filename = "mlogin";
            storedata = getSharedPreferences(filename, 0);
            SharedPreferences.Editor edit = storedata.edit();
            edit.putString("id", loginResponse.getLoginBody().getUserId());
            edit.putString("fname", loginResponse.getLoginBody().getFirstName());
            edit.putString("lname", loginResponse.getLoginBody().getLastName());
            edit.putString("phone", loginResponse.getLoginBody().getPhone());
            edit.putString("img", loginResponse.getLoginBody().getImg());
            edit.putString("email", loginResponse.getLoginBody().getEmail());
            edit.putString("username", loginResponse.getLoginBody().getUsername());
            edit.putString("country_id", loginResponse.getLoginBody().getCountryId());
            edit.putString("pass", loginResponse.getLoginBody().getPassword());
            String userType = loginResponse.getLoginBody().getType();

            if (userType != null && userType.equals("2")) {
                edit.putString("type", "teacher");
            } else if (userType != null && userType.equals("3")) {
                edit.putString("type", "worker");
            } else {
                edit.putString("type", "client");
            }


            edit.commit();

            Gdata.user_fname = loginResponse.getLoginBody().getFirstName();
            Gdata.user_lname = loginResponse.getLoginBody().getLastName();
//                                Gdata.user_username = obj.getJSONObject("user").getString("username");
            Gdata.user_email = loginResponse.getLoginBody().getEmail();
            Gdata.user_phone = loginResponse.getLoginBody().getPhone();
            Gdata.user_img = loginResponse.getLoginBody().getImg();
            Gdata.user_username = loginResponse.getLoginBody().getUsername();
            Gdata.user_pass = loginResponse.getLoginBody().getPassword();
            Gdata.user_cid = loginResponse.getLoginBody().getCountryId();
            Intent profileIntent;
            if (userType != null && userType.equals("2")) {
                profileIntent = new Intent(OtherloginActivity.this, TeachertabActivity.class);
            } else if (userType != null && userType.equals("3")) {
                profileIntent = new Intent(OtherloginActivity.this, WorkertabActivity.class);
            } else {
                profileIntent = new Intent(OtherloginActivity.this, SendorderActivity.class);
            }

            profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(profileIntent);
            finish();


        } else {
            Toast t = Toast.makeText(getApplicationContext(), loginResponse.getMsg(), Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();

        }
    }

    public void forgetpass() {
        final ProgressDialog progressDialog = ProgressDialog.show(OtherloginActivity.this, getString(R.string.wait), getString(R.string.che), true);
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

                }) {

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
