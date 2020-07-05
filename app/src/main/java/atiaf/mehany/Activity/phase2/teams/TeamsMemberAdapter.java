package atiaf.mehany.Activity.phase2.teams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import atiaf.mehany.R;
import atiaf.mehany.phase2.response.JoinModel;
import atiaf.mehany.phase2.response.TeamDetailsModel;

public class TeamsMemberAdapter extends RecyclerView.Adapter<TeamsMemberAdapter.ServicesViewHolder> {
    private List<JoinModel> serviceModelList;

    private SelectedServiceCallBack selectedServiceCallBack;

    public TeamsMemberAdapter(List<JoinModel> allServices, SelectedServiceCallBack selectedServiceCallBack) {
        serviceModelList = allServices;
        this.selectedServiceCallBack=selectedServiceCallBack;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_member_row_item, parent, false);

        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {
        JoinModel serviceModel = serviceModelList.get(position);
        holder.tv_team_member_name.setText("("+(position+1)+") "+serviceModel.getName());
        holder.itemView.setOnClickListener(v ->{
            selectedServiceCallBack.selectedServiceModel(serviceModel);
        });

    }

    @Override
    public int getItemCount() {
        return serviceModelList.size();
    }

    static class ServicesViewHolder extends RecyclerView.ViewHolder {
        TextView tv_team_member_name;

        ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_team_member_name = itemView.findViewById(R.id.tv_team_member_name);

        }
    }

    public interface SelectedServiceCallBack{
        void selectedServiceModel(JoinModel serviceModel);
    }


}
