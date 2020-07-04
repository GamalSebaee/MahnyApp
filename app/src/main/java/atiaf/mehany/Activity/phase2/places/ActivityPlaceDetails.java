package atiaf.mehany.Activity.phase2.places;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import atiaf.mehany.Activity.phase2.BaseActivity;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.GeneralModel;
import atiaf.mehany.R;
import atiaf.mehany.phase2.remote_data.ApiCallBack;
import atiaf.mehany.phase2.response.PlaceDetailsModel;
import atiaf.mehany.phase2.response.PlaceDetailsResponse;

public class ActivityPlaceDetails extends BaseActivity {
    TextViewWithFont tv_place_title,tv_date, tv_address_title, tv_phone_title,
            tv_email_title, tv_note_title,tv_view_location, tv_book_now;
    View reservation_content;
    EditText et_name_title, et_phone_title, et_hours_title,et_address_title, et_note_title;
    Group reservation_group_team, reservation_group_place;
    private String placeId = "0";
    private String placeLat, placeLng;
    TextViewWithFont toolbar_title;
    TextView btnBack;
    final Calendar myCalendar = Calendar.getInstance();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        getExtraPlaceData();
        initViews();
        loadPlaceDetails();
    }


    private void getExtraPlaceData() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            placeId = getIntent().getExtras().getString("place_id", "0");
        }
    }

    @Override
    public void initViews() {
        toolbar_title=findViewById(R.id.toolbar_title);
        reservation_group_place = findViewById(R.id.reservation_group_place);
        reservation_group_team = findViewById(R.id.reservation_group_team);
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
        reservation_group_place.setVisibility(View.VISIBLE);
        reservation_group_team.setVisibility(View.GONE);
        reservation_content = findViewById(R.id.reservation_content);
        reservation_content.setVisibility(View.GONE);
        toolbar_title.setText(getResources().getString(R.string.stadium_details));

        if (Locale.getDefault().getLanguage().equals("en")) {
            btnBack.setRotation(180);
        }
        btnBack.setOnClickListener((View view) -> super.onBackPressed());
        tv_book_now.setOnClickListener(view -> {
            if (reservation_content.getVisibility() == View.GONE) {
                reservation_content.setVisibility(View.VISIBLE);
            } else {
                confirmReservation();
            }
        });
        tv_date.setOnClickListener(view -> {
            new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        tv_view_location.setOnClickListener(view -> navigateNoPlaceLocation());
    }

    private void loadPlaceDetails() {
        progressDialog.show();
        apiManager.getPlaceDetails(placeId, "" + Gdata.user_id, new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.hide();
                PlaceDetailsResponse placeDetailsResponse = (PlaceDetailsResponse) data;
                if (placeDetailsResponse.getPlaceDetails() != null) {
                    setPlaceDetails(placeDetailsResponse.getPlaceDetails());
                }

            }

            @Override
            public void ResponseFail(Object data) {
                progressDialog.hide();
                Toast.makeText(ActivityPlaceDetails.this, "" + data, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void setPlaceDetails(PlaceDetailsModel placeDetails) {
        tv_place_title.setText(placeDetails.getTitle());
        tv_address_title.setText(placeDetails.getAddress());
        tv_phone_title.setText(placeDetails.getPhone());
        tv_email_title.setText(placeDetails.getEmail());
        tv_note_title.setText(placeDetails.getNote());
        placeLat = placeDetails.getLat();
        placeLng = placeDetails.getLng();
    }

    private void navigateNoPlaceLocation() {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr="+placeLat+","+placeLng,
                12f, 2f, tv_place_title.getText()+" "+getString(R.string.stadium_here));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(this, "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void confirmReservation() {
        HashMap<String, String> allFormData = new HashMap<>();
        allFormData.put("user_id",Gdata.user_id);
        allFormData.put("place_id",placeId);
        allFormData.put("name",et_name_title.getText().toString());
        allFormData.put("phone",et_phone_title.getText().toString());
        allFormData.put("note",et_note_title.getText().toString());
        allFormData.put("address",et_address_title.getText().toString());
        allFormData.put("start_at",selectedDateTime);
        allFormData.put("hours",et_hours_title.getText().toString());
        Log.d("allFormData",""+new Gson().toJson(allFormData));
        progressDialog.show();
        apiManager.bookingPlace(allFormData, new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.hide();
                GeneralModel generalModel= (GeneralModel) data;
                Toast.makeText(ActivityPlaceDetails.this, ""+generalModel.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void ResponseFail(Object data) {
                progressDialog.hide();
                Toast.makeText(ActivityPlaceDetails.this, ""+data, Toast.LENGTH_SHORT).show();
            }
        });

    }
    private String selectedDateTime=null;
    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        selectedDateTime=year+"-"+monthOfYear+"-"+dayOfMonth;
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker= new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
            selectedDateTime = selectedDateTime+" "+selectedHour + ":" + selectedMinute+":00";
            tv_date.setText(selectedDateTime);
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    };



}
