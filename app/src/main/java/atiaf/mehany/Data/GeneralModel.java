package atiaf.mehany.Data;

import com.google.gson.annotations.SerializedName;

public class GeneralModel {
    @SerializedName("Success")
    public boolean success;
    @SerializedName("msg")
    public String msg;
    @SerializedName("error")
    public String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
