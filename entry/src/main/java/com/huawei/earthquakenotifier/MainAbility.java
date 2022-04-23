package com.huawei.earthquakenotifier;

import com.huawei.earthquakenotifier.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbility extends Ability {

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0xD001100, "MY_TAG");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        setMainRoute(MainAbilitySlice.class.getName());
    }
}