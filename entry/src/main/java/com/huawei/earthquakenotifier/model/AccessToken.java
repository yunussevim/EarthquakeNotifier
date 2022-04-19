package com.huawei.earthquakenotifier.model;

import com.google.gson.annotations.SerializedName;

public class AccessToken {

    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("expires_in")
    public int expiresIn;
    @SerializedName("token_type")
    public String tokenType;
}
