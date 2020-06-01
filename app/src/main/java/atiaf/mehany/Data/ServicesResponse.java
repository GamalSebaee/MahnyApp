package atiaf.mehany.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServicesResponse extends GeneralModel {
    @SerializedName("data")
    @Expose
    private List<ServiceModel> data = null;

    public List<ServiceModel> getData() {
        return data;
    }

    public void setData(List<ServiceModel> data) {
        this.data = data;
    }
}
