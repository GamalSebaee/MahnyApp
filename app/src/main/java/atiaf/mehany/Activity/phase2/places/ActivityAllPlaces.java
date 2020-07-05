package atiaf.mehany.Activity.phase2.places;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

import atiaf.mehany.Activity.phase2.BaseActivity;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.R;
import atiaf.mehany.phase2.remote_data.ApiCallBack;
import atiaf.mehany.phase2.response.PlaceDetailsModel;
import atiaf.mehany.phase2.response.PlacesResponse;

public class ActivityAllPlaces extends BaseActivity {

    RecyclerView rv_all_places;
    TextViewWithFont no_data_found;
    TextViewWithFont toolbar_title;
    TextView btnBack;
    RadioGroup rg_filter_list;
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
        btn_create_new_team=findViewById(R.id.btn_create_new_team);
        toolbar_title=findViewById(R.id.toolbar_title);
        rv_all_places=findViewById(R.id.rv_all_places);
        rg_filter_list=findViewById(R.id.rg_filter_list);
        no_data_found=findViewById(R.id.no_data_found);
        btnBack=findViewById(R.id.btnBack);
        no_data_found.setVisibility(View.GONE);
        btn_create_new_team.setVisibility(View.GONE);
        rg_filter_list.setVisibility(View.GONE);
        rv_all_places.setLayoutManager(new LinearLayoutManager(this));
        toolbar_title.setText(getResources().getString(R.string.all_places));

        if (Locale.getDefault().getLanguage().equals("en")) {
            btnBack.setRotation(180);
        }
        btnBack.setOnClickListener((View view) -> super.onBackPressed());
        loadAllPlaces();
    }

    private void loadAllPlaces() {
        progressDialog.show();
        apiManager.getAllPlaces(new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.hide();
                PlacesResponse placesResponse= (PlacesResponse) data;
                if(placesResponse.getAllPlaces() != null && placesResponse.getAllPlaces().size()>0){
                    no_data_found.setVisibility(View.GONE);
                    rv_all_places.setVisibility(View.VISIBLE);
                    setPlacesAdapter(placesResponse.getAllPlaces());

                }else{
                    no_data_found.setVisibility(View.VISIBLE);
                    rv_all_places.setVisibility(View.GONE);
                }
            }

            @Override
            public void ResponseFail(Object data) {
                no_data_found.setVisibility(View.VISIBLE);
                progressDialog.hide();
                Toast.makeText(ActivityAllPlaces.this, ""+data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setPlacesAdapter(List<PlaceDetailsModel> allPlaces) {
        PlacesAdapter placesAdapter=new PlacesAdapter(allPlaces, serviceModel -> {
            Intent intent=new Intent(this,ActivityPlaceDetails.class);
            intent.putExtra("place_id",""+serviceModel.getPlaceId());
            startActivity(intent);
        });
        rv_all_places.setAdapter(placesAdapter);
    }

}
