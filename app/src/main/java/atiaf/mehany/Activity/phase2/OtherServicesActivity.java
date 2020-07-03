package atiaf.mehany.Activity.phase2;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import atiaf.mehany.Activity.phase2.adapter.CustomSpinnerAdapter;
import atiaf.mehany.Activity.phase2.adapter.ServicesAdapter;
import atiaf.mehany.Data.FormInputModel;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.GeneralModel;
import atiaf.mehany.Data.OptionModel;
import atiaf.mehany.Data.ServiceModel;
import atiaf.mehany.Data.ServicesResponse;
import atiaf.mehany.R;
import atiaf.mehany.phase2.remote_data.ApiCallBack;

public class OtherServicesActivity extends BaseActivity {

    LinearLayout formContent;
    LayoutInflater inflater;
    Button btn_confirm;
    TextView toolbar_title,btnBack;
    RecyclerView rvServicesList;
    private String selectedLat="0",selectedLng="0";

    HashMap<String, String> allFormData = new HashMap<>();
    HashMap<Object, Object> spinnerData = new HashMap<>();
    private ServiceModel selectedServiceModel = null;
    Typeface typeface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_services);
        initViews();
    }


    @Override
    public void initViews() {
        if(getIntent() != null&& getIntent().getExtras() != null){
            selectedLat = getIntent().getExtras().getString("lat","0");
            selectedLng = getIntent().getExtras().getString("lng","0");
        }

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        toolbar_title = findViewById(R.id.toolbar_title);
        btnBack = findViewById(R.id.btnBack);
        formContent = findViewById(R.id.formContent);
        btn_confirm = findViewById(R.id.btn_confirm);
        rvServicesList = findViewById(R.id.rv_services_list);
        rvServicesList.setNestedScrollingEnabled(false);
        rvServicesList.setLayoutManager(new LinearLayoutManager(this));
        btn_confirm.setOnClickListener(v -> submitOrder());
        if (Locale.getDefault().getLanguage().equals("ar")) {
            typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Cocon.otf");
        }else {
            typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Cocon.otf");
        }
        if (Locale.getDefault().getLanguage().equals("en")) {
            btnBack.setRotation(180);
        }
        btnBack.setOnClickListener( view-> super.onBackPressed());
        toolbar_title.setText(getResources().getString(R.string.other_services));
        btn_confirm.setTypeface(typeface);
        getAllServices();
    }



    private void clearFormContent(){
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
                    selectedServiceModel=serviceModel;
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
            editText.setPadding(convertDpToPixel(10), convertDpToPixel(5), convertDpToPixel(10),
                    convertDpToPixel(10));
        } else {

            editText.setPadding(convertDpToPixel(10), convertDpToPixel(5), convertDpToPixel(10),
                    convertDpToPixel(10));
        }
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
        if(selectedServiceModel != null){
            for (int index = 0; index < formContent.getChildCount(); index++) {
                View childView = formContent.getChildAt(index);
                if (childView instanceof EditText) {
                    allFormData.put(""+childView.getTag(), ((EditText) childView).getText().toString());
                } else if (childView instanceof Spinner) {
                    allFormData.put(""+childView.getTag(),""+
                            spinnerData.get(childView.getTag()));
                }
            }
            allFormData.put(selectedServiceModel.getName(),""+selectedServiceModel.getId());
            allFormData.put("user_id",Gdata.user_id);
            allFormData.put("order_lat",selectedLat);
            allFormData.put("order_lng",selectedLng);
            Log.d("allFormData", "" + new Gson().toJson(allFormData));
            progressDialog.show();
            apiManager.submitOrder(allFormData, new ApiCallBack() {
                @Override
                public void ResponseSuccess(Object data) {
                    progressDialog.dismiss();
                    GeneralModel generalModel= (GeneralModel) data;
                    Toast.makeText(OtherServicesActivity.this, ""+generalModel.getMsg(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void ResponseFail(Object data) {
                    progressDialog.dismiss();
                    Toast.makeText(OtherServicesActivity.this, ""+data, Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

}
