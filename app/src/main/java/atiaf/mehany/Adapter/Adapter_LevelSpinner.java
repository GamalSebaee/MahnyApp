package atiaf.mehany.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import atiaf.mehany.Data.InnerData;
import atiaf.mehany.R;

/**
 * Created by TareQ on 11/11/2016.
 */

public class Adapter_LevelSpinner extends ArrayAdapter<InnerData> {
    private Activity c;
    public ArrayList<InnerData> dat ;



    public Adapter_LevelSpinner(Activity c, ArrayList<InnerData> te) {
        super(c, R.layout.custom_spinner,te);
        this.c=c;
        dat=te;
    }

    @Override
    public View getView(int position, View View, ViewGroup parent) {
        LayoutInflater inflater = c.getLayoutInflater();
        android.view.View v = inflater.inflate(R.layout.custom_spinner, null , true);

        TextView title = (TextView) v.findViewById(R.id.customspinner_id);
        title.setText(dat.get(position).lev_title);

        return v ;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    // It gets a View that displays the data at the specified position
}
