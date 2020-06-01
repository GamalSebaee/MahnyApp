package atiaf.mehany.Fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

import atiaf.mehany.Activity.PerviousorderActivity;
import atiaf.mehany.Activity.ProviderseettingActivity;
import atiaf.mehany.Activity.WorkeraboutappActivity;
import atiaf.mehany.Activity.WorkercontactusActivity;
import atiaf.mehany.Activity.WorkertabActivity;
import atiaf.mehany.Activity.WorkeruseappActivity;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.Workerimg;
import atiaf.mehany.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
View v ;
    LinearLayout lin1 , lin2 , lin3 , lin4  , lin5 , lin6 , lin7 , lin8 , lin9 ;
    TextViewWithFont status , num , lang ;
    SwitchCompat toggle ;
    String lan = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v==null){
            v = inflater.inflate(R.layout.fragment_setting, container, false);
             lin1 = (LinearLayout) v.findViewById(R.id.lin1);
            lin2 = (LinearLayout) v.findViewById(R.id.lin2);
            lin3 = (LinearLayout) v.findViewById(R.id.lin3);
            lin4 = (LinearLayout) v.findViewById(R.id.lin4);
            lin5 = (LinearLayout) v.findViewById(R.id.lin5);
            lin6 = (LinearLayout) v.findViewById(R.id.lin6);
            lin7 = (LinearLayout) v.findViewById(R.id.lin7);
            lin8 = (LinearLayout) v.findViewById(R.id.lin8);
            lin9 = (LinearLayout) v.findViewById(R.id.lin9);
            status = (TextViewWithFont) v.findViewById(R.id.status);
            num = (TextViewWithFont) v.findViewById(R.id.num);
            lang = (TextViewWithFont) v.findViewById(R.id.lang);
            toggle = (SwitchCompat) v.findViewById(R.id.toggle);
if (Gdata.worker_status){
    toggle.setChecked(true);
    status.setText(getContext().getString(R.string.more4));
}else {
    toggle.setChecked(false);
    status.setText(getContext().getString(R.string.more5));
}
            if (Locale.getDefault().getLanguage().equals("ar")){
                lang.setText(getContext().getString(R.string.more7));
            }else {
                lang.setText(getContext().getString(R.string.more8));
            }
            lin1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent i = new Intent(getContext(), ClientsettingActivity.class);
//                    startActivity(i);
                }
            });
            lin2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), ProviderseettingActivity.class);
                    startActivity(i);
                }
            });
            lin4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openLanguageDialog();
                }
            });
            lin5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), PerviousorderActivity.class);
                    startActivity(i);
                }
            });
            lin6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), WorkercontactusActivity.class);
                    startActivity(i);
                }
            });
            lin7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), WorkeraboutappActivity.class);
                    startActivity(i);
                }
            });
            lin8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(), WorkeruseappActivity.class);
                    startActivity(i);
                }
            });
            lin9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent i = new Intent(getContext(), ProviderseettingActivity.class);
//                    startActivity(i);
                }
            });
            num.setText(Gdata.worker_opernum);

        }
        return v;
    }
    void openLanguageDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lang_dialog);
        dialog.setCancelable(true);
        CardView okBtn = (CardView) dialog.findViewById(R.id.ok_button);
        CardView cancel = (CardView) dialog.findViewById(R.id.cancel_button);

        final RadioButton arabic = (RadioButton) dialog.findViewById(R.id.radio_arabic);
        RadioButton english = (RadioButton) dialog.findViewById(R.id.radio_english);


        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lan = "ar";
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lan = "en";
            }
        });

        if (Locale.getDefault().getLanguage().equals("en")) {
            arabic.setGravity(Gravity.LEFT | Gravity.CENTER);
        } else if (Locale.getDefault().getLanguage().equals("ar")) {

        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lang.equals("")) {
                    Toast.makeText(getContext(), R.string.plz_select_lang, Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    Locale locale = new Locale(lan);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;

                    getContext().getResources().updateConfiguration(config, null);

                    prefs.edit().putString("lang", lan).commit();
                    dialog.dismiss();
                    sendLognRequest();

                }
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
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
//                                Gdata.user_username = obj.getJSONObject("user").getString("username");
                        Gdata.worker_email = obj.getJSONObject("data").getString("email");
                        Gdata.worker_phone = obj.getJSONObject("data").getString("phone");
                        Gdata.worker_img = obj.getJSONObject("data").getString("img");
                        Gdata.worker_username = obj.getJSONObject("data").getString("username");
                        Gdata.worker_status = obj.getJSONObject("data").getBoolean("availabl");
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
                                Gdata.array.add(workerim);
                                if (z==jsonArray.length()-1){
                                    Workerimg workerimg = new Workerimg();
                                    workerimg.img = "";
                                    Gdata.array.add(workerimg);
                                }
                            }
                        }
                        Gdata.worker_jobs = "";
//                        Gdata.worker_job_id = obj.getJSONObject("data").getString("job_id");
                        for (int i =0 ; i<obj.getJSONObject("data").getJSONArray("note").length();i++){
                            JSONObject j = obj.getJSONObject("data").getJSONArray("note").getJSONObject(i);
                            if (i == obj.getJSONObject("data").getJSONArray("note").length()-1){
                                Gdata.worker_jobs = Gdata.worker_jobs + j.getString("title");
                            }else {
                                Gdata.worker_jobs = Gdata.worker_jobs + j.getString("title") + " - ";

                            }
                        }

                        Intent profileIntent = new Intent(getContext(), WorkertabActivity.class);
                        profileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(profileIntent);
                        ((Activity)getContext()).finish();
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {

                        String msg = obj.getString("msg");

                        Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
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
                params.put("user_id", Gdata.worker_id);
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
}
