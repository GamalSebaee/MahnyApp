package atiaf.mehany.Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Order;
import atiaf.mehany.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by سيد on 04/06/2017.
 */
public class Perviousorderdapter extends RecyclerView.Adapter<Perviousorderdapter.MyViewHolder> {

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
        public TextViewWithFont name;
        public CircleImageView img;
        public TextView t1;
        public TextView t11;
        public TextView t2;
        public TextView t12;
        public TextView t3;
        public TextView t13;
        public TextView t4;
        public TextView t14;
        public TextView t5;
        public TextView t15;
        public LinearLayout lin;
        public LinearLayout li;
        public LinearLayout lin1;
        public LinearLayout lin2;

        public MyViewHolder(View view) {
            super(view);
            ordernum = (TextViewWithFont) view.findViewById(R.id.num);
            date = (TextViewWithFont) view.findViewById(R.id.date);
            loc = (TextViewWithFont) view.findViewById(R.id.loc);
            ordertype = (TextViewWithFont) view.findViewById(R.id.ordertype);
            statge = (TextViewWithFont) view.findViewById(R.id.stage);
            subject = (TextViewWithFont) view.findViewById(R.id.subject);
            sertype = (TextViewWithFont) view.findViewById(R.id.sertype);
            lin = (LinearLayout) view.findViewById(R.id.lin);
            lin1 = (LinearLayout) view.findViewById(R.id.lin1);
            lin2 = (LinearLayout) view.findViewById(R.id.lin2);
            li = (LinearLayout) view.findViewById(R.id.li);
            name = (TextViewWithFont) view.findViewById(R.id.name);
            t1 = (TextView) view.findViewById(R.id.t1);
            t11 = (TextView) view.findViewById(R.id.t11);
            t2 = (TextView) view.findViewById(R.id.t2);
            t12 = (TextView) view.findViewById(R.id.t12);
            t3 = (TextView) view.findViewById(R.id.t3);
            t13 = (TextView) view.findViewById(R.id.t13);
            t4 = (TextView) view.findViewById(R.id.t4);
            t14 = (TextView) view.findViewById(R.id.t14);
            t5 = (TextView) view.findViewById(R.id.t5);
            t15 = (TextView) view.findViewById(R.id.t15);
            img = (CircleImageView) view.findViewById(R.id.img);
        }
    }


    public Perviousorderdapter(List<Order> horizontalList) {
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clientpervios, parent, false);
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
        holder.name.setText(horizontalList.get(position).name);
        holder.ordertype.setText(horizontalList.get(position).status);

        if (horizontalList.get(position).type.equals("0")){
    holder.lin.setVisibility(View.GONE);
    holder.lin1.setVisibility(View.GONE);
    holder.lin2.setVisibility(View.VISIBLE);
            holder.li.setVisibility(View.VISIBLE);
        }else {
    holder.lin.setVisibility(View.VISIBLE);
    holder.lin1.setVisibility(View.VISIBLE);
    holder.lin2.setVisibility(View.GONE);
            holder.li.setVisibility(View.GONE);
}
        Picasso.with(context).load(horizontalList.get(position).img).into(holder.img, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }

        });
        if (horizontalList.get(position).rat.equals("5")){
            holder.t1.setBackgroundResource(R.drawable.ic_star);
            holder.t2.setBackgroundResource(R.drawable.ic_star);
            holder.t3.setBackgroundResource(R.drawable.ic_star);
            holder.t4.setBackgroundResource(R.drawable.ic_star);
            holder.t5.setBackgroundResource(R.drawable.ic_star);
        }else if (horizontalList.get(position).rat.equals("4")){
            holder.t1.setBackgroundResource(R.drawable.ic_star);
            holder.t2.setBackgroundResource(R.drawable.ic_star);
            holder.t3.setBackgroundResource(R.drawable.ic_star);
            holder.t4.setBackgroundResource(R.drawable.ic_star);
            holder.t5.setBackgroundResource(R.drawable.ic_star_2);
        }else if (horizontalList.get(position).rat.equals("3")){
            holder.t1.setBackgroundResource(R.drawable.ic_star);
            holder.t2.setBackgroundResource(R.drawable.ic_star);
            holder.t3.setBackgroundResource(R.drawable.ic_star);
            holder.t4.setBackgroundResource(R.drawable.ic_star_2);
            holder.t5.setBackgroundResource(R.drawable.ic_star_2);
        }else if (horizontalList.get(position).rat.equals("2")){
            holder.t1.setBackgroundResource(R.drawable.ic_star);
            holder.t2.setBackgroundResource(R.drawable.ic_star);
            holder.t3.setBackgroundResource(R.drawable.ic_star_2);
            holder.t4.setBackgroundResource(R.drawable.ic_star_2);
            holder.t5.setBackgroundResource(R.drawable.ic_star_2);
        }else if (horizontalList.get(position).rat.equals("1")){
            holder.t1.setBackgroundResource(R.drawable.ic_star);
            holder.t2.setBackgroundResource(R.drawable.ic_star_2);
            holder.t3.setBackgroundResource(R.drawable.ic_star_2);
            holder.t4.setBackgroundResource(R.drawable.ic_star_2);
            holder.t5.setBackgroundResource(R.drawable.ic_star_2);
        }else if (horizontalList.get(position).rat.equals("0")){
            holder.t1.setBackgroundResource(R.drawable.ic_star_2);
            holder.t2.setBackgroundResource(R.drawable.ic_star_2);
            holder.t3.setBackgroundResource(R.drawable.ic_star_2);
            holder.t4.setBackgroundResource(R.drawable.ic_star_2);
            holder.t5.setBackgroundResource(R.drawable.ic_star_2);
        }
        if (horizontalList.get(position).rat1.equals("5")){
            holder.t11.setBackgroundResource(R.drawable.ic_star);
            holder.t12.setBackgroundResource(R.drawable.ic_star);
            holder.t13.setBackgroundResource(R.drawable.ic_star);
            holder.t14.setBackgroundResource(R.drawable.ic_star);
            holder.t15.setBackgroundResource(R.drawable.ic_star);
        }else if (horizontalList.get(position).rat1.equals("4")){
            holder.t11.setBackgroundResource(R.drawable.ic_star);
            holder.t12.setBackgroundResource(R.drawable.ic_star);
            holder.t13.setBackgroundResource(R.drawable.ic_star);
            holder.t14.setBackgroundResource(R.drawable.ic_star);
            holder.t15.setBackgroundResource(R.drawable.ic_star_2);
        }else if (horizontalList.get(position).rat1.equals("3")){
            holder.t11.setBackgroundResource(R.drawable.ic_star);
            holder.t12.setBackgroundResource(R.drawable.ic_star);
            holder.t13.setBackgroundResource(R.drawable.ic_star);
            holder.t14.setBackgroundResource(R.drawable.ic_star_2);
            holder.t15.setBackgroundResource(R.drawable.ic_star_2);
        }else if (horizontalList.get(position).rat1.equals("2")){
            holder.t11.setBackgroundResource(R.drawable.ic_star);
            holder.t12.setBackgroundResource(R.drawable.ic_star);
            holder.t13.setBackgroundResource(R.drawable.ic_star_2);
            holder.t14.setBackgroundResource(R.drawable.ic_star_2);
            holder.t15.setBackgroundResource(R.drawable.ic_star_2);
        }else if (horizontalList.get(position).rat1.equals("1")){
            holder.t11.setBackgroundResource(R.drawable.ic_star);
            holder.t12.setBackgroundResource(R.drawable.ic_star_2);
            holder.t13.setBackgroundResource(R.drawable.ic_star_2);
            holder.t14.setBackgroundResource(R.drawable.ic_star_2);
            holder.t15.setBackgroundResource(R.drawable.ic_star_2);
        }else if (horizontalList.get(position).rat1.equals("0")){
            holder.t11.setBackgroundResource(R.drawable.ic_star_2);
            holder.t12.setBackgroundResource(R.drawable.ic_star_2);
            holder.t13.setBackgroundResource(R.drawable.ic_star_2);
            holder.t14.setBackgroundResource(R.drawable.ic_star_2);
            holder.t15.setBackgroundResource(R.drawable.ic_star_2);
        }
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }



}
