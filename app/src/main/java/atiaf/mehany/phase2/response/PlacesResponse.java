package atiaf.mehany.phase2.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import atiaf.mehany.Data.GeneralModel;

public class PlacesResponse extends GeneralModel {
    @SerializedName("data")
    private List<PlaceDetailsModel> allPlaces = null ;

    public List<PlaceDetailsModel> getAllPlaces() {
        return allPlaces;
    }

    public void setAllPlaces(List<PlaceDetailsModel> allPlaces) {
        this.allPlaces = allPlaces;
    }
}
