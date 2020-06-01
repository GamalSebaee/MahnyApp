package atiaf.mehany.phase2.response;

import com.google.gson.annotations.SerializedName;

import atiaf.mehany.Data.GeneralModel;

public class LoginResponse extends GeneralModel {
    @SerializedName("data")
    private LoginBody loginBody;

    public LoginBody getLoginBody() {
        return loginBody;
    }

    public void setLoginBody(LoginBody loginBody) {
        this.loginBody = loginBody;
    }
}
