package com.huawei.earthquakenotifier.provider;

import com.huawei.earthquakenotifier.MyApplication;
import com.huawei.earthquakenotifier.api.APIClient;
import com.huawei.earthquakenotifier.api.APIInterface;
import com.huawei.earthquakenotifier.model.MultipleResource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EarthquakeApiProvider {

    public void callApiRetrofit() {
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        String[] dates = getDate();
        Call<MultipleResource> call = apiInterface.doGetListResources(dates[0],dates[1]);// map the Model class to the response we use:
        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(Call<MultipleResource> call, Response<MultipleResource> response) {
                MultipleResource resource = response.body();
                if(resource !=null){
                    MyApplication.earthquakes = resource.features;
                }
            }

            @Override
            public void onFailure(Call<MultipleResource> call, Throwable t)
            {
                call.cancel();
            }
        });
    }

    private String[] getDate(){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date dateToday = new Date();
        Date dateTomorrow = new Date(dateToday.getTime() + (1000 * 60 * 60 * 24));
        String today = simpleDateFormat.format(dateToday);
        String tomorrow = simpleDateFormat.format(dateTomorrow);
        return new String[]{today,tomorrow};
    }

    public int calcDistance(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return (int)distance;
    }
}
