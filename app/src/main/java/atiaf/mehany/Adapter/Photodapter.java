package atiaf.mehany.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
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

import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.Workerimg;
import atiaf.mehany.Fragment.ProfileFragment;
import atiaf.mehany.R;

/**
 * Created by سيد on 04/06/2017.
 */
public class Photodapter extends RecyclerView.Adapter<Photodapter.MyViewHolder> {

    private List<Workerimg> horizontalList;
    Context context;
    PopupMenu popup;
    Dialog dialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public LinearLayout lin;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.img);
            lin = (LinearLayout) view.findViewById(R.id.lin);

        }
    }


    public Photodapter(List<Workerimg> horizontalList) {
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (horizontalList.get(position).img.equals("")) {
            holder.lin.setVisibility(View.VISIBLE);
            holder.image.setVisibility(View.GONE);
        } else {
            holder.lin.setVisibility(View.GONE);
            holder.image.setVisibility(View.VISIBLE);
            Picasso.with(context).load(horizontalList.get(position).img).into(holder.image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }

            });
        }
        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ((Activity) context).startActivityForResult(i, 1);
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.pictures);
                final TextView back = (TextView) dialog.findViewById(R.id.back);
                final TextView more = (TextView) dialog.findViewById(R.id.more);
                final ImageView img = (ImageView) dialog.findViewById(R.id.img);
                Picasso.with(context).load(horizontalList.get(position).img).into(img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }

                });
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup = new PopupMenu(context, more);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater()
                                .inflate(R.menu.delete, popup.getMenu());
                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.delete:
                                        delete(horizontalList.get(position).id, position);
                                        break;

                                }
                                return true;
                            }
                        });

                        popup.show();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }

    public void delete(final String id, final int pos) {
        final ProgressDialog progressDialog = ProgressDialog.show(context, context.getString(R.string.wait), context.getString(R.string.che), true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_worker + "delete_image", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("Success")) {
                        Gdata.array.remove(pos);
                        ProfileFragment.productadapter.notifyDataSetChanged();
                        popup.dismiss();
                        dialog.dismiss();
                    } else {
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
                params.put("image_id", id);
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
