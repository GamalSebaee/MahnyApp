package atiaf.mehany.Activity.phase2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import atiaf.mehany.Data.OptionModel;

public class CustomSpinnerAdapter extends ArrayAdapter<OptionModel> {

    // Your sent context
    private Context context;
    // Your custom values for the spinner (User)
    private  List<OptionModel> values;

    public CustomSpinnerAdapter(Context context, int textViewResourceId,
                                List<OptionModel> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
        return values.size();
    }

    @Override
    public OptionModel getItem(int position){
        return values.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(values.get(position).getName());
        setTextTypeFace(label);
        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @NonNull
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.WHITE);
        label.setText(values.get(position).getName());
        setTextTypeFace(label);
        return label;
    }

    private void setTextTypeFace(TextView textView){
        Typeface typeface;
        if (Locale.getDefault().getLanguage().equals("ar")) {
            typeface = Typeface.createFromAsset(textView.getContext().getResources().getAssets(), "fonts/Cocon.otf");
        }else {
            typeface = Typeface.createFromAsset(textView.getContext().getResources().getAssets(), "fonts/Cocon.otf");
        }
        textView.setTypeface(typeface);
    }
}