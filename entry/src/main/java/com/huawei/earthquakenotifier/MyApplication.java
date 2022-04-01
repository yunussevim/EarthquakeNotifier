package com.huawei.earthquakenotifier;

import com.huawei.earthquakenotifier.model.MultipleResource;
import ohos.aafwk.ability.AbilityPackage;

import java.util.List;

public class MyApplication extends AbilityPackage {

    public static int distance = 0;
    public static int magnitude = 0;
    public static boolean firtLaunch = true;
    public static List<MultipleResource.Feature> earthquakes;
    @Override
    public void onInitialize() {
        super.onInitialize();
    }
}
