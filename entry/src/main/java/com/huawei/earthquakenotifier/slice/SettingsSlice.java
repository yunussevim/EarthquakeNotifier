package com.huawei.earthquakenotifier.slice;

import com.huawei.earthquakenotifier.MyApplication;
import com.huawei.earthquakenotifier.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;

public class SettingsSlice extends AbilitySlice {

    private TextField mag;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_slice_settings);
        Button back = findComponentById(ResourceTable.Id_btn_settings_back);
        back.setClickedListener(component -> {present(new MainAbilitySlice(), new Intent());});
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

    @Override
    protected void onBackground() {
        super.onBackground();
        MyApplication.magnitude = Integer.parseInt(mag.getText());
    }

    public void initTabs(){
        Button homeButton = (Button) findComponentById(ResourceTable.Id_btn_home);
        Button detectedButton = (Button) findComponentById(ResourceTable.Id_btn_detected);
        Button settingsButton = (Button) findComponentById(ResourceTable.Id_btn_settings);
        Text textSlider = (Text) findComponentById(ResourceTable.Id_text_slider);
        Slider slider = (Slider) findComponentById(ResourceTable.Id_slider);
        mag = (TextField) findComponentById(ResourceTable.Id_settings_field_magnitude);

        homeButton.setClickedListener(component -> {present(new MainAbilitySlice(), new Intent());});
        detectedButton.setClickedListener(component -> {present(new DetectedSlice(), new Intent());});
        settingsButton.setClickedListener(component -> {present(new SettingsSlice(), new Intent());});

        mag.setText(String.valueOf(MyApplication.magnitude));
        textSlider.setText(String.valueOf(MyApplication.distance)+" km");
        slider.setMaxValue(3000);
        slider.setProgressValue(MyApplication.distance);
        slider.setValueChangedListener(new Slider.ValueChangedListener() {
            @Override
            public void onProgressUpdated(Slider slider, int i, boolean b) {
                textSlider.setText(String.valueOf(i)+" km");
                MyApplication.distance = i;
            }

            @Override
            public void onTouchStart(Slider slider) {

            }

            @Override
            public void onTouchEnd(Slider slider) {

            }
        });
    }

}