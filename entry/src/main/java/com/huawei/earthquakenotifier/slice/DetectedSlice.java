package com.huawei.earthquakenotifier.slice;

import com.huawei.earthquakenotifier.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;

public class DetectedSlice extends AbilitySlice {


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_slice_detected);
        initTabs();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    public void initTabs(){
        Button homeButton = (Button) findComponentById(ResourceTable.Id_btn_home);
        Button detectedButton = (Button) findComponentById(ResourceTable.Id_btn_detected);
        Button settingsButton = (Button) findComponentById(ResourceTable.Id_btn_settings);

        homeButton.setClickedListener(component -> {present(new MainAbilitySlice(), new Intent());});
        detectedButton.setClickedListener(component -> {present(new DetectedSlice(), new Intent());});
        settingsButton.setClickedListener(component -> {present(new SettingsSlice(), new Intent());});
    }
}