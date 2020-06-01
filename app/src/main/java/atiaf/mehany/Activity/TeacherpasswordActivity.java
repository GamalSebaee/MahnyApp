package atiaf.mehany.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

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

public class TeacherpasswordActivity extends Activity {
    TextViewWithFont title;
    LinearLayout lin;
    TextView back;
    EditText edit, cpass, ccpass;
    CardView login;
    String s = "";
    SharedPreferences storedata;
    private static String filename = "mlogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientpassword);
        title = (TextViewWithFont) findViewById(R.id.title);
        lin = (LinearLayout) findViewById(R.id.lin);
        back = (TextView) findViewById(R.id.back);
        edit = (EditText) findViewById(R.id.edit);
        cpass = (EditText) findViewById(R.id.cpass);
        ccpass = (EditText) findViewById(R.id.ccpass);
        login = (CardView) findViewById(R.id.login);
        if (Locale.getDefault().getLanguage().equals("ar")) {
            edit.setGravity(Gravity.CENTER | Gravity.RIGHT);
            cpass.setGravity(Gravity.CENTER | Gravity.RIGHT);
            ccpass.setGravity(Gravity.CENTER | Gravity.RIGHT);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = cpass.getText().toString().trim();
                if (!ccpass.getText().toString().trim().equals(cpass.getText().toString().trim())) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast t = null;
                            t = Toast.makeText(getApplicationContext(), getString(R.string.cpa), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();

                        }
                    });
                }else if (!edit.getText().toString().trim().equals(Gdata.teacher_pass)){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast t = null;
                            t = Toast.makeText(getApplicationContext(), getString(R.string.ccpa), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();

                        }
                    });
                } else{
                    updatepass();
                }
            }
        });
    }

    public void updatepass() {
        final ProgressDialog progressDialog = ProgressDialog.show(TeacherpasswordActivity.this, getString(R.string.wait), getString(R.string.che), true);
            Ion.with(TeacherpasswordActivity.this)
                    .load(Gdata.url_teacher + "edit_data")
                    .progressDialog(progressDialog).progress(new ProgressCallback() {
                @Override
                public void onProgress(long downloaded, long total) {
                }
            })
                    .setMultipartParameter("username", Gdata.teacher_username)
                    .setMultipartParameter("first_name", Gdata.teacher_fname)
                    .setMultipartParameter("last_name", Gdata.teacher_lname)
                    .setMultipartParameter("phone", Gdata.teacher_phone)
                    .setMultipartParameter("email", Gdata.teacher_email)
                    .setMultipartParameter("password", edit.getText().toString().trim())
                    .setMultipartParameter("user_id", Gdata.teacher_id)
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

                                        getdata();

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

    public void getdata() {
        final ProgressDialog progressDialog = ProgressDialog.show(TeacherpasswordActivity.this, getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "get_data", new Response.Listener<String>() {
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
                        Gdata.teacher_id = obj.getJSONObject("data").getString("user_id");
                        Gdata.teacher_fname = obj.getJSONObject("data").getString("first_name");
                        Gdata.teacher_lname = obj.getJSONObject("data").getString("last_name");
//                                Gdata.user_username = obj.getJSONObject("user").getString("username");
                        Gdata.teacher_email = obj.getJSONObject("data").getString("email");
                        Gdata.teacher_phone = obj.getJSONObject("data").getString("phone");
                        Gdata.teacher_img = obj.getJSONObject("data").getString("img");
                        Gdata.teacher_username = obj.getJSONObject("data").getString("username");
                        Gdata.teacher_status = obj.getJSONObject("data").getBoolean("availabl");
                        Gdata.teacher_pass = obj.getJSONObject("data").getString("password");
                        Gdata.teacher_opernum = obj.getJSONObject("data").getString("operation");
                        Gdata.teacher_cid = obj.getJSONObject("data").getString("country_id");

//                        Gdata.teacher_job_id = obj.getJSONObject("data").getString("job_id");
                        Gdata.teacher_stages = "";
                        Gdata.teacher_subjects = "";
//                        Gdata.teacher_job_id = obj.getJSONObject("data").getString("job_id");
                        for (int i =0 ; i<obj.getJSONObject("data").getJSONArray("note").length();i++){
                            JSONObject j = obj.getJSONObject("data").getJSONArray("note").getJSONObject(i);
                            if (i == obj.getJSONObject("data").getJSONArray("note").length()-1){
                                if (!Gdata.teacher_stages.contains(j.getString("lev_title"))) {
                                    Gdata.teacher_stages = Gdata.teacher_stages + j.getString("lev_title");
                                }
                                Gdata.teacher_subjects = Gdata.teacher_subjects + j.getString("sub_title") ;


                            }else {
                                if (!Gdata.teacher_stages.contains(j.getString("lev_title"))) {
                                    Gdata.teacher_stages = Gdata.teacher_stages + j.getString("lev_title") + " - ";
                                }
                                Gdata.teacher_subjects = Gdata.teacher_subjects + j.getString("sub_title")  + " - ";


                            }
                        }

                        if (Gdata.teacher_stages.substring(Gdata.teacher_stages.length()-2 , Gdata.teacher_stages.length()-1).equals("-")){
                            Gdata.teacher_stages = Gdata.teacher_stages.substring(0 , Gdata.teacher_stages.length()-2);
                        }
                        JSONArray jsonArray = obj.getJSONObject("data").getJSONArray("all_image");
                        Gdata.teacherarray.clear();
                        if (jsonArray.length()==0){
                            Workerimg workerimg = new Workerimg();
                            workerimg.img = "";
                            Gdata.teacherarray.add(workerimg);
                        }else {
                            for (int z = 0 ; z<jsonArray.length();z++){
                                JSONObject jo = jsonArray.getJSONObject(z);
                                Workerimg workerim = new Workerimg();
                                workerim.img = jo.getString("img");
                                Gdata.teacherarray.add(workerim);
                                if (z==jsonArray.length()-1){
                                    Workerimg workerimg = new Workerimg();
                                    workerimg.img = "";
                                    Gdata.teacherarray.add(workerimg);
                                }
                            }
                        }

                        Intent profileIntent = new Intent(TeacherpasswordActivity.this, TeachertabActivity.class);
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
                params.put("user_id", Gdata.teacher_id);
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
