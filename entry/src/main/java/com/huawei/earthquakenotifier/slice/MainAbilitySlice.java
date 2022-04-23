package com.huawei.earthquakenotifier.slice;

import com.huawei.earthquakenotifier.provider.EarthquakeApiProvider;
import com.huawei.earthquakenotifier.provider.LocationProvider;
import com.huawei.earthquakenotifier.MyApplication;
import com.huawei.earthquakenotifier.ResourceTable;
import com.huawei.hms.location.harmony.LocationAvailability;
import com.huawei.hms.location.harmony.LocationCallback;
import com.huawei.hms.location.harmony.LocationResult;
import com.huawei.hms.maps.harmony.*;
import com.huawei.hms.maps.harmony.model.*;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.utils.Color;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.util.List;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;

public class MainAbilitySlice extends AbilitySlice {
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "MY_TAG");
    private MapView mMapView;
    private HuaweiMap mHuaweiMap;
    private DirectionalLayout layout;
    private LocationProvider locationProvider;
    private Circle userLocationPoint;
    private EarthquakeApiProvider earthquakeApiProvider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Button settings = findComponentById(ResourceTable.Id_settings);
        settings.setClickedListener(component -> {present(new SettingsSlice(), new Intent());});
        layout = findComponentById(ResourceTable.Id_mapLayout);

        earthquakeApiProvider = new EarthquakeApiProvider();
        earthquakeApiProvider.callApiRetrofit();

        locationProvider = new LocationProvider(this);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    MyApplication.lon = locationResult.getLastLocation().getLongitude();
                    MyApplication.lat = locationResult.getLastLocation().getLatitude();
                    userLocationPoint.setCenter(new LatLng(MyApplication.lat,MyApplication.lon));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(new LatLng(MyApplication.lat,MyApplication.lon));
                    mHuaweiMap.moveCamera(cameraUpdate);
                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                if (locationAvailability != null) {
                    // Process the location status.
                }
            }
        };

        locationProvider.checkLocationSetting();
        if(locationProvider.checkPermissions()){
            locationProvider.requestPermission();
        }

        locationProvider.startLocationUpdate(5000L,locationCallback);

        if(MyApplication.firstLaunch){
            showDialog();
            MyApplication.firstLaunch = false;
        }
        else{
            showEarthquakes();
        }
        generateMap();
    }

    private void generateMap(){
        CommonContext.setContext(this);
        HuaweiMapOptions huaweiMapOptions = new HuaweiMapOptions();

        CameraPosition cameraPosition =
                new CameraPosition(new LatLng(MyApplication.lat,MyApplication.lon), 10, 0, 0);

        huaweiMapOptions.camera(cameraPosition)
                .zoomControlsEnabled(false)
                .compassEnabled(true)
                .zoomGesturesEnabled(true)
                .scrollGesturesEnabled(true)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(true)
                .liteMode(false)
                .minZoomPreference(3)
                .maxZoomPreference(13);
        mMapView = new MapView(this,huaweiMapOptions);
        mMapView.onCreate();
        mMapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(HuaweiMap huaweiMap) {
                mHuaweiMap = huaweiMap;
                mHuaweiMap.setOnMapClickListener(new OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                    }
                });

                userLocationPoint = mHuaweiMap.addCircle(new CircleOptions()
                        .center(new LatLng(MyApplication.lat,MyApplication.lon))
                        .radius(1500)
                        .fillColor(Color.GREEN.getValue()));
                int strokeColor = Color.RED.getValue();
                float strokeWidth = 15.0f;
                userLocationPoint.setStrokeColor(strokeColor);
                userLocationPoint.setStrokeWidth(strokeWidth);
            }
        });
        ComponentContainer.LayoutConfig config = new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT);
        layout.setLayoutConfig(config);
        layout.addComponent(mMapView);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void showDialog(){
        CommonDialog commonDialog = new CommonDialog(getContext());
        Component container = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_dialog, null, false);
        TextField mag = container.findComponentById(ResourceTable.Id_field_magnitude);
        TextField distance = container.findComponentById(ResourceTable.Id_field_distance);
        container.findComponentById(ResourceTable.Id_btn_dialog).setClickedListener(component1 -> {
            if(mag.getText()==""|distance.getText()==""){
                new ToastDialog(CommonContext.getContext()).setText("Fill the fileds.").show();
            }
            else{
                MyApplication.magnitude = Integer.parseInt(mag.getText());
                MyApplication.distance = Integer.parseInt(distance.getText());
                commonDialog.destroy();
                showEarthquakes();
            }
        });
        commonDialog.setContentCustomComponent(container);
        commonDialog.setSize(MATCH_CONTENT, MATCH_CONTENT);
        commonDialog.show();
    }

    private void showEarthquakes(){
        for(int i = 0; i< MyApplication.earthquakes.size();i++){
            List<Double> coordinates = MyApplication.earthquakes.get(i).geometry.coordinates;
            int distance = earthquakeApiProvider.calcDistance(MyApplication.lat,coordinates.get(1) ,MyApplication.lon ,coordinates.get(0));
            double mag = MyApplication.earthquakes.get(i).properties.mag;
            if(distance<=MyApplication.distance && mag>= MyApplication.magnitude){
                Marker mMarker = null;
                if (null == mHuaweiMap) {
                    return;
                }
                if (null != mMarker) {
                    mMarker.remove();
                    mMarker = null;
                }
                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(coordinates.get(1),coordinates.get(0)))
                        .title(MyApplication.earthquakes.get(i).id)
                        .snippet(MyApplication.earthquakes.get(i).properties.place);
                mMarker = mHuaweiMap.addMarker(options);

            }
        }
    }
}
