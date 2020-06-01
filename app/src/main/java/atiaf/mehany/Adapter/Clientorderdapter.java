package atiaf.mehany.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Activity.Clientdetailsstep1Activity;
import atiaf.mehany.Activity.ClientordertabActivity;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.clientorder;
import atiaf.mehany.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by سيد on 04/06/2017.
 */
public class Clientorderdapter extends RecyclerView.Adapter<Clientorderdapter.MyViewHolder> {

    private List<clientorder> horizontalList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextViewWithFont name;
        public TextViewWithFont dis;
        public TextViewWithFont vie;
        public TextView t1;
        public TextView t2;
        public TextView t3;
        public TextView t4;
        public TextView t5;
public LinearLayout lin ;
        public CircleImageView image ;
        public MyViewHolder(View view) {
            super(view);
            name = (TextViewWithFont) view.findViewById(R.id.name);
            dis = (TextViewWithFont) view.findViewById(R.id.dis);
            vie = (TextViewWithFont) view.findViewById(R.id.view);
            image = (CircleImageView) view.findViewById(R.id.image);
            t1 = (TextView) view.findViewById(R.id.t1);
            t2 = (TextView) view.findViewById(R.id.t2);
            t3 = (TextView) view.findViewById(R.id.t3);
            t4 = (TextView) view.findViewById(R.id.t4);
            t5 = (TextView) view.findViewById(R.id.t5);
            lin = (LinearLayout) view.findViewById(R.id.lin);

        }
    }


    public Clientorderdapter(List<clientorder> horizontalList) {
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detailsorder, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      holder.name.setText(horizontalList.get(position).name);
        holder.dis.setText(horizontalList.get(position).dis);
      if (horizontalList.get(position).rate.equals("5")){
          holder.t1.setBackgroundResource(R.drawable.ic_star);
          holder.t2.setBackgroundResource(R.drawable.ic_star);
          holder.t3.setBackgroundResource(R.drawable.ic_star);
          holder.t4.setBackgroundResource(R.drawable.ic_star);
          holder.t5.setBackgroundResource(R.drawable.ic_star);
      }else if (horizontalList.get(position).rate.equals("4")){
          holder.t1.setBackgroundResource(R.drawable.ic_star);
          holder.t2.setBackgroundResource(R.drawable.ic_star);
          holder.t3.setBackgroundResource(R.drawable.ic_star);
          holder.t4.setBackgroundResource(R.drawable.ic_star);
          holder.t5.setBackgroundResource(R.drawable.ic_star_2);
      }else if (horizontalList.get(position).rate.equals("3")){
          holder.t1.setBackgroundResource(R.drawable.ic_star);
          holder.t2.setBackgroundResource(R.drawable.ic_star);
          holder.t3.setBackgroundResource(R.drawable.ic_star);
          holder.t4.setBackgroundResource(R.drawable.ic_star_2);
          holder.t5.setBackgroundResource(R.drawable.ic_star_2);
      }else if (horizontalList.get(position).rate.equals("2")){
          holder.t1.setBackgroundResource(R.drawable.ic_star);
          holder.t2.setBackgroundResource(R.drawable.ic_star);
          holder.t3.setBackgroundResource(R.drawable.ic_star_2);
          holder.t4.setBackgroundResource(R.drawable.ic_star_2);
          holder.t5.setBackgroundResource(R.drawable.ic_star_2);
      }else if (horizontalList.get(position).rate.equals("1")){
          holder.t1.setBackgroundResource(R.drawable.ic_star);
          holder.t2.setBackgroundResource(R.drawable.ic_star_2);
          holder.t3.setBackgroundResource(R.drawable.ic_star_2);
          holder.t4.setBackgroundResource(R.drawable.ic_star_2);
          holder.t5.setBackgroundResource(R.drawable.ic_star_2);
      }else if (horizontalList.get(position).rate.equals("0")){
          holder.t1.setBackgroundResource(R.drawable.ic_star_2);
          holder.t2.setBackgroundResource(R.drawable.ic_star_2);
          holder.t3.setBackgroundResource(R.drawable.ic_star_2);
          holder.t4.setBackgroundResource(R.drawable.ic_star_2);
          holder.t5.setBackgroundResource(R.drawable.ic_star_2);
      }
        Log.e("ccccd",horizontalList.get(position).img);
       Picasso.with(context).load(horizontalList.get(position).img).into(holder.image, new Callback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onError() {

        }

    });
        if (position%2==0){
            holder.lin.setBackgroundColor(Color.parseColor("#ffffff"));
        }else {
            holder.lin.setBackgroundColor(Color.parseColor("#f1f5f7"));
        }
        holder.vie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.activity_clientworkerprofile);
                final TextView t1 , t2 , t3 , t4 ,t5 ;
                final TextViewWithFont name  , job;
                final CardView accept ;
                final RecyclerView list ;
                final Photodapter productadapter ;
                final CircleImageView img ;
                img = (CircleImageView) dialog.findViewById(R.id.img);
                t1 = (TextView) dialog.findViewById(R.id.t1);
                t2 = (TextView) dialog.findViewById(R.id.t2);
                t3 = (TextView) dialog.findViewById(R.id.t3);
                t4 = (TextView) dialog.findViewById(R.id.t4);
                t5 = (TextView) dialog.findViewById(R.id.t5);
                name = (TextViewWithFont) dialog.findViewById(R.id.name);
                job = (TextViewWithFont) dialog.findViewById(R.id.job);
                accept = (CardView) dialog.findViewById(R.id.accept);
                list = (RecyclerView) dialog.findViewById(R.id.list);
                name.setText(horizontalList.get(position).name);
                job.setText(horizontalList.get(position).job);
                if (horizontalList.get(position).rate.equals("5")){
                    t1.setBackgroundResource(R.drawable.ic_star);
                    t2.setBackgroundResource(R.drawable.ic_star);
                    t3.setBackgroundResource(R.drawable.ic_star);
                    t4.setBackgroundResource(R.drawable.ic_star);
                    t5.setBackgroundResource(R.drawable.ic_star);
                }else if (horizontalList.get(position).rate.equals("4")){
                    t1.setBackgroundResource(R.drawable.ic_star);
                    t2.setBackgroundResource(R.drawable.ic_star);
                    t3.setBackgroundResource(R.drawable.ic_star);
                    t4.setBackgroundResource(R.drawable.ic_star);
                    t5.setBackgroundResource(R.drawable.ic_star_2);
                }else if (horizontalList.get(position).rate.equals("3")){
                    t1.setBackgroundResource(R.drawable.ic_star);
                    t2.setBackgroundResource(R.drawable.ic_star);
                    t3.setBackgroundResource(R.drawable.ic_star);
                    t4.setBackgroundResource(R.drawable.ic_star_2);
                    t5.setBackgroundResource(R.drawable.ic_star_2);
                }else if (horizontalList.get(position).rate.equals("2")){
                    t1.setBackgroundResource(R.drawable.ic_star);
                    t2.setBackgroundResource(R.drawable.ic_star);
                    t3.setBackgroundResource(R.drawable.ic_star_2);
                    t4.setBackgroundResource(R.drawable.ic_star_2);
                    t5.setBackgroundResource(R.drawable.ic_star_2);
                }else if (horizontalList.get(position).rate.equals("1")){
                    t1.setBackgroundResource(R.drawable.ic_star);
                    t2.setBackgroundResource(R.drawable.ic_star_2);
                    t3.setBackgroundResource(R.drawable.ic_star_2);
                    t4.setBackgroundResource(R.drawable.ic_star_2);
                    t5.setBackgroundResource(R.drawable.ic_star_2);
                }else if (horizontalList.get(position).rate.equals("0")){
                    t1.setBackgroundResource(R.drawable.ic_star_2);
                    t2.setBackgroundResource(R.drawable.ic_star_2);
                    t3.setBackgroundResource(R.drawable.ic_star_2);
                    t4.setBackgroundResource(R.drawable.ic_star_2);
                    t5.setBackgroundResource(R.drawable.ic_star_2);
                }
                try {
                    Picasso.with(context).load(horizontalList.get(position).img).into(img, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }

                    });
                }catch (OutOfMemoryError e){

                }

                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL, false);
                list.setLayoutManager(layoutManager);
                productadapter = new Photodapter(horizontalList.get(position).image);
                list.setAdapter(productadapter);
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Clientdetailsstep1Activity.type.equals("0")){
                            makeaction(Clientdetailsstep1Activity.orderid,horizontalList.get(position).id
                            );
                        }else{
                            makeactio(Clientdetailsstep1Activity.orderid,horizontalList.get(position).id
                            );
                        }

                    }
                });

                dialog.show();
//                Intent i = new Intent(context , ClientworkerprofileActivity.class);
//                i.putExtra("pos",position);
//                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }
    public void makeaction(final String orderid , final  String id){
        final ProgressDialog progressDialog = ProgressDialog.show(context, context.getString(R.string.wait), context.getString(R.string.che), true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url+"select_woker", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("Success")){
                        Intent i = new Intent(context,ClientordertabActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                        ((Activity)context).finish();
                        String msg = obj.getString("msg");
                        Toast t = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
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
                params.put("worker_id", id);
                params.put("order_id",  orderid);
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
    public void makeactio(final String orderid , final  String id){
        final ProgressDialog progressDialog = ProgressDialog.show(context, context.getString(R.string.wait), context.getString(R.string.che), true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url+"select_teacher", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("Success")){
                        Intent i = new Intent(context,ClientordertabActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(i);
                        ((Activity)context).finish();
                        String msg = obj.getString("msg");
                        Toast t = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
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
                params.put("teacher_id", id);
                params.put("order_id",  orderid);
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
