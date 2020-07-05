package atiaf.mehany.Activity.phase2.teams;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;

import atiaf.mehany.Activity.phase2.BaseActivity;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.GeneralModel;
import atiaf.mehany.R;
import atiaf.mehany.phase2.remote_data.ApiCallBack;

public class ActivityCreateTeam extends BaseActivity {
    TextViewWithFont tv_book_now;
    EditText et_name_title, et_phone_title, et_number_players_title, et_address_title, et_note_title;
    Group reservation_group_team, reservation_group_place;
    TextViewWithFont toolbar_title;
    TextView btnBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        initViews();
    }

    @Override
    public void initViews() {
        toolbar_title = findViewById(R.id.toolbar_title);
        reservation_group_place = findViewById(R.id.reservation_group_place);
        reservation_group_team = findViewById(R.id.reservation_group_team);
        et_address_title = findViewById(R.id.et_address_title);
        tv_book_now = findViewById(R.id.tv_book_now);
        et_name_title = findViewById(R.id.et_name_title);
        et_phone_title = findViewById(R.id.et_phone_title);
        et_number_players_title = findViewById(R.id.et_number_players_title);
        et_note_title = findViewById(R.id.et_note_title);
        btnBack = findViewById(R.id.btnBack);
        reservation_group_place.setVisibility(View.GONE);
        reservation_group_team.setVisibility(View.VISIBLE);
        toolbar_title.setText(getResources().getString(R.string.create_team));

        if (Locale.getDefault().getLanguage().equals("en")) {
            btnBack.setRotation(180);
        }
        btnBack.setOnClickListener((View view) -> super.onBackPressed());
        tv_book_now.setOnClickListener(view -> {
            confirmCreateTeam();
        });

    }


    private void confirmCreateTeam() {
        HashMap<String, String> allFormData = new HashMap<>();
        allFormData.put("user_id", Gdata.user_id);
        allFormData.put("number_of_players", et_number_players_title.getText().toString());
        allFormData.put("name", et_name_title.getText().toString());
        allFormData.put("phone", et_phone_title.getText().toString());
        allFormData.put("note", et_note_title.getText().toString());
        allFormData.put("address", et_address_title.getText().toString());
        Log.d("allFormData", "" + new Gson().toJson(allFormData));
        progressDialog.show();
        apiManager.createTeam(allFormData, new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.hide();
                GeneralModel generalModel = (GeneralModel) data;
                Toast.makeText(ActivityCreateTeam.this, "" + generalModel.getMsg(), Toast.LENGTH_SHORT).show();
                resetReservationForm();
            }

            @Override
            public void ResponseFail(Object data) {
                progressDialog.hide();
                Toast.makeText(ActivityCreateTeam.this, "" + data, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void resetReservationForm() {
        et_phone_title.setText("");
        et_note_title.setText("");
        et_name_title.setText("");
        et_address_title.setText("");
        setResult(RESULT_OK);
        finish();
    }


}
