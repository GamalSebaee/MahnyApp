package atiaf.mehany.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Activity.TeachernotdetialsActivity;
import atiaf.mehany.Activity.TeachertabActivity;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.Not;
import atiaf.mehany.Fragment.TeacherNotFragment;
import atiaf.mehany.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by سيد on 04/06/2017.
 */
public class Teachernotdapter extends RecyclerView.Adapter<Teachernotdapter.MyViewHolder> {

    private List<Not> horizontalList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        public TextViewWithFont name;
        public TextViewWithFont name1;
        public TextViewWithFont name2;
        public TextViewWithFont dis;
        public TextViewWithFont dis1;
        public TextViewWithFont loc;
        public TextViewWithFont sertype;
        public TextViewWithFont sertype1;
        public TextViewWithFont details;
        public TextViewWithFont details1;
        public CardView lin;
        public CardView lin1;
        public CardView lin2;
        public CardView start;
        public CardView cancel;
        public CircleImageView img;
        public CircleImageView img1;
        public CircleImageView img2;
        public MapView map;
        public GoogleMap gMap;
        public MapView map1;
        public GoogleMap gMap1;
        public MyViewHolder(View view) {
            super(view);
            name = (TextViewWithFont) view.findViewById(R.id.name);
            name1 = (TextViewWithFont) view.findViewById(R.id.name1);
            name2 = (TextViewWithFont) view.findViewById(R.id.name2);
            dis = (TextViewWithFont) view.findViewById(R.id.dis);
            dis1 = (TextViewWithFont) view.findViewById(R.id.dis1);
            loc = (TextViewWithFont) view.findViewById(R.id.loc1);
            sertype = (TextViewWithFont) view.findViewById(R.id.sertype1);
            sertype1 = (TextViewWithFont) view.findViewById(R.id.sertype2);
            details = (TextViewWithFont) view.findViewById(R.id.details1);
            details1 = (TextViewWithFont) view.findViewById(R.id.details2);
            cancel = (CardView) view.findViewById(R.id.cancel);
            start = (CardView) view.findViewById(R.id.start);
            lin = (CardView) view.findViewById(R.id.lin);
            lin1 = (CardView) view.findViewById(R.id.lin1);
            lin2 = (CardView) view.findViewById(R.id.lin2);
            map = (MapView) view.findViewById(R.id.maps);
            img = (CircleImageView) view.findViewById(R.id.img);
            img1 = (CircleImageView) view.findViewById(R.id.img1);
            img2 = (CircleImageView) view.findViewById(R.id.img2);

            // Should this be created here?
            if (map != null)
            {
                map.onCreate(null);
                map.onResume();
                map.getMapAsync(this);
            }
            map1 = (MapView) view.findViewById(R.id.maps1);
            // Should this be created here?
            if (map1 != null)
            {
                map1.onCreate(null);
                map1.onResume();
                map1.getMapAsync(this);
            }
        }
        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(context);
            gMap = googleMap;
            gMap1 = googleMap;
        }
    }


    public Teachernotdapter(List<Not> horizontalList) {
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teachernot, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }
    GoogleMap mMap=null;
    GoogleMap mMap1 ;
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

         holder.map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
        if(mMap != null){
            LatLng latLng = new LatLng((horizontalList.get(position).lat), (horizontalList.get(position).lon));


// Make marker on the current location
            BitmapDescriptor map_client = BitmapDescriptorFactory.fromResource(R.mipmap.map);
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .icon(map_client)
                    .title("my location");


            mMap.addMarker(options);
// move the camera zoom to the location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        }

         holder.map1.getMapAsync(new OnMapReadyCallback() {
             @Override
             public void onMapReady(GoogleMap googleMap) {
                 mMap1=googleMap;
             }
         });
        if(mMap1 != null){
            LatLng latLng = new LatLng((horizontalList.get(position).lat), (horizontalList.get(position).lon));


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
        if (horizontalList.get(position).status.equals("0")){
            holder.lin.setVisibility(View.VISIBLE);
            holder.lin1.setVisibility(View.GONE);
            holder.lin2.setVisibility(View.GONE);
        }else  if (horizontalList.get(position).status.equals("1")){
            holder.lin.setVisibility(View.GONE);
            holder.lin1.setVisibility(View.VISIBLE);
            holder.lin2.setVisibility(View.GONE);
        }else  if (horizontalList.get(position).status.equals("9")){
            holder.lin.setVisibility(View.GONE);
            holder.lin1.setVisibility(View.GONE);
            holder.lin2.setVisibility(View.VISIBLE);
        }
        holder.loc.setText(horizontalList.get(position).loc);
        holder.dis.setText(horizontalList.get(position).km);
        holder.dis1.setText(horizontalList.get(position).km);
        holder.sertype.setText(horizontalList.get(position).sertype);
        holder.sertype1.setText(horizontalList.get(position).sertype);
        holder.details.setText(horizontalList.get(position).details);
        holder.details1.setText(horizontalList.get(position).details);
        holder.name.setText(horizontalList.get(position).clientname);
        holder.name1.setText(horizontalList.get(position).clientname);
        holder.name2.setText(horizontalList.get(position).clientname);
        Picasso.with(context).load(horizontalList.get(position).clientimg).into(holder.img, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }

        });
        Picasso.with(context).load(horizontalList.get(position).clientimg).into(holder.img1, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }

        });
        Picasso.with(context).load(horizontalList.get(position).clientimg).into(holder.img2, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }

        });
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context , TeachernotdetialsActivity.class);
                i.putExtra("pos",position);
                context.startActivity(i);
            }
        });
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_order(horizontalList.get(position).id , position , horizontalList.get(position).clientid);
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                make_done(horizontalList.get(position).id , position , horizontalList.get(position).clientid);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }
    public void start_order(final String id , final int pos , final String user_id){
        final ProgressDialog progressDialog = ProgressDialog.show(context, context.getString(R.string.wait), context.getString(R.string.che), true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher+"start_order", new Response.Listener<String>() {
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
                        TeacherNotFragment.appsdep.remove(pos);
                        TeacherNotFragment.productadapter.notifyDataSetChanged();
                        Intent i = new Intent(context , TeachertabActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                        ((Activity) context) .finish();
                        Gdata.pager = 3 ;
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
                params.put("user_id", user_id);
                params.put("teacher_id", Gdata.teacher_id);
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
    public void make_done(final String id , final int pos , final String user_id){
        final ProgressDialog progressDialog = ProgressDialog.show(context, context.getString(R.string.wait), context.getString(R.string.che), true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_worker+"make_Done", new Response.Listener<String>() {
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
                        TeacherNotFragment.appsdep.remove(pos);
                        TeacherNotFragment.productadapter.notifyDataSetChanged();

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
                params.put("user_id", Gdata.teacher_id);
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
