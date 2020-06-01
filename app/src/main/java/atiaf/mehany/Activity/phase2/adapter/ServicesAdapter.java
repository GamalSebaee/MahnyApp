package atiaf.mehany.Activity.phase2.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import atiaf.mehany.Data.ServiceModel;
import atiaf.mehany.R;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder> {
    private List<ServiceModel> serviceModelList;

    private SelectedServiceCallBack selectedServiceCallBack;

    public ServicesAdapter(List<ServiceModel> allServices,SelectedServiceCallBack selectedServiceCallBack) {
        serviceModelList = allServices;
        this.selectedServiceCallBack=selectedServiceCallBack;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_form_content, parent, false);

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
        if (serviceModel.isSelected()) {
            holder.rbServices.setChecked(true);
        } else {
            holder.rbServices.setChecked(false);
        }
        holder.rb_servicesTxt.setText(serviceModel.getTitle());
        holder.itemView.setOnClickListener(v -> {
            selectedServiceCallBack.selectedServiceModel(serviceModel);
            resetAllViews();
            serviceModel.setSelected(true);
            notifyDataSetChanged();

        });

    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    static class ServicesViewHolder extends RecyclerView.ViewHolder {
        RadioButton rbServices;
        TextView rb_servicesTxt;

        ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            rbServices = itemView.findViewById(R.id.rb_services);
            rb_servicesTxt = itemView.findViewById(R.id.rb_servicesTxt);
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
