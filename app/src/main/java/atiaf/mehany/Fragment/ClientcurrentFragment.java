package atiaf.mehany.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Adapter.clientorderdapte;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.Order;
import atiaf.mehany.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientcurrentFragment extends Fragment {
    RecyclerView list ;
    public static clientorderdapte productadapter ;
    public static List<Order> appsdep = new ArrayList<>();
View v ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v==null){
            v = inflater.inflate(R.layout.fragment_clientcurrent, container, false);
            list = (RecyclerView) v.findViewById(R.id.list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            list.setLayoutManager(layoutManager);
            productadapter = new clientorderdapte(appsdep);
            list.setAdapter(productadapter);
            if (isNetworkAvailable()) {
                loaddata();
            } else {
                Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
            }

        }
        return v ;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void loaddata(){
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "get_all_call_currant", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("data");
                    appsdep.clear();
                    for (int i = jsonArray.length()-1 ; i >=0 ; i--){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        Order charge = new Order() ;
                        charge.ordernum = jo.getString("request_id");
                        if (jo.getString("status").equals("0")){
                            charge.worker_id = "";
                        }else {
                            charge.worker_id = jo.getString("worker_id");
                        }
                        charge.date = jo.getString("create_dateTime").substring(0,jo.getString("create_dateTime").indexOf(" "));
                        charge.loc = Gdata.getAddress1(jo.getDouble("lat"),jo.getDouble("lng"));
                        charge.type = jo.getString("type");
                        if (jo.getString("type").equals("0")){
                            charge.sertype = jo.getString("title_job");
                        }else {
                            charge.stage = jo.getString("level_title");
                            charge.subject = jo.getString("subject_title");
                        }
                        charge.status = jo.getString("status");
                        charge.statustitle = jo.getString("status_title");
                        appsdep.add(charge);
                    }
                    productadapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                progressDialog.dismiss();

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
                params.put("user_id", Gdata.user_id);
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
