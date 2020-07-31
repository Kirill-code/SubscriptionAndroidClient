package com.subscription.android.client;


import com.subscription.android.client.model.DTO.InstructorDTO;
import com.subscription.android.client.model.DTO.VisitsDTO;
import com.subscription.android.client.model.Instructor;
import com.subscription.android.client.model.Price;
import com.subscription.android.client.model.Subscription;
import com.subscription.android.client.model.DTO.OutcomeSubscriptionDTO;
import com.subscription.android.client.model.User;
import com.subscription.android.client.model.UserAdmins;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface Api {
//stage api
// String BASE_URL = "http://192.168.8.103:8080/";
 String BASE_URL = "https://www.suryaschoolapi.ru.com:8443/subscription/";

  //change token validation!!
    @GET("admins")
    Call<List<UserAdmins>> getAdminEmails(@Header("token") String token);
    @GET("generalusers")
    Call<List<User>> getUsersList(@Header("token") String token);
    @GET("fullprice")
    Call<List<Price>> getPrice();
    @GET("subscription")
    Call<List<Subscription>> getDescription(@Header("token") String token);
    @GET("uidsubscription/{uid}")
    Call<Subscription> getSubscriptionByUid(@Header("token") String token, @Path("uid") String uid );
    @GET("instructorgrouped")
    Call<List> getInstructorsVisits(@Header("token") String token );
    @GET("visitsbydate")
    Call<List<List>> getMonthVisits(@Header("instid") long instid, @Header("dateStart") String dateStart, @Header("dateEnd") String dateEnd);
  @GET("visitsbydate")
  Flowable<List<List>> getMonthVisitsRx(@Header("instid") long instid, @Header("dateStart") String dateStart, @Header("dateEnd") String dateEnd);
    @GET("uidinstructor/{uid}")
    Call<Instructor> getInstructorByUid(@Header("token") String token, @Path("uid") String uid );

    @POST("adminclaim")
    Call<Void> registerAdmin(@Header("token") String token,@Header("uid") String uid);
    @POST("newsubscription")
    Call<Void> savesubscription(/*@Header("token") String token,*/@Body OutcomeSubscriptionDTO subscription);
    @POST("savenewvisit")
    Call<Void> savevisit(@Body String uid);
    @POST("createnewuser")
    Call<User> newUser(@Body User user);
    @POST("newinstructor")
    Call<Void> assignInstructor(@Header("token") String token,@Body InstructorDTO instructor);


  @DELETE("removeadminclaim")
  Call<Void> removeAdmin(@Header("token") String token,@Header("uid") String uid);

}
