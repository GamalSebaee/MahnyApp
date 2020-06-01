package atiaf.mehany.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Fragment.NotFragment;
import atiaf.mehany.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class WorkernotdetialsActivity extends Activity implements OnMapReadyCallback{
    CircleImageView img1;
    TextViewWithFont name1 , loc , sertype1 , details1 , dis1;
    CardView accept , cancel ;
    MapView map1;
     GoogleMap gMap1;
    TextView back;
    GoogleMap mMap1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workernotdetials);
        back = (TextView) findViewById(R.id.back);
        if (Locale.getDefault().getLanguage().equals("en")) {
            back.setRotation(180);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        name1 = (TextViewWithFont) findViewById(R.id.name1);
        dis1 = (TextViewWithFont) findViewById(R.id.dis1);
        loc = (TextViewWithFont) findViewById(R.id.loc1);
        sertype1 = (TextViewWithFont) findViewById(R.id.sertype1);
        details1 = (TextViewWithFont) findViewById(R.id.details1);
        cancel = (CardView) findViewById(R.id.cancel);
        accept = (CardView) findViewById(R.id.ok);
        map1 = (MapView) findViewById(R.id.maps1);
        img1 = (CircleImageView) findViewById(R.id.img1);
        // Should this be created here?
        if (map1 != null)
        {
            map1.onCreate(null);
            map1.onResume();
            map1.getMapAsync(this);
        }
         map1.getMapAsync(new OnMapReadyCallback() {
             @Override
             public void onMapReady(GoogleMap googleMap) {
                 mMap1=googleMap;
             }
         });
        if(mMap1 != null){
            LatLng latLng = new LatLng((NotFragment.appsdep.get(getIntent().getIntExtra("pos",3445445)).lat), (NotFragment.appsdep.get(getIntent().getIntExtra("pos",3445445)).lon));


// Make marker on the current location
            BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.mipmap.map);
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .icon(map_client)
                    .title("my location");


            mMap1.addMarker(options);
// move the camera zoom to the location
            mMap1.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }
        name1.setText(NotFragment.appsdep.get(getIntent().getIntExtra("pos",8676)).clientname);
        dis1.setText(NotFragment.appsdep.get(getIntent().getIntExtra("pos",8676)).km);
        loc.setText(NotFragment.appsdep.get(getIntent().getIntExtra("pos",8676)).loc);
        sertype1.setText(NotFragment.appsdep.get(getIntent().getIntExtra("pos",8676)).sertype);
        details1.setText(NotFragment.appsdep.get(getIntent().getIntExtra("pos",8676)).details);
        Picasso.with(getApplicationContext()).load(NotFragment.appsdep.get(getIntent().getIntExtra("pos",8676)).clientimg).into(img1, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }

        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeaction("1");
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeaction("2");
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getApplicationContext());
        gMap1 = googleMap;
    }
    public void makeaction(final String x ){
        final ProgressDialog progressDialog = ProgressDialog.show(WorkernotdetialsActivity.this, getString(R.string.wait), getString(R.string.che), true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_worker+"make_action_order", new Response.Listener<String>() {
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
                            Intent i = new Intent(getApplicationContext(),WorkertabActivity.class);
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
                    params.put("user_id", Gdata.worker_id);
                    params.put("order_id", NotFragment.appsdep.get(getIntent().getIntExtra("pos",455544)).id);
                    params.put("action",  x);
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
