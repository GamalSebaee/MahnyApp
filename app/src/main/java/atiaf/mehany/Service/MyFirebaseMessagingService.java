package atiaf.mehany.Service;

/**
 * Created by PC on 04/10/2016.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
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
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import atiaf.mehany.Activity.ClientordertabActivity;
import atiaf.mehany.Activity.SplashActivity;
import atiaf.mehany.Activity.TeachertabActivity;
import atiaf.mehany.Activity.WorkertabActivity;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.Workerimg;
import atiaf.mehany.Model;
import atiaf.mehany.R;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String operationId;
    String messageBody, title;
    Intent intent;
    double latitude, longitude;
    JSONObject obj;
    int x = 0;
    private static String filename = "mlogin";
    String id, type, body;
    SharedPreferences storedata;
    Model   success ;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("sdds", remoteMessage.getData().toString());



        try {
            Type fooType = new TypeToken<Model>() {
            }.getType();
            success = new Gson().fromJson(remoteMessage.getData().toString(), fooType);
            Log.e("55555", success.casee +"" );


            if (success.casee.equals("25")) {
                Log.e("555555555", "55555555k" );
//                    Gdata.pager = 2;
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Intent notificationIntent = new Intent(getApplicationContext(), SplashActivity.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder = null;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logotesttttttt);
                notificationBuilder = new NotificationCompat.Builder(getApplicationContext()) ;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.logotesttttttt).build();
                    notificationBuilder.setLargeIcon(bitmap).build();
                    notificationBuilder.setColor(Color.parseColor("#0070ba"));
                } else {
                    notificationBuilder .setSmallIcon(R.mipmap.ic_launcher).build();
//            builder.setLargeIcon(bitmap2);
                }

                notificationBuilder
                        .setContentTitle(success.title)
                        .setContentText(success.body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(contentIntent).build();

                Random random = new Random();
                int notificationID = random.nextInt(9999 - 1000) + 1000;
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notificationID, notificationBuilder.build());

            }










        }
        catch (JsonSyntaxException a){a.printStackTrace();}
        catch (NullPointerException a){a.printStackTrace();}

        getLang();
        storedata = getSharedPreferences(filename, 0);
        id = storedata.getString("id", "vgcvc");
        type = storedata.getString("type", "vgcvc");
        JSONObject jo = new JSONObject(remoteMessage.getData());

        try {
            if (Locale.getDefault().getLanguage().equals("ar")) {
                body = jo.getString("body_ar");
            } else {
                body = jo.getString("body_en");
            }


// tarek edit



            if (jo.getString("type").equals("Worker")) {
                sendLognRequest();
                if (jo.getString("case").equals("0")) {
                    Gdata.pager = 2;




                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Intent notificationIntent = new Intent(getApplicationContext(), WorkertabActivity.class);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder notificationBuilder = null;
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logotesttttttt);
                    notificationBuilder = new NotificationCompat.Builder(getApplicationContext()) ;
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        notificationBuilder.setSmallIcon(R.drawable.logotesttttttt).build();
                        notificationBuilder.setLargeIcon(bitmap).build();
                        notificationBuilder.setColor(Color.parseColor("#0070ba"));
                    } else {
                        notificationBuilder .setSmallIcon(R.mipmap.ic_launcher).build();
//            builder.setLargeIcon(bitmap2);
                    }




                    notificationBuilder
                            .setContentTitle(jo.getString("title"))
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(contentIntent).build();

                    Random random = new Random();
                    int notificationID = random.nextInt(9999 - 1000) + 1000;
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(notificationID, notificationBuilder.build());
                }
            } else if (jo.getString("type").equals("Teacher")) {
                sendLognReques();
                if (jo.getString("case").equals("0")) {
                    Gdata.pager = 2;
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Intent notificationIntent = new Intent(getApplicationContext(), TeachertabActivity.class);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);


                    NotificationCompat.Builder notificationBuilder = null;



                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logotesttttttt);
                    notificationBuilder = new NotificationCompat.Builder(getApplicationContext()) ;
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        notificationBuilder.setSmallIcon(R.drawable.logotesttttttt).build();
                        notificationBuilder.setLargeIcon(bitmap).build();
                        notificationBuilder.setColor(Color.parseColor("#0070ba"));
                    } else {
                        notificationBuilder .setSmallIcon(R.mipmap.ic_launcher).build();
//            builder.setLargeIcon(bitmap2);
                    }


                    notificationBuilder
                            .setContentTitle(jo.getString("title"))
                            .setContentText(body)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(contentIntent);

                    Random random = new Random();
                    int notificationID = random.nextInt(9999 - 1000) + 1000;
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(notificationID, notificationBuilder.build());
                }
            } else {
                Gdata.user_id = storedata.getString("id", "vgcvc");
                Gdata.user_username = storedata.getString("username", "vgcvc");
                Gdata.user_img = storedata.getString("img", "vgcvc");
                Gdata.user_phone = storedata.getString("phone", "vgcvc");
                Gdata.user_email = storedata.getString("email", "vgcvc");
                Gdata.user_fname = storedata.getString("fname", "vgcvc");
                Gdata.user_lname = storedata.getString("lname", "vgcvc");
                Gdata.user_pass = storedata.getString("pass", "vgcvc");
                Gdata.user_cid = storedata.getString("country_id", "vgcvc");
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Intent notificationIntent = new Intent(getApplicationContext(), ClientordertabActivity.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder = null;


                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.logotesttttttt);
                notificationBuilder = new NotificationCompat.Builder(getApplicationContext()) ;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setSmallIcon(R.drawable.logotesttttttt).build();
                    notificationBuilder.setLargeIcon(bitmap).build();
                    notificationBuilder.setColor(Color.parseColor("#0070ba"));
                } else {
                    notificationBuilder .setSmallIcon(R.mipmap.ic_launcher).build();
//            builder.setLargeIcon(bitmap2);
                }




                notificationBuilder
                        .setContentTitle(jo.getString("title"))
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(contentIntent);

                Random random = new Random();
                int notificationID = random.nextInt(9999 - 1000) + 1000;
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(notificationID, notificationBuilder.build());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if (type.equals("client")) {
//            Gdata.user_id = storedata.getString("id", "vgcvc");
//            Gdata.user_username = storedata.getString("username", "vgcvc");
//            Gdata.user_img = storedata.getString("img", "vgcvc");
//            Gdata.user_phone = storedata.getString("phone", "vgcvc");
//            Gdata.user_email = storedata.getString("email", "vgcvc");
//            Gdata.user_name = storedata.getString("name", "vgcvc");
//            Gdata.user_packid = storedata.getString("area_id", "vgcvc");
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Intent notificationIntent = new Intent(getApplicationContext(), OrderclienttabActivity.class);
//            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            NotificationCompat.Builder notificationBuilder = null;
////            notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
////                    .setSmallIcon(R.mipmap.ic_launcher)
////                    .setContentTitle(getString(R.string.app_name))
////                    .setContentText(getString(R.string.order1)
////                    .setAutoCancel(true)
////                    .setSound(defaultSoundUri)
////                    .setContentIntent(contentIntent);
//
//            notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText(getString(R.string.order1))
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(contentIntent);
//
//            Random random = new Random();
//            int notificationID = random.nextInt(9999 - 1000) + 1000;
//            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(notificationID, notificationBuilder.build());
//        } else {
//            Gdata.delegate_id = storedata.getString("id", "vgcvc");
//            Gdata.delegate_username = storedata.getString("username", "vgcvc");
//            Gdata.delegate_img = storedata.getString("img", "vgcvc");
//            Gdata.delegate_phone = storedata.getString("phone", "vgcvc");
//            Gdata.delegate_email = storedata.getString("email", "vgcvc");
//            Gdata.delegate_name = storedata.getString("name", "vgcvc");
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Intent notificationIntent = new Intent(getApplicationContext(), DelegatenotActivity.class);
//            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            NotificationCompat.Builder notificationBuilder = null;
////            notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
////                    .setSmallIcon(R.mipmap.ic_launcher)
////                    .setContentTitle(getString(R.string.app_name))
////                    .setContentText(getString(R.string.order1)
////                    .setAutoCancel(true)
////                    .setSound(defaultSoundUri)
////                    .setContentIntent(contentIntent);
//
//            notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(getString(R.string.app_name))
//                    .setContentText(getString(R.string.order1))
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(contentIntent);
//
//            Random random = new Random();
//            int notificationID = random.nextInt(9999 - 1000) + 1000;
//            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(notificationID, notificationBuilder.build());
//        }
        Log.e("rr", remoteMessage.getData().toString());

    }

    void getLang() { // 7otaha abl l intent

        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String lang = prefs.getString("lang", Locale.getDefault().getLanguage());

//            if(lang==null){
//                Intent i = new Intent(Splash.this, Language_Chooser.class);
//                startActivity(i);
//                finish();
//            }

//            else {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getApplicationContext().getResources().updateConfiguration(config, null);


//            }
        } catch (NullPointerException a) {
            a.printStackTrace();
        } catch (RuntimeException a) {
            a.printStackTrace();
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Gdata.refreshedToken= s;
        Log.e("eee",Gdata.refreshedToken);
        System.out.println(Gdata.refreshedToken);
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
                            workerimg.id = "";
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

                })

        {

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
                                Gdata.teacher_subjects = Gdata.teacher_subjects + j.getString("sub_title") ;


                            } else {
                                if (!Gdata.teacher_stages.contains(j.getString("lev_title"))) {
                                    Gdata.teacher_stages = Gdata.teacher_stages + j.getString("lev_title") + " - ";
                                }
                                Gdata.teacher_subjects = Gdata.teacher_subjects + j.getString("sub_title")  + " - ";


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
                            workerimg.id = "";
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

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", id);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}