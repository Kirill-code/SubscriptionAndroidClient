package com.subscription.android.client;


import com.subscription.android.client.model.DTO.VisitsDTO;
import com.subscription.android.client.model.Price;
import com.subscription.android.client.model.Subscription;
import com.subscription.android.client.model.User;
import com.subscription.android.client.model.UserAdmins;
import com.subscription.android.client.model.VisitDate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface Api {
    //TODO propertie file
  //TODO create API parent class
 //String BASE_URL = "http://185.35.67.65:8080/subscription-server/";
  String BASE_URL = "http://192.168.8.103:8080/";
    @GET("admins")
    Call<List<UserAdmins>> getAdminEmails(@Header("token") String token);
    @GET("generalusers")
    Call<List<User>> getUsersList(@Header("token") String token);
    @GET("fullprice")
    Call<List<Price>> getPrice();
    @GET("subscription")
    Call<List<Subscription>> getDescription(@Header("token") String token);
    @GET("uidsubscription/{uid}")
    Call<Subscription> getSubscriptionByUid(/*@Header("token") String token,*/ @Path("uid") String uid );
    @GET("instructorgrouped")
    Call<List> getInstructorsVisits(/*@Header("token") String token,*/ );
  @GET("visitsbydate")
  Call<List<List>> getMonthVisits(@Header("instid") long instid, @Header("dateStart") String dateStart, @Header("dateEnd") String dateEnd);

    @POST("adminclaim")
    Call<Void> registerAdmin(@Header("token") String token,@Header("uid") String uid);
    @POST("savesubscriptions")
    Call<Void> savesubscription(@Body Subscription subscription);
    @POST("savenewvisit")
    Call<Void> savevisit(@Body String uid);
    @POST("createnewuser")
    Call<Void> newUser(@Body User user);

    @DELETE("removeadminclaim")
  Call<Void> removeAdmin(@Header("token") String token,@Header("uid") String uid);

}
