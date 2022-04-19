package com.huawei.earthquakenotifier.api;

import com.huawei.earthquakenotifier.model.AccessToken;
import com.huawei.earthquakenotifier.model.NotificationBody;
import com.huawei.earthquakenotifier.model.MultipleResource;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;


public interface APIInterface {

    @GET("/fdsnws/event/1/query?format=geojson")
    Call<MultipleResource> doGetListResources(@Query("starttime") String startTime,
                                              @Query("endtime") String endTime);

    @FormUrlEncoded
    @POST("/oauth2/v3/token")
    Call<AccessToken> getAccessToken(@Field("grant_type") String grantType,
                                     @Field("client_id") int clientId,
                                     @Field("client_secret") String clientSecret);

    @Headers("Content-Type:application/json; charset=UTF-8")
    @POST("v1/105851089/messages:send")
    Call<ResponseBody> sendNotification (
            @Header("Authorization") String Auth,
            @Body NotificationBody notificationMessage
    );
}