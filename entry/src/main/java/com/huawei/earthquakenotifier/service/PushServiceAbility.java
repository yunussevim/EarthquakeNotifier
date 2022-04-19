package com.huawei.earthquakenotifier.service;

import com.huawei.hms.push.common.ApiException;
import com.huawei.hms.push.ohos.HmsInstanceId;
import com.huawei.hms.push.ohos.HmsMessageService;
import com.huawei.hms.push.ohos.ZBaseException;
import com.huawei.hms.push.ohos.ZRemoteMessage;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.rpc.IRemoteObject;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class PushServiceAbility extends HmsMessageService {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0xD001100, "MY_TAG");

    @Override
    public void onStart(Intent intent) {
        HiLog.error(LABEL_LOG, "PushServiceAbility::onStart");
        super.onStart(intent);
    }

    @Override
    public void onBackground() {
        super.onBackground();
        HiLog.info(LABEL_LOG, "PushServiceAbility::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.info(LABEL_LOG, "PushServiceAbility::onStop");
    }

    @Override
    public void onCommand(Intent intent, boolean restart, int startId) {
    }

    @Override
    public IRemoteObject onConnect(Intent intent) {
        return null;
    }

    @Override
    public void onDisconnect(Intent intent) {
    }

    @Override
    // Obtain a push token.
    public void onNewToken(String token) {
        HiLog.info(LABEL_LOG, "onNewToken called, token:%{public}s", token);
    }

    @Override
    // An error code is recorded when the push token fails to be obtained.
    public void onTokenError(Exception exception) {
        HiLog.error(LABEL_LOG, "get onNewtoken error, error code is %{public}d", ((ZBaseException)exception).getErrorCode());
    }

    @Override
    public void onMessageReceived(ZRemoteMessage message) {
        // The fields in the message content are recorded.
        HiLog.info(LABEL_LOG, "get token, %{public}s", message.getToken());
        HiLog.info(LABEL_LOG, "get data, %{public}s", message.getData());

        ZRemoteMessage.Notification notification = message.getNotification();
        if (notification != null) {
            HiLog.info(LABEL_LOG, "get title, %{public}s", notification.getTitle());
            HiLog.info(LABEL_LOG, "get body, %{public}s", notification.getBody());
        }
    }
}