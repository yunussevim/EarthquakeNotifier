package com.huawei.earthquakenotifier.provider;

import com.huawei.hms.location.harmony.*;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.bundle.IBundleManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class LocationProvider{

    private final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "MY_TAG");
    private final Context context;

    private FusedLocationClient fusedLocationClient;
    private SettingsProviderClient settingsProviderClient;

    public LocationProvider(Context context) {
        this.context = context;
        fusedLocationClient = new FusedLocationClient(context);
        settingsProviderClient = new SettingsProviderClient(context);
    }

    public void checkLocationSetting() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(100);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        LocationSettingsRequest request =
                builder.addLocationRequest(locationRequest).setAlwaysShow(false).setNeedBle(false).build();
        settingsProviderClient.checkLocationSettings(request)
                .addOnSuccessListener(response -> {
                    HiLog.debug(LABEL,"location settings available on device");

                })
                .addOnFailureListener(exp -> {
                    new ToastDialog(context)
                            .setText("Please enable location setting on device.")
                            .show();
                    HiLog.debug(LABEL,"location settings not available on device");
                });
    }

    public void requestPermission(){
        context.requestPermissionsFromUser(new String[]{"ohos.permission.LOCATION","ohos.permission.LOCATION_IN_BACKGROUND"}, 1001);
    }

    public void startLocationUpdate(Long interval, LocationCallback callback){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(interval);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setNeedAddress(true);
        locationRequest.setLanguage("en");

        fusedLocationClient = new FusedLocationClient(context);
        fusedLocationClient.requestLocationUpdates(locationRequest, callback)
                .addOnSuccessListener(v -> {
                    HiLog.debug(LABEL,"    ok");
                })
                .addOnFailureListener(e -> {
                    startLocationUpdate(interval,callback);
                });
    }

    public void stopLocationUpdate(LocationCallback locationCallback){
        fusedLocationClient.removeLocationUpdates(locationCallback)
                .addOnSuccessListener(v -> {
                    // Processing when the API call is successful.
                })
                .addOnFailureListener(e -> {
                    // Processing when the API call fails.
                });
    }

    public boolean checkPermissions() {
        return context.verifySelfPermission("ohos.permission.LOCATION") != IBundleManager.PERMISSION_GRANTED &&
                context.verifySelfPermission("ohos.permission.LOCATION_IN_BACKGROUND") != IBundleManager.PERMISSION_GRANTED;
    }
}
