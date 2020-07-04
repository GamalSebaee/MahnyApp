package atiaf.mehany.Activity.phase2.teams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import atiaf.mehany.R;
import atiaf.mehany.phase2.response.PlaceDetailsModel;
import atiaf.mehany.phase2.response.TeamDetailsModel;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ServicesViewHolder> {
    private List<TeamDetailsModel> serviceModelList;

    private SelectedServiceCallBack selectedServiceCallBack;

    public TeamsAdapter(List<TeamDetailsModel> allServices, SelectedServiceCallBack selectedServiceCallBack) {
        serviceModelList = allServices;
        this.selectedServiceCallBack=selectedServiceCallBack;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_row_item, parent, false);

        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        TeamDetailsModel serviceModel = serviceModelList.get(position);
        holder.tv_place_title.setText(serviceModel.getName());
        holder.tv_address_title.setText(serviceModel.getAddress());
        holder.tv_team_id.setText("#"+serviceModel.getTeamId());
        holder.tv_phone_title.setText(serviceModel.getPhone());
        holder.tv_team_count.setText("("+serviceModel.getNumberOfJoins()+"/"+serviceModel.getNumberOfPlayers()+")");
        holder.itemView.setOnClickListener(v ->{
            selectedServiceCallBack.selectedServiceModel(serviceModel);
        });

    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    static class ServicesViewHolder extends RecyclerView.ViewHolder {
        TextView tv_place_title,tv_address_title,tv_phone_title,tv_team_count,tv_team_id;

        ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_place_title = itemView.findViewById(R.id.tv_place_title);
            tv_address_title = itemView.findViewById(R.id.tv_address_title);
            tv_phone_title = itemView.findViewById(R.id.tv_phone_title);
            tv_team_count = itemView.findViewById(R.id.tv_team_count);
            tv_team_id = itemView.findViewById(R.id.tv_team_id);

        }
    }

    public interface SelectedServiceCallBack{
        void selectedServiceModel(TeamDetailsModel serviceModel);
    }


}
