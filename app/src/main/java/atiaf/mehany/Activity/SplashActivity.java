package atiaf.mehany.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
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

import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.Workerimg;
import atiaf.mehany.R;

public class SplashActivity extends Activity {
    private static String filename = "mlogin";
    String id, type;
    SharedPreferences storedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        storedata = getSharedPreferences(filename, 0);
        id = storedata.getString("id", "vgcvc");
        type = storedata.getString("type", "vgcvc");
        getLang();
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (id.equals("vgcvc")) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();


                    } else {
                        if (type.equals("client")) {
                            Gdata.user_id = storedata.getString("id", "vgcvc");
                            Gdata.user_username = storedata.getString("username", "vgcvc");
                            Gdata.user_img = storedata.getString("img", "vgcvc");
                            Gdata.user_phone = storedata.getString("phone", "vgcvc");
                            Gdata.user_email = storedata.getString("email", "vgcvc");
                            Gdata.user_fname = storedata.getString("fname", "vgcvc");
                            Gdata.user_lname = storedata.getString("lname", "vgcvc");
                            Gdata.user_pass = storedata.getString("pass", "vgcvc");
                            Gdata.user_cid = storedata.getString("country_id", "vgcvc");
                            Intent i = new Intent(getApplicationContext(), SendorderActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } else if (type.equals("worker")) {
                            sendLognRequest();
                        } else if (type.equals("teacher")) {
                            sendLognReques();
                        }
                    }
                }
            }
        }.start();
    }

    void getLang() { // 7otaha abl l intent

        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
            String lang = prefs.getString("lang", Locale.getDefault().getLanguage());
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getApplicationContext().getResources().updateConfiguration(config, null);
        } catch (NullPointerException a) {
            a.printStackTrace();
        } catch (RuntimeException a) {
            a.printStackTrace();
        }

    }

    public void sendLognRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_worker + "get_data", new Response.Listener<String>() {
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
                        Gdata.worker_pass = obj.getJSONObject("data").getString("password");
                        Gdata.worker_email = obj.getJSONObject("data").getString("email");
                        Gdata.worker_phone = obj.getJSONObject("data").getString("phone");
                        Gdata.worker_img = obj.getJSONObject("data").getString("img");
                        Gdata.worker_username = obj.getJSONObject("data").getString("username");
                        Gdata.worker_status = obj.getJSONObject("data").getBoolean("availabl");
                        Gdata.worker_opernum = obj.getJSONObject("data").getString("operation");
                        Gdata.worker_cid = obj.getJSONObject("data").getString("country_id");
                        JSONArray jsonArray = obj.getJSONObject("data").getJSONArray("all_image");
                        Gdata.array.clear();
                        if (jsonArray.length() == 0) {
                            Workerimg workerimg = new Workerimg();
                            workerimg.img = "";
                            Gdata.array.add(workerimg);
                        } else {
                            for (int z = 0; z < jsonArray.length(); z++) {
                                JSONObject jo = jsonArray.getJSONObject(z);
                                Workerimg workerim = new Workerimg();
                                workerim.img = jo.getString("img");
                                workerim.id = jo.getString("image_id");
                                Gdata.array.add(workerim);
                                if (z == jsonArray.length() - 1) {
                                    Workerimg workerimg = new Workerimg();
                                    workerimg.img = "";
                                    workerimg.id = "";
                                    Gdata.array.add(workerimg);
                                }
                            }
                        }
                        Gdata.worker_jobs = "";

//                        Gdata.worker_job_id = obj.getJSONObject("data").getString("job_id");
                        for (int i = 0; i < obj.getJSONObject("data").getJSONArray("note").length(); i++) {
                            JSONObject j = obj.getJSONObject("data").getJSONArray("note").getJSONObject(i);
                            if (i == obj.getJSONObject("data").getJSONArray("note").length() - 1) {
                                Gdata.worker_jobs = Gdata.worker_jobs + j.getString("title");
                            } else {
                                Gdata.worker_jobs = Gdata.worker_jobs + j.getString("title") + " - ";

                            }
                        }
                        Gdata.pager = 1;
                        Intent profileIntent = new Intent(SplashActivity.this, WorkertabActivity.class);
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
                params.put("user_id", id);
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

    public void sendLognReques() {
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
                        Gdata.pager = 1;
                        Gdata.teacher_id = obj.getJSONObject("data").getString("user_id");
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

                        edit.putString("type", "teacher");
                        edit.commit();

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
                        for (int i = 0; i < obj.getJSONObject("data").getJSONArray("note").length(); i++) {
                            JSONObject j = obj.getJSONObject("data").getJSONArray("note").getJSONObject(i);
                            if (i == obj.getJSONObject("data").getJSONArray("note").length() - 1) {
                                if (!Gdata.teacher_stages.contains(j.getString("lev_title"))) {
                                    Gdata.teacher_stages = Gdata.teacher_stages + j.getString("lev_title");
                                }
                                Gdata.teacher_subjects = Gdata.teacher_subjects + j.getString("sub_title");


                            } else {
                                if (!Gdata.teacher_stages.contains(j.getString("lev_title"))) {
                                    Gdata.teacher_stages = Gdata.teacher_stages + j.getString("lev_title") + " - ";
                                }
                                Gdata.teacher_subjects = Gdata.teacher_subjects + j.getString("sub_title") + " - ";


                            }
                        }

                        if (Gdata.teacher_stages.substring(Gdata.teacher_stages.length() - 2, Gdata.teacher_stages.length() - 1).equals("-")) {
                            Gdata.teacher_stages = Gdata.teacher_stages.substring(0, Gdata.teacher_stages.length() - 2);
                        }
                        JSONArray jsonArray = obj.getJSONObject("data").getJSONArray("all_image");
                        Gdata.teacherarray.clear();
                        if (jsonArray.length() == 0) {
                            Workerimg workerimg = new Workerimg();
                            workerimg.img = "";
                            Gdata.teacherarray.add(workerimg);
                        } else {
                            for (int z = 0; z < jsonArray.length(); z++) {
                                JSONObject jo = jsonArray.getJSONObject(z);
                                Workerimg workerim = new Workerimg();
                                workerim.img = jo.getString("img");
                                workerim.id = jo.getString("image_id");
                                Gdata.teacherarray.add(workerim);
                                if (z == jsonArray.length() - 1) {
                                    Workerimg workerimg = new Workerimg();
                                    workerimg.img = "";
                                    workerimg.id = "";
                                    Gdata.teacherarray.add(workerimg);
                                }
                            }
                        }

                        Intent profileIntent = new Intent(SplashActivity.this, TeachertabActivity.class);
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
                params.put("user_id", id);
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
