package atiaf.mehany.phase2.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import atiaf.mehany.Data.GeneralModel;

public class TypesResponse extends GeneralModel {
    @SerializedName("data")
    private List<TypeModel> allTypes=null;

    public List<TypeModel> getAllTypes() {
        return allTypes;
    }

    public void setAllTypes(List<TypeModel> allTypes) {
        this.allTypes = allTypes;
    }
}
