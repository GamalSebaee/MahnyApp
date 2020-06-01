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
public class Chragedapte extends RecyclerView.Adapter<Chragedapte.MyViewHolder> {

    private List<charge> horizontalList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextViewWithFont title;
        public TextViewWithFont price;

        public MyViewHolder(View view) {
            super(view);
            title = (TextViewWithFont) view.findViewById(R.id.title);
            price = (TextViewWithFont) view.findViewById(R.id.body);

        }
    }


    public Chragedapte(List<charge> horizontalList) {
        this.horizontalList = horizontalList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.charge1, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      holder.title.setText(horizontalList.get(position).title);
        holder.price.setText(horizontalList.get(position).price);

    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }


}
