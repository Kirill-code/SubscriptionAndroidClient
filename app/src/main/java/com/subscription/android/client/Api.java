package com.subscription.android.client;


import com.subscription.android.client.model.Subscriptions;
import com.subscription.android.client.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface Api {

     static final String BASE_URL = "http://192.168.43.251:8080/";

    @GET("users")
    Call<List<User>> getAdminEmails(@Header("token") String token);
    @GET("subscription")
    Call<List<Subscriptions>> getDescription(@Header("token") String token);
    @GET("subscription/{uid}")
    Call<List<Subscriptions>> getSubscriptionByUid(@Path("uid") String uid);
    @POST("adminclaim")
    Call<Void> registerAdmin(@Header("token") String token,@Header("uid") String uid);

}
