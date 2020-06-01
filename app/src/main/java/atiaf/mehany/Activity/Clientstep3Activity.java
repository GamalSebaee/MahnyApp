package atiaf.mehany.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Fragment.ClientcurrentFragment;
import atiaf.mehany.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Clientstep3Activity extends Activity {
    TextView back;
    TextViewWithFont ordernum, title , tit1;
    TextViewWithFont date;
    TextViewWithFont loc;
    TextViewWithFont ordertype;
    TextViewWithFont statge;
    TextViewWithFont subject;
    TextViewWithFont sertype, name;
    TextViewWithFont cancel;
    TextViewWithFont status;
    LinearLayout lin;
    LinearLayout lin1;
    LinearLayout lin2;
    CircleImageView img;
    TextView t1, t2, t3, t4, t5;
    FrameLayout call;
    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientstep3);
        back = (TextView) findViewById(R.id.back);
        title = (TextViewWithFont) findViewById(R.id.title);
        tit1 = (TextViewWithFont) findViewById(R.id.tit1);
        ordernum = (TextViewWithFont) findViewById(R.id.ordernum);
        date = (TextViewWithFont) findViewById(R.id.date);
        loc = (TextViewWithFont) findViewById(R.id.loc);
        ordertype = (TextViewWithFont) findViewById(R.id.ordertype);
        statge = (TextViewWithFont) findViewById(R.id.stage);
        subject = (TextViewWithFont) findViewById(R.id.subject);
        sertype = (TextViewWithFont) findViewById(R.id.sertype);
        status = (TextViewWithFont) findViewById(R.id.status);
        lin = (LinearLayout) findViewById(R.id.lin);
        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);
        img = (CircleImageView) findViewById(R.id.img);
        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);
        t5 = (TextView) findViewById(R.id.t5);
        name = (TextViewWithFont) findViewById(R.id.name);
        call = (FrameLayout) findViewById(R.id.call);
        if (Locale.getDefault().getLanguage().equals("en")) {
            back.setRotation(180);
        }
        if (ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos",56666)).type.equals("0")){
            title.setText(getString(R.string.detailsorder10));
            tit1.setText(getString(R.string.detailsorder11));
        }else {
            title.setText(getString(R.string.detailsorder100));
            tit1.setText(getString(R.string.detailsorder111));
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ordernum.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos", 444445)).ordernum);
        date.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos", 444445)).date);
        loc.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos", 444445)).loc);
        statge.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos", 444445)).stage);
        subject.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos", 444445)).subject);
        sertype.setText(ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos", 444445)).sertype);
        if (ClientcurrentFragment.appsdep.get(getIntent().getIntExtra("pos", 444445)).type.equals("0")) {
            lin.setVisibility(View.GONE);
            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);
            ordertype.setText(getString(R.string.main5));
        } else {
            lin.setVisibility(View.VISIBLE);
            lin1.setVisibility(View.VISIBLE);
            lin2.setVisibility(View.GONE);
            ordertype.setText(getString(R.string.main4));

        }
        if (isNetworkAvailable()) {
            loaddata();
        } else {
            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(Clientstep3Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
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
        final ProgressDialog progressDialog = ProgressDialog.show(Clientstep3Activity.this, getString(R.string.wait), getString(R.string.che), true);
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
                    JSONObject jo = jsonArray.getJSONObject(0);
                    name.setText(jo.getString("first_name") + " " + jo.getString("last_name"));
                    if (String.valueOf(jo.getInt("rate")).equals("5")){
                        t1.setBackgroundResource(R.drawable.ic_star);
                        t2.setBackgroundResource(R.drawable.ic_star);
                        t3.setBackgroundResource(R.drawable.ic_star);
                        t4.setBackgroundResource(R.drawable.ic_star);
                        t5.setBackgroundResource(R.drawable.ic_star);
                    }else if (String.valueOf(jo.getInt("rate")).equals("4")){
                        t1.setBackgroundResource(R.drawable.ic_star);
                        t2.setBackgroundResource(R.drawable.ic_star);
                        t3.setBackgroundResource(R.drawable.ic_star);
                        t4.setBackgroundResource(R.drawable.ic_star);
                        t5.setBackgroundResource(R.drawable.ic_star_2);
                    }else if (String.valueOf(jo.getInt("rate")).equals("3")){
                        t1.setBackgroundResource(R.drawable.ic_star);
                        t2.setBackgroundResource(R.drawable.ic_star);
                        t3.setBackgroundResource(R.drawable.ic_star);
                        t4.setBackgroundResource(R.drawable.ic_star_2);
                        t5.setBackgroundResource(R.drawable.ic_star_2);
                    }else if (String.valueOf(jo.getInt("rate")).equals("2")){
                        t1.setBackgroundResource(R.drawable.ic_star);
                        t2.setBackgroundResource(R.drawable.ic_star);
                        t3.setBackgroundResource(R.drawable.ic_star_2);
                        t4.setBackgroundResource(R.drawable.ic_star_2);
                        t5.setBackgroundResource(R.drawable.ic_star_2);
                    }else if (String.valueOf(jo.getInt("rate")).equals("1")){
                        t1.setBackgroundResource(R.drawable.ic_star);
                        t2.setBackgroundResource(R.drawable.ic_star_2);
                        t3.setBackgroundResource(R.drawable.ic_star_2);
                        t4.setBackgroundResource(R.drawable.ic_star_2);
                        t5.setBackgroundResource(R.drawable.ic_star_2);
                    }else if (String.valueOf(jo.getInt("rate")).equals("0")){
                        t1.setBackgroundResource(R.drawable.ic_star_2);
                        t2.setBackgroundResource(R.drawable.ic_star_2);
                        t3.setBackgroundResource(R.drawable.ic_star_2);
                        t4.setBackgroundResource(R.drawable.ic_star_2);
                        t5.setBackgroundResource(R.drawable.ic_star_2);
                    }
                    Picasso.with(Clientstep3Activity.this).load(jo.getString("img")).into(img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }

                    });
                       phone = jo.getString("phone");


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
}
