package atiaf.mehany.phase2.body;

import com.google.gson.annotations.SerializedName;

public class UserLoginBody {
    @SerializedName("text")
    private String loginText;
    @SerializedName("password")
    private String loginPassword;

    public String getLoginText() {
        return loginText;
    }

    public void setLoginText(String loginText) {
        this.loginText = loginText;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}
