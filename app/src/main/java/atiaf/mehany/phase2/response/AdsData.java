package atiaf.mehany.phase2.response;

import com.google.gson.annotations.SerializedName;

public class AdsData {
    @SerializedName("id")
    private String id;
    @SerializedName("type")
    private String type;
    @SerializedName("body")
    private String body;
    @SerializedName("active")
    private String active;
    @SerializedName("stop")
    private String stop;
    @SerializedName("create_dateTime")
    private String create_dateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getCreate_dateTime() {
        return create_dateTime;
    }

    public void setCreate_dateTime(String create_dateTime) {
        this.create_dateTime = create_dateTime;
    }
}
