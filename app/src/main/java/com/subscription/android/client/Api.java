package com.subscription.android.client;


import com.subscription.android.client.model.Price;
import com.subscription.android.client.model.Subscription;
import com.subscription.android.client.model.User;
import com.subscription.android.client.model.VisitDate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface Api {
    //TODO propertie file
    String BASE_URL = "http://185.35.67.65:8080/subscription-server/";
  //  String BASE_URL = "http://192.168.31.27:8080/";
    @GET("users")
    Call<List<User>> getAdminEmails(@Header("token") String token);
    @GET("fullprice")
    Call<List<Price>> getPrice(/*@Header("token") String token*/);
    @GET("subscription")
    Call<List<Subscription>> getDescription(@Header("token") String token);
    @GET("uidsubscription/{uid}")
    Call<Subscription> getSubscriptionByUid(/*@Header("token") String token,*/ @Path("uid") String uid );
    @POST("adminclaim")
    Call<Void> registerAdmin(@Header("token") String token,@Header("uid") String uid);
    @POST("savesubscriptions")
    Call<Void> savesubscription(@Body Subscription subscription);
    @POST("savenewvisit")
    Call<Void> savevisit(@Body String uid);

}
