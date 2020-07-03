package atiaf.mehany.phase2.remote_data;


import java.util.HashMap;

import atiaf.mehany.Data.GeneralModel;
import atiaf.mehany.Data.ServicesResponse;
import atiaf.mehany.phase2.response.AdsResponse;
import atiaf.mehany.phase2.response.LoginResponse;
import atiaf.mehany.phase2.response.PlaceDetailsResponse;
import atiaf.mehany.phase2.response.PlacesResponse;
import atiaf.mehany.phase2.response.TeamDetailsResponse;
import atiaf.mehany.phase2.response.TeamsResponse;
import atiaf.mehany.phase2.response.TypesResponse;
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

    @GET("API/Main/places")
    Call<PlacesResponse> getAllPlaces(@Query("lang") String lang);

    @GET("API/Main/teams")
    Call<TeamsResponse> getAllTeams(@Query("lang") String lang);

    @GET("API/Main/place")
    Call<PlaceDetailsResponse> getPlaceDetails(@Query("place_id") String place_id,
                                               @Query("user_id") String user_id,
                                               @Query("lang") String lang);
    @GET("API/Main/team")
    Call<TeamDetailsResponse> getTeamDetails(@Query("team_id") String place_id,
                                             @Query("user_id") String user_id,
                                             @Query("lang") String lang);

    @GET("API/Main/ads")
    Call<AdsResponse> getAllAds(@Query("lang") String lang);

    @GET("API/Main/types")
    Call<TypesResponse> getAllTypes(@Query("lang") String lang);

    @FormUrlEncoded
    @POST("API/Auth/sign_up")
    Call<GeneralModel> createNewAccount(@Query("lang") String lang, @FieldMap HashMap<String,String> orderBody);

    @FormUrlEncoded
    @POST("API/Main/submitOrder")
    Call<GeneralModel> submitOrder(@Query("lang") String lang, @FieldMap HashMap<String,String> orderBody);

    @FormUrlEncoded
    @POST("API/Main/createTeam")
    Call<GeneralModel> createTeam(@Query("lang") String lang, @FieldMap HashMap<String,String> orderBody);

    @FormUrlEncoded
    @POST("API/Main/bookingPlace")
    Call<GeneralModel> bookingPlace(@Query("lang") String lang, @FieldMap HashMap<String,String> orderBody);

    @FormUrlEncoded
    @POST("API/Auth/sign_in")
    Call<LoginResponse> loginToApp(@Query("lang") String lang, @Field("text") String userName, @Field("password") String userPassword);

}
