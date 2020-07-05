package atiaf.mehany.Activity.phase2.teams;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Locale;

import atiaf.mehany.Activity.phase2.BaseActivity;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.GeneralModel;
import atiaf.mehany.R;
import atiaf.mehany.phase2.remote_data.ApiCallBack;
import atiaf.mehany.phase2.response.TeamDetailsModel;
import atiaf.mehany.phase2.response.TeamDetailsResponse;

public class ActivityTeamDetails extends BaseActivity {
    TextViewWithFont tv_place_title,tv_date, tv_address_title, tv_phone_title,tv_team_joiners_empty,
            tv_email_title, tv_note_title,tv_view_location, tv_book_now,tv_team_id,tv_team_count,
            tv_user_is_joined_label;
    View reservation_content;
    EditText et_name_title, et_phone_title, et_hours_title,et_address_title, et_note_title;
    Group reservation_group_team, reservation_group_place;
    RecyclerView rv_team_joiners;
    private String teamId = "0";
    TextViewWithFont toolbar_title;
    TextView btnBack;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);
        getExtraPlaceData();
        initViews();
        loadPlaceDetails();
    }


    private void getExtraPlaceData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            teamId = getIntent().getExtras().getString("team_id", "0");
        }
    }

    @Override
    public void initViews() {
        tv_user_is_joined_label=findViewById(R.id.tv_user_is_joined_label);
        rv_team_joiners=findViewById(R.id.rv_team_joiners);
        toolbar_title=findViewById(R.id.toolbar_title);
        reservation_group_place = findViewById(R.id.reservation_group_place);
        reservation_group_team = findViewById(R.id.reservation_group_team);
        tv_team_id = findViewById(R.id.tv_team_id);
        tv_team_joiners_empty = findViewById(R.id.tv_team_joiners_empty);
        tv_team_count = findViewById(R.id.tv_team_count);
        et_address_title = findViewById(R.id.et_address_title);
        tv_view_location = findViewById(R.id.tv_view_location);
        tv_place_title = findViewById(R.id.tv_place_title);
        tv_address_title = findViewById(R.id.tv_address_title);
        tv_phone_title = findViewById(R.id.tv_phone_title);
        tv_email_title = findViewById(R.id.tv_email_title);
        tv_note_title = findViewById(R.id.tv_note_title);
        tv_book_now = findViewById(R.id.tv_book_now);
        tv_date = findViewById(R.id.tv_date);
        et_name_title = findViewById(R.id.et_name_title);
        et_phone_title = findViewById(R.id.et_phone_title);
        et_hours_title = findViewById(R.id.et_hours_title);
        et_note_title = findViewById(R.id.et_note_title);
        btnBack = findViewById(R.id.btnBack);
        reservation_group_place.setVisibility(View.GONE);
        reservation_group_team.setVisibility(View.GONE);
        reservation_content = findViewById(R.id.reservation_content);
        tv_team_joiners_empty.setVisibility(View.GONE);
        reservation_content.setVisibility(View.GONE);
        toolbar_title.setText(getResources().getString(R.string.team_details));
        rv_team_joiners.setLayoutManager(new LinearLayoutManager(this));
        rv_team_joiners.setNestedScrollingEnabled(false);

        if (Locale.getDefault().getLanguage().equals("en")) {
            btnBack.setRotation(180);
        }
        btnBack.setOnClickListener((View view) -> super.onBackPressed());
        tv_book_now.setOnClickListener(view -> {
            if (reservation_content.getVisibility() == View.GONE) {
                reservation_content.setVisibility(View.VISIBLE);
            } else {
                confirmJoinTeam();
            }
        });

    }

    private void loadPlaceDetails() {
        progressDialog.show();
        apiManager.getTeamDetails(teamId, "" + Gdata.user_id, new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.hide();
                TeamDetailsResponse placeDetailsResponse = (TeamDetailsResponse) data;
                if (placeDetailsResponse.getTeamDetailsModel() != null) {
                    setTeamDetails(placeDetailsResponse.getTeamDetailsModel());
                }

            }

            @Override
            public void ResponseFail(Object data) {
                progressDialog.hide();
                Toast.makeText(ActivityTeamDetails.this, "" + data, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void setTeamDetails(TeamDetailsModel placeDetails) {
        tv_place_title.setText(placeDetails.getName());
        tv_address_title.setText(placeDetails.getAddress());
        tv_phone_title.setText(placeDetails.getPhone());
        tv_note_title.setText(placeDetails.getNote());
        tv_team_id.setText("#"+placeDetails.getTeamId());
        tv_team_count.setText("("+placeDetails.getNumberOfJoins()+"/"+placeDetails.getNumberOfPlayers()+")");
        if(placeDetails.getJoins() != null && placeDetails.getJoins().size() >0){
            rv_team_joiners.setVisibility(View.VISIBLE);
            tv_team_joiners_empty.setVisibility(View.GONE);
            TeamsMemberAdapter teamsMemberAdapter=new TeamsMemberAdapter(placeDetails.getJoins(), serviceModel -> {

            });
            rv_team_joiners.setAdapter(teamsMemberAdapter);
        }else{
            tv_team_joiners_empty.setVisibility(View.VISIBLE);
            rv_team_joiners.setVisibility(View.GONE);
        }
        placeDetails.setIs_joined("0");
        if(placeDetails.getIs_joined() != null && placeDetails.getIs_joined().equals("1")){
            tv_book_now.setVisibility(View.GONE);
            tv_user_is_joined_label.setVisibility(View.VISIBLE);
        }else{
            tv_book_now.setVisibility(View.VISIBLE);
            tv_user_is_joined_label.setVisibility(View.GONE);
        }

    }

    private void confirmJoinTeam() {
        HashMap<String, String> allFormData = new HashMap<>();
        allFormData.put("user_id",Gdata.user_id);
        allFormData.put("team_id", teamId);
        allFormData.put("name",et_name_title.getText().toString());
        allFormData.put("phone",et_phone_title.getText().toString());
        allFormData.put("note",et_note_title.getText().toString());
        allFormData.put("address",et_address_title.getText().toString());
        Log.d("allFormData",""+new Gson().toJson(allFormData));
        progressDialog.show();
        apiManager.joinTeam(allFormData, new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.hide();
                GeneralModel generalModel= (GeneralModel) data;
                Toast.makeText(ActivityTeamDetails.this, ""+generalModel.getMsg(), Toast.LENGTH_SHORT).show();
                resetReservationForm();
                loadPlaceDetails();

            }

            @Override
            public void ResponseFail(Object data) {
                progressDialog.hide();
                Toast.makeText(ActivityTeamDetails.this, ""+data, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void resetReservationForm() {
        et_phone_title.setText("");
        et_note_title.setText("");
        et_name_title.setText("");
        et_address_title.setText("");
        reservation_content.setVisibility(View.GONE);
    }


}
