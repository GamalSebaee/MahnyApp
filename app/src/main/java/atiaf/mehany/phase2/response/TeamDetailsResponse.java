package atiaf.mehany.phase2.response;

import com.google.gson.annotations.SerializedName;

import atiaf.mehany.Data.GeneralModel;

public class TeamDetailsResponse extends GeneralModel {
    @SerializedName("data")
    private TeamDetailsModel teamDetailsModel=null;

    public TeamDetailsModel getTeamDetailsModel() {
        return teamDetailsModel;
    }

    public void setTeamDetailsModel(TeamDetailsModel teamDetailsModel) {
        this.teamDetailsModel = teamDetailsModel;
    }
}
