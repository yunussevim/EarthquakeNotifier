package com.huawei.earthquakenotifier.service;

import com.huawei.earthquakenotifier.MyApplication;
import com.huawei.earthquakenotifier.model.MultipleResource;
import com.huawei.earthquakenotifier.provider.EarthquakeApiProvider;
import com.huawei.earthquakenotifier.provider.NotificationSender;
import com.huawei.earthquakenotifier.slice.MainAbilitySlice;
import com.huawei.hms.maps.harmony.model.LatLng;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.global.icu.text.AlphabeticIndex;
import ohos.rpc.IRemoteObject;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CheckEarthquakeServiceAbility extends Ability {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0xD001100, "Demo");
    private EarthquakeApiProvider earthquakeApiProvider;
    private NotificationSender notificationSender;

    private void checkEarthquakes() throws InterruptedException {
        HiLog.debug(LABEL_LOG,"checked");
        List<MultipleResource.Feature> oldEarthquakes = MyApplication.earthquakes;
        HiLog.debug(LABEL_LOG,MyApplication.earthquakes.get(0).properties.detail);
        notificationSender = new NotificationSender(this);
        earthquakeApiProvider = new EarthquakeApiProvider();
        earthquakeApiProvider.callApiRetrofit();
        Thread.sleep(5000);
        HiLog.debug(LABEL_LOG,MyApplication.earthquakes.get(0).properties.detail);
        List<MultipleResource.Feature> newEarthquakes = MyApplication.earthquakes;
        for(MultipleResource.Feature detected : newEarthquakes){
            boolean isNewEarthquake = true;
            for(MultipleResource.Feature old : oldEarthquakes){
                if(detected.id.equals(old.id)){
                    isNewEarthquake = false;
                    break;
                }
            }
            if(isNewEarthquake){
                List<Double> coordinates = detected.geometry.coordinates;
                int distance = earthquakeApiProvider.calcDistance(MyApplication.lat,coordinates.get(1) ,MyApplication.lon ,coordinates.get(0));
                double mag = detected.properties.mag;
                if(distance<=MyApplication.distance && mag >= MyApplication.magnitude){
                    String title = "Earthquake Detected!";
                    String message = mag+": "+ detected.properties.place;
                    notificationSender.sendNotification(title,message);
                }
            }
        }
        HiLog.debug(LABEL_LOG,"here");
        Thread.sleep(20*1000);
        checkEarthquakes();
    }

    @Override
    public void onStart(Intent intent) {
        HiLog.error(LABEL_LOG, "CheckEarthquakeServiceAbility::onStart");
        super.onStart(intent);
        try {
            checkEarthquakes();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackground() {
        super.onBackground();
        HiLog.info(LABEL_LOG, "CheckEarthquakeServiceAbility::onBackground");
    }

    @Override
    public void onStop() {
        super.onStop();
        HiLog.info(LABEL_LOG, "CheckEarthquakeServiceAbility::onStop");
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
}