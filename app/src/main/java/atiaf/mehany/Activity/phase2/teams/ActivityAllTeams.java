package atiaf.mehany.Activity.phase2.teams;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

import atiaf.mehany.Activity.phase2.BaseActivity;
import atiaf.mehany.Activity.phase2.places.PlacesAdapter;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.R;
import atiaf.mehany.phase2.remote_data.ApiCallBack;
import atiaf.mehany.phase2.response.PlaceDetailsModel;
import atiaf.mehany.phase2.response.TeamDetailsModel;
import atiaf.mehany.phase2.response.TeamsResponse;

public class ActivityAllTeams extends BaseActivity {

    RecyclerView rv_all_places;
    TextViewWithFont no_data_found;
    TextViewWithFont toolbar_title;
    TextView btnBack;
    FloatingActionButton btn_create_new_team;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        initViews();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void initViews() {

        toolbar_title=findViewById(R.id.toolbar_title);
        btn_create_new_team=findViewById(R.id.btn_create_new_team);
        rv_all_places=findViewById(R.id.rv_all_places);
        no_data_found=findViewById(R.id.no_data_found);
        btnBack=findViewById(R.id.btnBack);
        no_data_found.setVisibility(View.GONE);
        rv_all_places.setLayoutManager(new LinearLayoutManager(this));
        toolbar_title.setText(getResources().getString(R.string.all_teams));
        btn_create_new_team.setVisibility(View.VISIBLE);
        if (Locale.getDefault().getLanguage().equals("en")) {
            btnBack.setRotation(180);
        }
        btnBack.setOnClickListener((View view) -> super.onBackPressed());
        loadAllPlaces();
    }

    private void loadAllPlaces() {
        progressDialog.show();
        apiManager.getAllTeams(new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.hide();
                TeamsResponse placesResponse= (TeamsResponse) data;
                if(placesResponse.getAllTeams() != null && placesResponse.getAllTeams().size()>0){
                    no_data_found.setVisibility(View.GONE);
                    rv_all_places.setVisibility(View.VISIBLE);
                    setTeamsAdapter(placesResponse.getAllTeams());

                }else{
                    no_data_found.setVisibility(View.VISIBLE);
                    rv_all_places.setVisibility(View.GONE);
                }
            }

            @Override
            public void ResponseFail(Object data) {
                no_data_found.setVisibility(View.VISIBLE);
                progressDialog.hide();
                Toast.makeText(ActivityAllTeams.this, ""+data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTeamsAdapter(List<TeamDetailsModel> allPlaces) {
        TeamsAdapter placesAdapter=new TeamsAdapter(allPlaces, serviceModel -> {

        });
        rv_all_places.setAdapter(placesAdapter);
    }

}
