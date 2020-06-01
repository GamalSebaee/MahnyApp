package atiaf.mehany.phase2;

import java.util.concurrent.TimeUnit;

import atiaf.mehany.phase2.remote_data.ApiClient;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Phase2ApiBuilder {

    private static OkHttpClient.Builder httpClient(){
       return new OkHttpClient.Builder()
                .callTimeout(120000, TimeUnit.SECONDS)
                .connectTimeout(120000, TimeUnit.SECONDS)
                .writeTimeout(120000, TimeUnit.SECONDS)
                .readTimeout(120000, TimeUnit.SECONDS);
    }

    private static Retrofit.Builder builder() {
        String API_BASE_URL = "https://mhny.mtgofa.com/";
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
    }


    private static Retrofit retrofit() {
        return builder().client(httpClient().build()).build();
    }


    static ApiClient getApiClient(){
        return retrofit().create(ApiClient.class);
    }


}
