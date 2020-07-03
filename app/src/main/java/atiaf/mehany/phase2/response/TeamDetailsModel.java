package atiaf.mehany.phase2.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamDetailsModel {
    @SerializedName("team_id")
    @Expose
    private String teamId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("number_of_players")
    @Expose
    private String numberOfPlayers;
    @SerializedName("number_of_joins")
    @Expose
    private String numberOfJoins;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("stop")
    @Expose
    private String stop;
    @SerializedName("create_dateTime")
    @Expose
    private String createDateTime;
    @SerializedName("is_owner")
    @Expose
    private String is_owner;
    @SerializedName("is_joined")
    @Expose
    private String is_joined;

    @SerializedName("joins")
    @Expose
    private List<JoinModel> joins=null;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(String numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getNumberOfJoins() {
        return numberOfJoins;
    }

    public void setNumberOfJoins(String numberOfJoins) {
        this.numberOfJoins = numberOfJoins;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getIs_owner() {
        return is_owner;
    }

    public void setIs_owner(String is_owner) {
        this.is_owner = is_owner;
    }

    public String getIs_joined() {
        return is_joined;
    }

    public void setIs_joined(String is_joined) {
        this.is_joined = is_joined;
    }

    public List<JoinModel> getJoins() {
        return joins;
    }

    public void setJoins(List<JoinModel> joins) {
        this.joins = joins;
    }
}
