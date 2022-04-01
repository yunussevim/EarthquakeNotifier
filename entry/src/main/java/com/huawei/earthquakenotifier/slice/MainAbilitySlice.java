package com.huawei.earthquakenotifier.slice;

import com.huawei.earthquakenotifier.MyApplication;
import com.huawei.earthquakenotifier.api.APIClient;
import com.huawei.earthquakenotifier.api.APIInterface;
import com.huawei.earthquakenotifier.model.MultipleResource;
import com.huawei.earthquakenotifier.ResourceTable;
import com.huawei.hms.location.harmony.LocationRequest;
import com.huawei.hms.location.harmony.LocationSettingsRequest;
import com.huawei.hms.location.harmony.SettingsProviderClient;
import com.huawei.hms.maps.harmony.*;
import com.huawei.hms.maps.harmony.model.*;
import com.huawei.hms.push.common.ApiException;
import com.huawei.hms.push.ohos.HmsInstanceId;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.agp.window.dialog.CommonDialog;
import ohos.agp.window.dialog.ToastDialog;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;

public class MainAbilitySlice extends AbilitySlice {
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "MY_TAG");
    private MapView mMapView;
    private HuaweiMap mHuaweiMap;
    private DirectionalLayout layout;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Button settings = findComponentById(ResourceTable.Id_settings);
        settings.setClickedListener(component -> {present(new SettingsSlice(), new Intent());});
        layout = findComponentById(ResourceTable.Id_mapLayout);
        initTabs();

        if(MyApplication.firtLaunch){
            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
            callretofit(apiInterface);
            showDialog();
            MyApplication.firtLaunch = false;
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
                new CameraPosition(new LatLng(38.9637,35.2433), 10, 0, 0);

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
                        new ToastDialog(CommonContext.getContext()).setText("onMapClick ").show();
                    }
                });
                Circle mCircle = new Circle(this);
                if (null == mHuaweiMap) {
                    return;
                }
                if (null != mCircle) {
                    mCircle.remove();
                    mCircle = null;
                }
                mCircle = mHuaweiMap.addCircle(new CircleOptions()
                        .center(new LatLng(38.9637,35.2433))
                        .radius(1500)
                        .fillColor(Color.GREEN.getValue()));
                int strokeColor = Color.RED.getValue();
                float strokeWidth = 15.0f;
                mCircle.setStrokeColor(strokeColor);
                mCircle.setStrokeWidth(strokeWidth);
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

    private void callretofit(APIInterface apiInterface) {
        Call<MultipleResource> call = apiInterface.doGetListResources();// map the Model class to the response we use:
        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(Call<MultipleResource> call, Response<MultipleResource> response) {
                MultipleResource resource = response.body();
                MyApplication.earthquakes = resource.features;
            }

            @Override
            public void onFailure(Call<MultipleResource> call, Throwable t)
            {
                HiLog.debug(LABEL,t.getMessage());
                call.cancel();
            }
        });
    }

    private void initTabs(){
        Button homeButton = (Button) findComponentById(ResourceTable.Id_btn_home);
        Button detectedButton = (Button) findComponentById(ResourceTable.Id_btn_detected);
        Button settingsButton = (Button) findComponentById(ResourceTable.Id_btn_settings);

        homeButton.setClickedListener(component -> {present(new MainAbilitySlice(), new Intent());});
        detectedButton.setClickedListener(component -> {present(new DetectedSlice(), new Intent());});
        settingsButton.setClickedListener(component -> {present(new SettingsSlice(), new Intent());});
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
            int distance = calcDistance(38.9637,coordinates.get(1) ,35.2433 ,coordinates.get(0));
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
                        .title(MyApplication.earthquakes.get(i).properties.title)
                        .snippet(MyApplication.earthquakes.get(i).properties.place);
                mMarker = mHuaweiMap.addMarker(options);
            }
        }
    }

    private int calcDistance(double lat1, double lat2, double lon1,double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        HiLog.debug(LABEL,distance+"");
        return (int)distance;
    }
}
