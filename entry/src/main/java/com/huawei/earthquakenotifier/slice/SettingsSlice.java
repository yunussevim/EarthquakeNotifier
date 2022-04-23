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
        initUI();
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

    public void initUI(){
        Switch enableNotifications = findComponentById(ResourceTable.Id_switch_notifications);
        Text textSlider = (Text) findComponentById(ResourceTable.Id_text_slider);
        Slider slider = (Slider) findComponentById(ResourceTable.Id_slider);
        mag = (TextField) findComponentById(ResourceTable.Id_settings_field_magnitude);
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
        if(MyApplication.isNotificationsEnabled){
            enableNotifications.setChecked(true);
        }
        enableNotifications.setCheckedStateChangedListener((absButton, b) -> {
            MyApplication.isNotificationsEnabled = b;
        });
    }



}