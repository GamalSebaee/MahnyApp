package atiaf.mehany.phase2.body;

import com.google.gson.annotations.SerializedName;

class BookingPlaceBody extends CreateTeamBody {
    @SerializedName("place_id")
    private String place_id;
    @SerializedName("start_at")
    private String start_at;
    @SerializedName("hours")
    private String hours;

}
