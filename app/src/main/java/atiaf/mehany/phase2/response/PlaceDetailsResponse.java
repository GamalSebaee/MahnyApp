package atiaf.mehany.phase2.response;

import com.google.gson.annotations.SerializedName;

import atiaf.mehany.Data.GeneralModel;

public class PlaceDetailsResponse extends GeneralModel {
    @SerializedName("data")
    private PlaceDetailsModel placeDetails=null;

    public PlaceDetailsModel getPlaceDetails() {
        return placeDetails;
    }

    public void setPlaceDetails(PlaceDetailsModel placeDetails) {
        this.placeDetails = placeDetails;
    }
}
