package com.huawei.earthquakenotifier.api;

import com.huawei.earthquakenotifier.model.MultipleResource;
import retrofit2.Call;
import retrofit2.http.GET;


public interface APIInterface {

    @GET("/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02")
    Call<MultipleResource> doGetListResources();

}