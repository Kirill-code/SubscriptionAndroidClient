package com.subscription.android.client;


import com.subscription.android.client.model.Subscription;
import com.subscription.android.client.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface Api {
    //TODO propertie file
     static final String BASE_URL = "http://192.168.43.251:8080/";

    @GET("users")
    Call<List<User>> getAdminEmails(@Header("token") String token);
    @GET("subscription")
    Call<List<Subscription>> getDescription(@Header("token") String token);
    @GET("subscription/{uid}")
    Call<Subscription> getSubscriptionByUid(@Header("token") String token, @Path("uid") String uid );
    @POST("adminclaim")
    Call<Void> registerAdmin(@Header("token") String token,@Header("uid") String uid);
    @POST("savesubscriptions")
    Call<Void> savesubscription(@Body Subscription subscription);

}
