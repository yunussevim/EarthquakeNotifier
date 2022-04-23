package com.huawei.earthquakenotifier.provider;

import com.huawei.earthquakenotifier.api.APIClient;
import com.huawei.earthquakenotifier.api.APIInterface;
import com.huawei.earthquakenotifier.model.AccessToken;
import com.huawei.earthquakenotifier.model.NotificationBody;
import com.huawei.hms.push.common.ApiException;
import com.huawei.hms.push.ohos.HmsInstanceId;
import ohos.aafwk.ability.Ability;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationSender {

    private String accessToken;
    private String pushToken;
    private final Ability context;

    public NotificationSender(Ability context) {
        this.context = context;
        callAuthRetrofit();
        getPushToken();
    }

    public void sendNotification(String title, String message){
        APIInterface apiInterface = APIClient.getPushClient().create(APIInterface.class);
        NotificationBody notificationMessage = new NotificationBody.Builder(
                title, message, pushToken )
                .build();
        Call<ResponseBody> call = apiInterface.sendNotification(
                "Bearer " +accessToken,
                notificationMessage
        );
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void callAuthRetrofit() {
        APIInterface authInterface = APIClient.getAuthClient().create(APIInterface.class);
        String grantType = "client_credentials";
        int clientId = 0; // enter client id
        String clientSecret = "enter client secret";
        Call<AccessToken> call = authInterface.getAccessToken(grantType,clientId,clientSecret);// map the Model class to the response we use:
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                AccessToken resource = response.body();
                if(resource!=null){
                    String tempToken = resource.accessToken;
                    accessToken = tempToken.replace("\\","");
                    }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t)
            {
                call.cancel();
            }
        });
    }

    private void getPushToken() {
        // Create a thread.
        new Thread("getToken") {
            @Override
            public void run() {
                try {
                    // Obtain the value of client/app_id from the agconnect-services.json file.
                    String appId = "enter App id";
                    // Set tokenScope to HCM.
                    String tokenScope = "HCM";
                    // Obtain a push token.
                    pushToken = HmsInstanceId.getInstance(context.getAbilityPackage(), context).getToken(appId, tokenScope);
                } catch (ApiException e) {
                    // An error code is recorded when the push token fails to be obtained.
                }
            }
        }.start();
    }
}
