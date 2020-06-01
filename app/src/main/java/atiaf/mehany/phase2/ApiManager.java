package atiaf.mehany.phase2;

import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.GeneralModel;
import atiaf.mehany.Data.ServicesResponse;
import atiaf.mehany.phase2.body.UserLoginBody;
import atiaf.mehany.phase2.remote_data.ApiCallBack;
import atiaf.mehany.phase2.remote_data.ApiClient;
import atiaf.mehany.phase2.response.LoginBody;
import atiaf.mehany.phase2.response.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class ApiManager {
    private ApiClient apiClient;
    public ApiManager(){
        apiClient= Phase2ApiBuilder.getApiClient();
    }

   public void getAllServices(final ApiCallBack apiCallBack){
        apiClient.getAllServices(Gdata.getAppLang()).enqueue(new Callback<ServicesResponse>() {
            @Override
            public void onResponse(Call<ServicesResponse> call, Response<ServicesResponse> response) {
           GeneralModel generalModel=response.body();
                if(generalModel != null && generalModel.success){
                    apiCallBack.ResponseSuccess(response.body());
                }else{
                    apiCallBack.ResponseFail(Objects.requireNonNull(generalModel).getError());
                }

            }

            @Override
            public void onFailure(Call<ServicesResponse> call, Throwable t) {
                apiCallBack.ResponseFail(t.getMessage());
            }
        });
    }

   public void submitOrder(HashMap<String,String> orderBody,final ApiCallBack apiCallBack){
        apiClient.submitOrder(Gdata.getAppLang(),orderBody).enqueue(new Callback<GeneralModel>() {
            @Override
            public void onResponse(Call<GeneralModel> call, Response<GeneralModel> response) {
                GeneralModel generalModel=response.body();
                if(generalModel != null && generalModel.success){
                    apiCallBack.ResponseSuccess(response.body());
                }else{
                    apiCallBack.ResponseFail(Objects.requireNonNull(generalModel).getError());
                }

            }

            @Override
            public void onFailure(Call<GeneralModel> call, Throwable t) {
                apiCallBack.ResponseFail(t.getMessage());
            }
        });
    }

    public void loginToApp(UserLoginBody loginBody, final ApiCallBack apiCallBack){
        Log.d("loginBody",""+new Gson().toJson(loginBody));
        apiClient.loginToApp(Gdata.getAppLang(),loginBody.getLoginText(),loginBody.getLoginPassword()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse generalModel=response.body();
                if(generalModel != null && generalModel.success){
                    apiCallBack.ResponseSuccess(response.body());
                }else{
                    String errorMsg;
                    if(generalModel != null && generalModel.getMsg() != null){
                        errorMsg = generalModel.getMsg();
                    }else if(generalModel != null && generalModel.getError() != null){
                        errorMsg = generalModel.getError();
                    }else{

                        if (Locale.getDefault().getLanguage().equals("ar")) {
                            errorMsg = "تم ارسال كلمه السر على الايميل";
                        } else {
                            errorMsg = "Password has been sent to your email";
                        }
                    }
                    apiCallBack.ResponseFail(errorMsg);
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                apiCallBack.ResponseFail(t.getMessage());
            }
        });
    }

}
