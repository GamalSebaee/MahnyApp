package atiaf.mehany.Adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.charge;
import atiaf.mehany.R;

/**
 * Created by سيد on 04/06/2017.
 */
public class Chragedapter extends RecyclerView.Adapter<Chragedapter.MyViewHolder> {

    private List<charge> horizontalList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextViewWithFont title;
        public TextViewWithFont price;
        public TextViewWithFont opnum;

        public MyViewHolder(View view) {
            super(view);
            title = (TextViewWithFont) view.findViewById(R.id.title);
            price = (TextViewWithFont) view.findViewById(R.id.price);
            opnum = (TextViewWithFont) view.findViewById(R.id.opnum);

        }
    }


    public Chragedapter(List<charge> horizontalList) {
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.charge, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      holder.title.setText(horizontalList.get(position).title);
        holder.price.setText(horizontalList.get(position).price);
        holder.opnum.setText(horizontalList.get(position).oper);

    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }


}
