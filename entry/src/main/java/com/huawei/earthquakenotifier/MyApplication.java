package com.huawei.earthquakenotifier;

import com.huawei.earthquakenotifier.model.MultipleResource;
import ohos.aafwk.ability.AbilityPackage;

import java.util.List;

public class MyApplication extends AbilityPackage {

    public static int distance = 0;
    public static int magnitude = 0;
    public static boolean firstLaunch = true;
    public static List<MultipleResource.Feature> earthquakes;
    public static double lat = 38.9637;
    public static double lon = 35.2433;
    public static boolean isNotificationsEnabled = false;
    @Override
    public void onInitialize() {
        super.onInitialize();
    }
}
