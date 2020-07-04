package atiaf.mehany.Activity.phase2.places;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import atiaf.mehany.R;
import atiaf.mehany.phase2.response.PlaceDetailsModel;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ServicesViewHolder> {
    private List<PlaceDetailsModel> serviceModelList;

    private SelectedServiceCallBack selectedServiceCallBack;

    public PlacesAdapter(List<PlaceDetailsModel> allServices, SelectedServiceCallBack selectedServiceCallBack) {
        serviceModelList = allServices;
        this.selectedServiceCallBack=selectedServiceCallBack;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_row_item, parent, false);

        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        PlaceDetailsModel serviceModel = serviceModelList.get(position);
        holder.tv_place_title.setText(serviceModel.getTitle());
        holder.tv_address_title.setText(serviceModel.getAddress());
        holder.tv_phone_title.setText(serviceModel.getPhone());
        holder.itemView.setOnClickListener(v ->{
            selectedServiceCallBack.selectedServiceModel(serviceModel);
        });

    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    static class ServicesViewHolder extends RecyclerView.ViewHolder {
        TextView tv_place_title,tv_address_title,tv_phone_title;

        ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_place_title = itemView.findViewById(R.id.tv_place_title);
            tv_address_title = itemView.findViewById(R.id.tv_address_title);
            tv_phone_title = itemView.findViewById(R.id.tv_phone_title);

        }
    }

    public interface SelectedServiceCallBack{
        void selectedServiceModel(PlaceDetailsModel serviceModel);
    }


}
