package atiaf.mehany.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Adapter.Clientorderdapter;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.Workerimg;
import atiaf.mehany.Data.clientorder;
import atiaf.mehany.Fragment.ClientcurrentFragment;
import atiaf.mehany.R;

 public class Clientdetailsstep1Activity extends Activity {
    TextView back;
     TextViewWithFont ordernum;
     TextViewWithFont date;
     TextViewWithFont loc;
     TextViewWithFont ordertype;
     TextViewWithFont statge;
     TextViewWithFont subject;
     TextViewWithFont sertype;
     TextViewWithFont cancel;
     TextViewWithFont status , title;
     LinearLayout lin;
     LinearLayout lin1;
     LinearLayout lin2;
     RecyclerView list ;
     public static Clientorderdapter productadapter ;
     public static List<clientorder> appsdep = new ArrayList<>();
     public static String orderid = "" , type = "";
     Context context ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientdetailsstep1);
        context = Clientdetailsstep1Activity.this;
        back = (TextView) findViewById(R.id.back);
        ordernum = (TextViewWithFont) findViewById(R.id.ordernum);
        date = (TextViewWithFont) findViewById(R.id.date);
        loc = (TextViewWithFont) findViewById(R.id.loc);
        ordertype = (TextViewWithFont) findViewById(R.id.ordertype);
        statge = (TextViewWithFont) findViewById(R.id.stage);
        subject = (TextViewWithFont) findViewById(R.id.subject);
        sertype = (TextViewWithFont) findViewById(R.id.sertype);
        cancel = (TextViewWithFont) findViewById(R.id.cancel);
        status = (TextViewWithFont) findViewById(R.id.status);
        title = (TextViewWithFont) findViewById(R.id.title);
        lin = (LinearLayout) findViewById(R.id.lin);
        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);
        orderid = ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",56666)).ordernum;
        type = ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",56666)).type;
        if (Locale.getDefault().getLanguage().equals("en")) {
            back.setRotation(180);
        }
        if (ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",56666)).type.equals("0")){
            title.setText(getString(R.string.detailsorder2));
        }else {
            title.setText(getString(R.string.detailsorder22));
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {
                onBackPressed();
            }
        });
        ordernum.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",444445)).ordernum);
        date.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",444445)).date);
        loc.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",444445)).loc);
        statge.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",444445)).stage);
        subject.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",444445)).subject);
        sertype.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",444445)).sertype);
        status.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",444445)).statustitle);
        if (ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",444445)).type.equals("0")){
            lin.setVisibility(View.GONE);
            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);
            ordertype.setText(getString(R.string.main5));
        }else {
            lin.setVisibility(View.VISIBLE);
            lin1.setVisibility(View.VISIBLE);
            lin2.setVisibility(View.GONE);
            ordertype.setText(getString(R.string.main4));

        }
        list = (RecyclerView) findViewById(R.id.list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Clientdetailsstep1Activity.this);
        list.setLayoutManager(layoutManager);
        productadapter = new Clientorderdapter(appsdep);
        list.setAdapter(productadapter);
        if (isNetworkAvailable()) {
            loaddata();
        } else {
            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }

cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        cancel_order(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",55555)).ordernum , getIntent().getIntExtra("pos",55555));
    }
});
 }
     private boolean isNetworkAvailable() {
         ConnectivityManager connectivityManager
                 = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
         return activeNetworkInfo != null && activeNetworkInfo.isConnected();
     }
     public void loaddata(){
         final ProgressDialog progressDialog = ProgressDialog.show(Clientdetailsstep1Activity.this, getString(R.string.wait), getString(R.string.che), true);
         StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "get_all_action", new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {

                 //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                 Log.e("response ", response + "");


                 // Hide Progress Dialog
                 //progressDialog.dismiss();
                 try {
                     // JSON Object
                     JSONObject obj = new JSONObject(response);
                     JSONArray jsonArray = obj.getJSONArray("data");
                     appsdep.clear();
                     if (jsonArray.length()>0){
                         title.setVisibility(View.VISIBLE);
                     }
                     for (int i = 0 ; i <jsonArray.length() ; i++){
                         JSONObject jo = jsonArray.getJSONObject(i);
                         clientorder charge = new clientorder() ;
                         charge.id = jo.getString("user_id");
                         charge.name = jo.getString("first_name") + " " + jo.getString("last_name");
                         charge.dis = jo.getInt("disticat") + " " + getString(R.string.detailsorder4);
                         charge.rate = String.valueOf(jo.getInt("rate"));
                         charge.img = jo.getString("img");
                         JSONArray jsonArray1 = jo.getJSONArray("all_image");
                         for (int z = 0 ; z<jsonArray1.length();z++){
                             JSONObject joo = jsonArray1.getJSONObject(z);
                             Workerimg workerim = new Workerimg();
                             workerim.img = joo.getString("img");
                             charge.image.add(workerim);

                         }
                         charge.job = "" ;

//                        Gdata.worker_job_id = obj.getJSONObject("data").getString("job_id");
                         if (ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",5555)).type.equals("0")) {
                             for (int ii = 0; ii < jo.getJSONArray("note").length(); ii++) {
                                 JSONObject j = jo.getJSONArray("note").getJSONObject(ii);
                                 if (ii == jo.getJSONArray("note").length() - 1) {
                                     charge.job = charge.job + j.getString("title");
                                 } else {
                                     charge.job = charge.job + j.getString("title") + " - ";

                                 }
                             }
                         }else {
                             for (int ii = 0; ii < jo.getJSONArray("note").length(); ii++) {
                                 JSONObject j = jo.getJSONArray("note").getJSONObject(ii);
                                 if (ii == jo.getJSONArray("note").length() - 1) {
                                     charge.job = charge.job + getString(R.string.main4) + " " +  j.getString("sub_title")+ " " +  j.getString("lev_title");
                                 } else {
                                     charge.job = charge.job + getString(R.string.main4) + " " +  j.getString("sub_title")+ " " +  j.getString("lev_title") + " - ";

                                 }
                             }
                         }
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
                 params.put("order_id", ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",56666)).ordernum);
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
     public void cancel_order(final String id , final int pos){
         final ProgressDialog progressDialog = ProgressDialog.show(context, context.getString(R.string.wait), context.getString(R.string.che), true);

         StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url+"cancel_order", new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {

                 //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                 Log.e("response ", response + "");


                 // Hide Progress Dialog
                 //progressDialog.dismiss();
                 try {
                     // JSON Object
                     JSONObject obj = new JSONObject(response);
                     if (obj.getBoolean("Success")){
                         ClientcurrentFragment.appsdep.remove(pos);
                         ClientcurrentFragment.productadapter.notifyDataSetChanged();
                         onBackPressed();
                     }else {
                         String msg = obj.getString("msg");
                         Toast t = Toast.makeText(context, msg, Toast.LENGTH_LONG);
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
                             Toast t = Toast.makeText(context, context.getString(R.string.inte), Toast.LENGTH_LONG);
                             t.setGravity(Gravity.CENTER, 0, 0);
                             t.show();
                         } else if (error instanceof AuthFailureError) {
                             //TODO
                         } else if (error instanceof ServerError) {
                             Toast t = Toast.makeText(context, context.getString(R.string.inte), Toast.LENGTH_LONG);
                             t.setGravity(Gravity.CENTER, 0, 0);
                             t.show();
                             //TODO
                         } else if (error instanceof NetworkError) {
                             Toast t = Toast.makeText(context, context.getString(R.string.inte), Toast.LENGTH_LONG);
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
                 params.put("order_id",  id);
                 params.put("lang", Locale.getDefault().getLanguage());
                 Log.e("loginParams", params.toString());

                 return params;
             }
         };

         RequestQueue requestQueue = Volley.newRequestQueue(context);

         stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                 DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                 DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
         requestQueue.add(stringRequest);
     }

 }
