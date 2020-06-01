package atiaf.mehany.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Activity.Clientdetailsstep1Activity;
import atiaf.mehany.Activity.Clientorderdetails4Activity;
import atiaf.mehany.Activity.Clientstep3Activity;
import atiaf.mehany.Activity.Detailsstep2Activity;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.Order;
import atiaf.mehany.Fragment.ClientcurrentFragment;
import atiaf.mehany.R;

/**
 * Created by سيد on 04/06/2017.
 */
public class clientorderdapte extends RecyclerView.Adapter<clientorderdapte.MyViewHolder> {

    private List<Order> horizontalList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextViewWithFont title;
        public TextViewWithFont ordernum;
        public TextViewWithFont date;
        public TextViewWithFont loc;
        public TextViewWithFont ordertype;
        public TextViewWithFont statge;
        public TextViewWithFont subject;
        public TextViewWithFont sertype;
        public TextViewWithFont cancel;
        public TextViewWithFont status;
        public LinearLayout lin;
        public LinearLayout lin1;
        public LinearLayout lin2;
        public LinearLayout linv;

        public MyViewHolder(View view) {
            super(view);
            ordernum = (TextViewWithFont) view.findViewById(R.id.ordernum);
            date = (TextViewWithFont) view.findViewById(R.id.date);
            loc = (TextViewWithFont) view.findViewById(R.id.loc);
            ordertype = (TextViewWithFont) view.findViewById(R.id.ordertype);
            statge = (TextViewWithFont) view.findViewById(R.id.stage);
            subject = (TextViewWithFont) view.findViewById(R.id.subject);
            sertype = (TextViewWithFont) view.findViewById(R.id.sertype);
            cancel = (TextViewWithFont) view.findViewById(R.id.cancel);
            status = (TextViewWithFont) view.findViewById(R.id.status);
            lin = (LinearLayout) view.findViewById(R.id.lin);
            lin1 = (LinearLayout) view.findViewById(R.id.lin1);
            lin2 = (LinearLayout) view.findViewById(R.id.lin2);
            linv = (LinearLayout) view.findViewById(R.id.linv);

        }
    }


    public clientorderdapte(List<Order> horizontalList) {
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clientcurrent, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      holder.ordernum.setText(horizontalList.get(position).ordernum);
        holder.date.setText(horizontalList.get(position).date);
        holder.loc.setText(horizontalList.get(position).loc);
        holder.statge.setText(horizontalList.get(position).stage);
        holder.subject.setText(horizontalList.get(position).subject);
        holder.sertype.setText(horizontalList.get(position).sertype);
        holder.status.setText(horizontalList.get(position).statustitle);
if (horizontalList.get(position).type.equals("0")){
    holder.lin.setVisibility(View.GONE);
    holder.lin1.setVisibility(View.GONE);
    holder.lin2.setVisibility(View.VISIBLE);
holder.ordertype.setText(context.getString(R.string.main5));
}else {
    holder.lin.setVisibility(View.VISIBLE);
    holder.lin1.setVisibility(View.VISIBLE);
    holder.lin2.setVisibility(View.GONE);
    holder.ordertype.setText(context.getString(R.string.main4));

}
        if (horizontalList.get(position).status.equals("0")){
            holder.cancel.setVisibility(View.VISIBLE);
        }else {
            holder.cancel.setVisibility(View.GONE);
        }
        holder.linv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (horizontalList.get(position).status.equals("0")){
                    Intent i = new Intent(context , Clientdetailsstep1Activity.class);
                    i.putExtra("pos",position);
                    context.startActivity(i);
                }else  if (horizontalList.get(position).status.equals("1")){
                    Intent i = new Intent(context , Detailsstep2Activity.class);
                    i.putExtra("pos",position);
                    context.startActivity(i);
                }else  if (horizontalList.get(position).status.equals("2")){
                    Intent i = new Intent(context , Detailsstep2Activity.class);
                    i.putExtra("pos",position);
                    context.startActivity(i);
                }else  if (horizontalList.get(position).status.equals("3")){
                    Intent i = new Intent(context , Clientstep3Activity.class);
                    i.putExtra("pos",position);
                    context.startActivity(i);
                }else  if (horizontalList.get(position).status.equals("4")){
                    Intent i = new Intent(context , Clientorderdetails4Activity.class);
                    i.putExtra("pos",position);
                    context.startActivity(i);
                }
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel_order(horizontalList.get(position).ordernum , position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
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
