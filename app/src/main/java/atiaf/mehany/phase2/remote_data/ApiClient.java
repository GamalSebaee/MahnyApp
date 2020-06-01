package atiaf.mehany.phase2.remote_data;


import java.util.HashMap;

import atiaf.mehany.Data.GeneralModel;
import atiaf.mehany.Data.ServicesResponse;
import atiaf.mehany.phase2.response.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiClient {
    @GET("API/Main/services")
    Call<ServicesResponse> getAllServices(@Query("lang") String lang);

    @FormUrlEncoded
    @POST("API/Main/submitOrder")
    Call<GeneralModel> submitOrder(@Query("lang") String lang, @FieldMap HashMap<String,String> orderBody);

    @FormUrlEncoded
    @POST("API/Auth/sign_in")
    Call<LoginResponse> loginToApp(@Query("lang") String lang, @Field("text") String userName, @Field("password") String userPassword);

}
