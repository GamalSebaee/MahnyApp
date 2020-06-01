package atiaf.mehany.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Adapter.Photodapter;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClientworkerprofileActivity extends Activity {
TextView t1 , t2 , t3 , t4 ,t5 ;
    TextViewWithFont name  , job;
    CardView accept ;
    RecyclerView list ;
    public static Photodapter productadapter ;
CircleImageView img ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientworkerprofile);
        img = (CircleImageView) findViewById(R.id.img);
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);
        t5 = (TextView) findViewById(R.id.t5);
        name = (TextViewWithFont) findViewById(R.id.name);
        job = (TextViewWithFont) findViewById(R.id.job);
accept = (CardView) findViewById(R.id.accept);
        list = (RecyclerView) findViewById(R.id.list);
        name.setText(Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",88777)).name);
        job.setText(Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",88777)).job);
        if (Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",88777)).rate.equals("5")){
           t1.setBackgroundResource(R.drawable.ic_star);
           t2.setBackgroundResource(R.drawable.ic_star);
           t3.setBackgroundResource(R.drawable.ic_star);
           t4.setBackgroundResource(R.drawable.ic_star);
           t5.setBackgroundResource(R.drawable.ic_star);
        }else if (Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",88777)).rate.equals("4")){
           t1.setBackgroundResource(R.drawable.ic_star);
           t2.setBackgroundResource(R.drawable.ic_star);
           t3.setBackgroundResource(R.drawable.ic_star);
           t4.setBackgroundResource(R.drawable.ic_star);
           t5.setBackgroundResource(R.drawable.ic_star_2);
        }else if (Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",88777)).rate.equals("3")){
           t1.setBackgroundResource(R.drawable.ic_star);
           t2.setBackgroundResource(R.drawable.ic_star);
           t3.setBackgroundResource(R.drawable.ic_star);
           t4.setBackgroundResource(R.drawable.ic_star_2);
           t5.setBackgroundResource(R.drawable.ic_star_2);
        }else if (Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",88777)).rate.equals("2")){
           t1.setBackgroundResource(R.drawable.ic_star);
           t2.setBackgroundResource(R.drawable.ic_star);
           t3.setBackgroundResource(R.drawable.ic_star_2);
           t4.setBackgroundResource(R.drawable.ic_star_2);
           t5.setBackgroundResource(R.drawable.ic_star_2);
        }else if (Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",88777)).rate.equals("1")){
           t1.setBackgroundResource(R.drawable.ic_star);
           t2.setBackgroundResource(R.drawable.ic_star_2);
           t3.setBackgroundResource(R.drawable.ic_star_2);
           t4.setBackgroundResource(R.drawable.ic_star_2);
           t5.setBackgroundResource(R.drawable.ic_star_2);
        }else if (Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",88777)).rate.equals("0")){
         t1.setBackgroundResource(R.drawable.ic_star_2);
         t2.setBackgroundResource(R.drawable.ic_star_2);
         t3.setBackgroundResource(R.drawable.ic_star_2);
         t4.setBackgroundResource(R.drawable.ic_star_2);
         t5.setBackgroundResource(R.drawable.ic_star_2);
        }
        Picasso.with(ClientworkerprofileActivity.this).load(Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",88777)).img).into(img, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }

        });
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext() , LinearLayoutManager.HORIZONTAL, false);
        list.setLayoutManager(layoutManager);
        productadapter = new Photodapter(Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",88777)).image);
        list.setAdapter(productadapter);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeaction();
            }
        });
    }
    public void makeaction( ){
        final ProgressDialog progressDialog = ProgressDialog.show(ClientworkerprofileActivity.this, getString(R.string.wait), getString(R.string.che), true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url+"select_woker", new Response.Listener<String>() {
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
                        Intent i = new Intent(getApplicationContext(),ClientordertabActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                        String msg = obj.getString("msg");
                        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }else {
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
                params.put("user_id", Gdata.user_id);
                params.put("worker_id", Clientdetailsstep1Activity.appsdep.get(getIntent().getIntExtra("pos",455544)).id);
                params.put("order_id",  Clientdetailsstep1Activity.orderid);
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
