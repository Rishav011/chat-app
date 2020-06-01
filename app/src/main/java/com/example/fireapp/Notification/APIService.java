package com.example.fireapp.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA721yt4Q:APA91bEzDEDehHufXdc8Q8RMiH7LaYwR4zVmZLi0EEWAOW9CrTCqJJjg-omiI8WLsT_YVJor4zwcs26bpxmWkvYWs_6c1Vh9tLJS80OS_oHVLQn0PlapycfH-cW9MZFUSqtmD7rwrtKZ"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}