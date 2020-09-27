package atiaf.mehany.Activity.phase2.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import atiaf.mehany.Data.ServiceModel;
import atiaf.mehany.R;

public class ServicesGridAdapter extends RecyclerView.Adapter<ServicesGridAdapter.ServicesViewHolder> {
    private List<ServiceModel> serviceModelList;

    private SelectedServiceCallBack selectedServiceCallBack;

    public ServicesGridAdapter(List<ServiceModel> allServices, SelectedServiceCallBack selectedServiceCallBack) {
        serviceModelList = allServices;
        this.selectedServiceCallBack=selectedServiceCallBack;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_form_circle_content, parent, false);

        return new ServicesViewHolder(view);
    }
    private void resetAllViews(){
        for (ServiceModel serviceModel:serviceModelList){
            serviceModel.setSelected(false);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        ServiceModel serviceModel = serviceModelList.get(position);
        holder.rbServices.setChecked(false);
        holder.rb_servicesTxt.setText(serviceModel.getTitle());
        if(position == 0){
            holder.iv_tab_icon.setImageResource(R.drawable.ic_teacher_white);
        }else if(position == 1){
            holder.iv_tab_icon.setImageResource(R.drawable.ic_worker_white);
        }else {
            if (serviceModel.getIcon() != null && !serviceModel.getIcon().isEmpty()) {
                Log.d("serviceModel",""+serviceModel.getIcon());
                Glide.with(holder.iv_tab_icon.getContext())
                        .load(serviceModel.getIcon())
                        .error(R.drawable.ic_baseline_settings_24)
                        .override(100, 100).into(holder.iv_tab_icon);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            selectedServiceCallBack.selectedServiceModel(serviceModel);
        });

    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    static class ServicesViewHolder extends RecyclerView.ViewHolder {
        RadioButton rbServices;
        TextView rb_servicesTxt;
        ImageView iv_tab_icon;

        ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            rbServices = itemView.findViewById(R.id.rb_services);
            rb_servicesTxt = itemView.findViewById(R.id.rb_servicesTxt);
            iv_tab_icon = itemView.findViewById(R.id.iv_tab_icon);
            /*Typeface typeface;
            if (Locale.getDefault().getLanguage().equals("ar")) {
                typeface = Typeface.createFromAsset(rbServices.getContext().getResources().getAssets(), "fonts/Cocon.otf");
            }else {
                typeface = Typeface.createFromAsset(rbServices.getContext().getResources().getAssets(), "fonts/Cocon.otf");
            }
            rbServices.setTypeface(typeface);*/
        }
    }

    public interface SelectedServiceCallBack{
        void selectedServiceModel(ServiceModel serviceModel);
    }


}
