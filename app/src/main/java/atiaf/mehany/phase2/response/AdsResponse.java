package atiaf.mehany.phase2.response;

import com.google.gson.annotations.SerializedName;

import atiaf.mehany.Data.GeneralModel;

public class AdsResponse extends GeneralModel {
    @SerializedName("data")
    private AdsData adsData;

    public AdsData getAdsData() {
        return adsData;
    }

    public void setAdsData(AdsData adsData) {
        this.adsData = adsData;
    }
}
