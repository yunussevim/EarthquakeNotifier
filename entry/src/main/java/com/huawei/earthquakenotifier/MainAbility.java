package com.huawei.earthquakenotifier;

import com.huawei.earthquakenotifier.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbility extends Ability {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
    }

}