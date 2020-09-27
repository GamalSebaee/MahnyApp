package atiaf.mehany.Activity.phase2;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Activity.ClientordertabActivity;
import atiaf.mehany.Activity.SendorderActivity;
import atiaf.mehany.Activity.phase2.adapter.CustomSpinnerAdapter;
import atiaf.mehany.Activity.phase2.adapter.ServicesAdapter;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Country;
import atiaf.mehany.Data.FormInputModel;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.GeneralModel;
import atiaf.mehany.Data.OptionModel;
import atiaf.mehany.Data.ServiceModel;
import atiaf.mehany.Data.ServicesResponse;
import atiaf.mehany.R;
import atiaf.mehany.phase2.remote_data.ApiCallBack;

public class OtherServicesActivity extends BaseActivity {

    LinearLayout formContent,lintechbical,linteacher,linsuccess;
    LayoutInflater inflater;
    Button btn_confirm;
    TextView toolbar_title, btnBack,tv_form_title,sendOrder;
    RecyclerView rvServicesList;
    private String selectedLat = "0", selectedLng = "0";
    ArrayList<Country> cities = new ArrayList<>();
    HashMap<String, String> allFormData = new HashMap<>();
    HashMap<Object, Object> spinnerData = new HashMap<>();
    ArrayList<Country> studies = new ArrayList<>();
    private ServiceModel selectedServiceModel = null;
    Typeface typeface;
    String stage = "", study = "", jobid = "";
    Spinner spinTech,spin,spin1;
    String rt = "1";
    EditText details;
    ArrayList<Country> stages = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_services);
        initViews();
    }


    @Override
    public void initViews() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            selectedLat = getIntent().getExtras().getString("lat", "0");
            selectedLng = getIntent().getExtras().getString("lng", "0");
            String selectedServiceModelValue = getIntent().getExtras().getString("selected_service_model", null);
            if (selectedServiceModelValue != null) {
                selectedServiceModel = new Gson().fromJson(selectedServiceModelValue,ServiceModel.class);
            }

        }

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        toolbar_title = findViewById(R.id.toolbar_title);
        lintechbical = findViewById(R.id.lintechbical);
        linsuccess = findViewById(R.id.linsuccess);
        spinTech =  findViewById(R.id.spintech);
        spin =  findViewById(R.id.spin);
        spin1 =  findViewById(R.id.spin1);
        details =  findViewById(R.id.details);
        sendOrder =  findViewById(R.id.sendOrder);
        linteacher = findViewById(R.id.linteacher);
        btnBack = findViewById(R.id.btnBack);
        formContent = findViewById(R.id.formContent);
        tv_form_title = findViewById(R.id.tv_form_title);
        btn_confirm = findViewById(R.id.btn_confirm);
        rvServicesList = findViewById(R.id.rv_services_list);
        rvServicesList.setNestedScrollingEnabled(false);
        rvServicesList.setLayoutManager(new LinearLayoutManager(this));
        btn_confirm.setOnClickListener(v -> submitOrder());
        if (Locale.getDefault().getLanguage().equals("ar")) {
            typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Cocon.otf");
        } else {
            typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Cocon.otf");
        }
        if (Locale.getDefault().getLanguage().equals("en")) {
            btnBack.setRotation(180);
        }
        btnBack.setOnClickListener(view -> super.onBackPressed());

        btn_confirm.setTypeface(typeface);
        linteacher.setVisibility(View.GONE);
        lintechbical.setVisibility(View.GONE);
        formContent.setVisibility(View.GONE);
        btn_confirm.setVisibility(View.GONE);
        sendOrder.setVisibility(View.GONE);
        if(selectedServiceModel != null){
            tv_form_title.setText(getResources().getString(R.string.enter_form_data));
            clearFormContent();
            toolbar_title.setText(selectedServiceModel.getTitle());
            if(selectedServiceModel.getId() == -1){
                rt = "1";
                linteacher.setVisibility(View.VISIBLE);
                sendOrder.setVisibility(View.VISIBLE);
            }else if(selectedServiceModel.getId() == -2){
                rt = "2";
                lintechbical.setVisibility(View.VISIBLE);
                sendOrder.setVisibility(View.VISIBLE);
            }else{
                formContent.setVisibility(View.VISIBLE);
                btn_confirm.setVisibility(View.VISIBLE);
                setViewForm(selectedServiceModel.getFormInputs());
            }
            getCities();
            getStages();

        }else{
            toolbar_title.setText(getResources().getString(R.string.other_services));
            tv_form_title.setText(getResources().getString(R.string.choose_services));
            getAllServices();
        }

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStudies();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sendOrder.setOnClickListener(view->{
            confirmSendOldRequest();
        });
    }

    private void confirmSendOldRequest() {
        if (rt.equals("1")) {
            if (stage.equals("") || study.equals("")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast t = null;
                        t = Toast.makeText(getApplicationContext(), getString(R.string.tr), Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();

                    }
                });
            } else {
                if (isNetworkAvailable()) {
                    sendstages();
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            }
        } else if (rt.equals("2")) {
            if (jobid.equals("") || details.getText().toString().trim().equals("")) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast t = null;
                        t = Toast.makeText(getApplicationContext(), getString(R.string.tr), Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();

                    }
                });
            } else {
                if (isNetworkAvailable()) {
                    sendLognRequest();
                } else {
                    Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.CENTER, 0, 0);
                    t.show();
                }
            }
        }
    }


    private void clearFormContent() {
        formContent.removeAllViews();
    }

    private void getAllServices() {
        progressDialog.show();
        apiManager.getAllServices(new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.dismiss();
                ServicesResponse servicesResponse = (ServicesResponse) data;
                List<ServiceModel> allServices = servicesResponse.getData();
                ServicesAdapter servicesAdapter = new ServicesAdapter(allServices, serviceModel -> {
                    clearFormContent();
                    selectedServiceModel = serviceModel;
                    btn_confirm.setVisibility(View.VISIBLE);
                    setViewForm(serviceModel.getFormInputs());
                });
                rvServicesList.setAdapter(servicesAdapter);

            }

            @Override
            public void ResponseFail(Object data) {
                progressDialog.dismiss();
                Toast.makeText(OtherServicesActivity.this, "" + data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setViewForm(List<FormInputModel> formInputs) {
        if (formInputs != null && formInputs.size() > 0) {
            for (FormInputModel formInputModel : formInputs) {
                addFormView(formInputModel);
            }
        }
    }

    private void addFormView(FormInputModel formInputModel) {
        addTextViews(formInputModel.getTitle());

        if (formInputModel.getType() != null && formInputModel.getType().toLowerCase()
                .trim().equals("text")) {
            addEditTexts(formInputModel.getName(), 1, InputType.TYPE_CLASS_TEXT);
        } else if (formInputModel.getType() != null && formInputModel.getType().toLowerCase()
                .trim().equals("phone")) {
            addEditTexts(formInputModel.getName(), 1, InputType.TYPE_CLASS_PHONE);
        } else if (formInputModel.getType() != null && formInputModel.getType().toLowerCase()
                .trim().equals("number")) {
            addEditTexts(formInputModel.getName(), 1, InputType.TYPE_NUMBER_FLAG_DECIMAL);
        } else if (formInputModel.getType() != null && formInputModel.getType().toLowerCase()
                .trim().equals("textarea")) {
            addEditTexts(formInputModel.getName(), 5, InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                    InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        } else {
            addSpinnerViews(formInputModel.getName(), formInputModel.getOptions());
        }

    }

    private void addTextViews(String title) {
        TextView textView = new TextView(this);
        textView.setText(title);
        setTextViewAttributes(textView);
        formContent.addView(textView);
    }

    private void addSpinnerViews(Object spinnerTag, List<OptionModel> values) {
        Spinner spinner = new Spinner(this);
        spinner.setTag(spinnerTag);
        setSpinnerAttributes(spinner);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(OtherServicesActivity.this,
                android.R.layout.simple_spinner_item,
                values);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                OptionModel item = adapter.getItem(position);
                if (item != null) {
                    spinnerData.remove(spinnerTag);
                    spinnerData.put(spinnerTag, "" + item.getValue());
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        formContent.addView(spinner);
    }

    private void addEditTexts(Object editTextTag, int numberOfRows, int type) {
        EditText editText = new EditText(this);
        editText.setTag(editTextTag);
        editText.setLines(numberOfRows);
        editText.setInputType(type);
        editText.setBackgroundResource(R.drawable.color);
        if (numberOfRows > 1) {
            editText.setGravity(Gravity.TOP | Gravity.START);
        }
        editText.setPadding(convertDpToPixel(10), convertDpToPixel(5), convertDpToPixel(10),
                convertDpToPixel(10));
        setEditTextAttributes(editText);
        formContent.addView(editText);
        addLineSeperator();
    }


    private void setEditTextAttributes(EditText editText) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );
        editText.setTextColor(Color.BLACK);
        editText.setTypeface(typeface);
        editText.setLayoutParams(params);
    }

    private void setSpinnerAttributes(Spinner spinner) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16),
                0
        );
        spinner.setBackgroundResource(R.drawable.color);
        spinner.setPadding(0, convertDpToPixel(5), 0, convertDpToPixel(5));
        spinner.setLayoutParams(params);
    }


    private void setTextViewAttributes(TextView textView) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(convertDpToPixel(16),
                convertDpToPixel(16),
                convertDpToPixel(16), 0
        );
        textView.setTypeface(typeface);
        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(params);
    }

    //This function to convert DPs to pixels
    private int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    private void addLineSeperator() {
        LinearLayout lineLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0);
        params.setMargins(0, convertDpToPixel(5), 0, convertDpToPixel(5));
        lineLayout.setLayoutParams(params);
        formContent.addView(lineLayout);
    }

    private void submitOrder() {
        if (selectedServiceModel != null) {
            for (int index = 0; index < formContent.getChildCount(); index++) {
                View childView = formContent.getChildAt(index);
                if (childView instanceof EditText) {
                    allFormData.put("" + childView.getTag(), ((EditText) childView).getText().toString());
                } else if (childView instanceof Spinner) {
                    allFormData.put("" + childView.getTag(), "" +
                            spinnerData.get(childView.getTag()));
                }
            }
            allFormData.put(selectedServiceModel.getName(), "" + selectedServiceModel.getId());
            allFormData.put("user_id", Gdata.user_id);
            allFormData.put("order_lat", selectedLat);
            allFormData.put("order_lng", selectedLng);
            Log.d("allFormData", "" + new Gson().toJson(allFormData));
            progressDialog.show();
            apiManager.submitOrder(allFormData, new ApiCallBack() {
                @Override
                public void ResponseSuccess(Object data) {
                    progressDialog.dismiss();
                    GeneralModel generalModel = (GeneralModel) data;
                    Toast.makeText(OtherServicesActivity.this, "" + generalModel.getMsg(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void ResponseFail(Object data) {
                    progressDialog.dismiss();
                    Toast.makeText(OtherServicesActivity.this, "" + data, Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    public void getCities() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "get_jobs", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");
                    cities.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("job_id");
                            countr.title = jo.getString("title");
                            cities.add(countr);
                        }
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.wreg3);
                        cities.add(countr);
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.wreg3);
                        cities.add(countr);
                        Country count = new Country();
                        count.id = "";
                        count.title = getString(R.string.wreg3);
                        cities.add(count);


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(OtherServicesActivity.this, android.R.layout.simple_spinner_dropdown_item, cities) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.spinner, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.image);
                            if (cities.size() > 0) {
                                try {
                                    txtTitle.setText(cities.get(position).title);
                                    jobid = cities.get(position).id;

                                } catch (IndexOutOfBoundsException e) {

                                }


                            }
                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.textspin, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            if (cities.size() > 0) {
                                txtTitle.setText(cities.get(position).title);
                            }
                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you dont display last item. It is used as hint.
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinTech.setAdapter(adapter);
                    spinTech.setSelection(adapter.getCount());

                } catch (JSONException e) {

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    public void getStages() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "get_level", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");
                    stages.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("level_id");
                            countr.title = jo.getString("title");
                            stages.add(countr);
                        }
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder);
                        stages.add(countr);
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder);
                        stages.add(countr);
                        Country count = new Country();
                        count.id = "";
                        count.title = getString(R.string.sendorder);
                        stages.add(count);


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(OtherServicesActivity.this, android.R.layout.simple_spinner_dropdown_item, stages) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.spinner, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.image);
                            if (stages.size() > 0) {
                                try {
                                    txtTitle.setText(stages.get(position).title);
                                    stage = stages.get(position).id;

                                } catch (IndexOutOfBoundsException e) {

                                }


                            }
                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.textspin, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            if (stages.size() > 0) {
                                txtTitle.setText(stages.get(position).title);
                            }
                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you dont display last item. It is used as hint.
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(adapter);
                    spin.setSelection(adapter.getCount());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void getStudies() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "get_subject", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");
                    studies.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("subject_id");
                            countr.title = jo.getString("title");
                            studies.add(countr);
                        }
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder1);
                        studies.add(countr);
                        if (jsonArray.length() == 0) {
                            Country count = new Country();
                            count.id = "";
                            count.title = getString(R.string.sendorder1);
                            studies.add(count);
                        }
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder1);
                        studies.add(countr);
                        Country count = new Country();
                        count.id = "";
                        count.title = getString(R.string.sendorder1);
                        studies.add(count);


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(OtherServicesActivity.this, android.R.layout.simple_spinner_dropdown_item, studies) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.spinner, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.image);
                            if (studies.size() > 0) {
                                try {
                                    txtTitle.setText(studies.get(position).title);
                                    study = studies.get(position).id;

                                } catch (IndexOutOfBoundsException e) {

                                }


                            }
                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.textspin, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            if (studies.size() > 0) {
                                txtTitle.setText(studies.get(position).title);
                            }
                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you dont display last item. It is used as hint.
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin1.setAdapter(adapter);
                    spin1.setSelection(adapter.getCount());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("lang", Locale.getDefault().getLanguage());
                params.put("subject_id", stage);
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void sendstages() {
        final ProgressDialog progressDialog = ProgressDialog.show(OtherServicesActivity.this, getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "send_request_teacher", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");

                    if (status) {

                        linsuccess.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            linsuccess.setVisibility(View.GONE);
                        }, 4000);
                        Handler handle = new Handler();
                        handle.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getApplicationContext(), ClientordertabActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }, 24000);
                    } else {

                        String msg = obj.getString("msg");
                        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        Log.e("response ", msg + "");

                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", Gdata.user_id);
                params.put("subject_id", study);
                params.put("lat", selectedLat);
                params.put("lng", selectedLng);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    public void sendLognRequest() {
        final ProgressDialog progressDialog = ProgressDialog.show(OtherServicesActivity.this, getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url + "send_request_worker", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");

                    if (status) {
                        linsuccess.setVisibility(View.VISIBLE);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                linsuccess.setVisibility(View.GONE);
                            }
                        }, 4000);
                        Handler handle = new Handler();
                        handle.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getApplicationContext(), ClientordertabActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }, 24000);
                    } else {

                        String msg = obj.getString("msg");
                        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        Log.e("response ", msg + "");

                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", Gdata.user_id);
                params.put("job_id", jobid);
                params.put("details", details.getText().toString().trim());
                params.put("lat", selectedLat);
                params.put("lng", selectedLng);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}
