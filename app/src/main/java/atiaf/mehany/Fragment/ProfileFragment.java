package atiaf.mehany.Fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Activity.MainActivity;
import atiaf.mehany.Activity.WorkereditActivity;
import atiaf.mehany.Adapter.Photodapter;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
View v ;
    FrameLayout edit , logout ;
    CircleImageView img ;
    TextViewWithFont name , job ;
    public static RecyclerView list ;
    SharedPreferences storedata;
    private static String filename = "mlogin";
    public static Photodapter productadapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v==null){
            v = inflater.inflate(R.layout.fragment_profile, container, false);
            edit = (FrameLayout) v.findViewById(R.id.edit);
            logout = (FrameLayout) v.findViewById(R.id.logout);
            img = (CircleImageView) v.findViewById(R.id.img);
            name = (TextViewWithFont) v.findViewById(R.id.name);
            job = (TextViewWithFont) v.findViewById(R.id.job);
            list = (RecyclerView) v.findViewById(R.id.list);
            name.setText(Gdata.worker_fname + " " + Gdata.worker_lname);
            job.setText(Gdata.worker_jobs);
            Picasso.with(getContext()).load(Gdata.worker_img).into(img, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }

            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNetworkAvailable()) {
                        sendLognRequest();
                    }else {
                        Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(),WorkereditActivity.class);
                    startActivity(i);
                }
            });
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            list.setLayoutManager(layoutManager);
            productadapter = new Photodapter(Gdata.array);
            list.setAdapter(productadapter);
            productadapter.notifyItemChanged(Gdata.array.size()-1);
            list.smoothScrollToPosition(Gdata.array.size()-1);
            list.setScrollY(Gdata.array.size()-1 );
        }
        return v;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void sendLognRequest() {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_worker + "sign_out", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");

                    if (status) {
                        storedata = getContext().getSharedPreferences(filename, 0);
                        SharedPreferences.Editor edit = storedata.edit();
                        storedata.edit().clear().commit();
                        Intent i = new Intent(getContext(),MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        ((Activity)getContext()).finish();                        // Session Manager

                    } else {

                        String msg;
                        if (Locale.getDefault().getLanguage().equals("ar")) {
                            msg = "تأكد من ان البيانات صحيحة";
                        } else {
                            msg = "Make sure that data is correct";
                        }
                        Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
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
