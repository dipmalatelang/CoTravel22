package com.example.tgapplication.chat;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAPoqsiCU:APA91bFmeJ-TrHKLIFKosQ4_S7SqOC2iu2IaJYgGHpxswbnv0fcwHlj8Zrr7riQdhTvUHWZPoC1IuZH612Mj-2K9IF30_Vyg9NcCHexicYPZUnhXnHlbBy7CbPY6jwtS5oFg7B4AVygt"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
