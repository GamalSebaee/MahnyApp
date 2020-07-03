package atiaf.mehany.phase2.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import atiaf.mehany.Data.GeneralModel;

public class TeamsResponse extends GeneralModel {
    @SerializedName("data")
    private List<TeamDetailsModel> allTeams = null ;

    public List<TeamDetailsModel> getAllTeams() {
        return allTeams;
    }

    public void setAllTeams(List<TeamDetailsModel> allTeams) {
        this.allTeams = allTeams;
    }
}
