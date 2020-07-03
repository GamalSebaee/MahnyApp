package atiaf.mehany.phase2.body;

import com.google.gson.annotations.SerializedName;

public class CreateTeamBody {
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("address")
    private String address;
    @SerializedName("number_of_players")
    private String number_of_players;
    @SerializedName("note")
    private String note;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber_of_players() {
        return number_of_players;
    }

    public void setNumber_of_players(String number_of_players) {
        this.number_of_players = number_of_players;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
