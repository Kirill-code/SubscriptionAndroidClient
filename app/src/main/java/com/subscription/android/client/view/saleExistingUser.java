package com.subscription.android.client.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.subscription.android.client.Api;
import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.R;
import com.subscription.android.client.model.Price;
import com.subscription.android.client.model.Subscription;

import java.io.EOFException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kirill_code on 06.02.2019.
 */
public class saleExistingUser extends BaseActivity {
    Button signIn;
    EditText userName, userSurname, usrPhone, usrEmail, usrPswd;
    long subCount ;
    List<Price> priceList;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    Api api = retrofit.create(Api.class);
    CarouselPicker carouselPicker;
    List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
    CarouselPicker.CarouselViewAdapter textAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);
        getPrice();

        signIn = (Button) findViewById(R.id.login);
        userName = (EditText) findViewById(R.id.usrName);
        userSurname = (EditText) findViewById(R.id.usrSurName);
        usrPhone = (EditText) findViewById(R.id.usrPhone);
        usrEmail = (EditText) findViewById(R.id.usrEmail);
        usrPswd = (EditText) findViewById(R.id.usrPassword);

        carouselPicker = (CarouselPicker) findViewById(R.id.carousel);
//20 here represents the textSize in dp, change it to the value you want.
         textAdapter= new CarouselPicker.CarouselViewAdapter(this, textItems, 0);



        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                subCount =priceList.get(position).getCost();
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userName.getText())) {
                    userName.setError(getResources().getText(R.string.usernameError));
                } else {
                    userName.setError(null);
                }
                if (TextUtils.isEmpty(userSurname.getText())) {
                    userSurname.setError(getResources().getText(R.string.usersurnameError));
                } else {
                    userSurname.setError(null);
                }
                if (TextUtils.isEmpty(usrPhone.getText())) {
                    usrPhone.setError(getResources().getText(R.string.userPhoneError));
                } else {
                    usrPhone.setError(null);
                }
                if (TextUtils.isEmpty(usrEmail.getText())) {
                    usrEmail.setError(getResources().getText(R.string.userEmailError));
                } else {
                    usrEmail.setError(null);
                }
                if (TextUtils.isEmpty(usrPswd.getText())) {
                    usrPswd.setError(getResources().getText(R.string.userEmailError));
                } else {
                    usrPswd.setError(null);
                }
                if (!TextUtils.isEmpty(userName.getText())
                        && !TextUtils.isEmpty(userSurname.getText())
                        && !TextUtils.isEmpty(usrPhone.getText())
                        && !TextUtils.isEmpty(usrEmail.getText())
                        && !TextUtils.isEmpty(usrPswd.getText())) {
                    Toast.makeText(getApplicationContext(), " "+subCount,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void getPrice() {

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();

                            showProgressDialog();

                            Call<List<Price>> call = api.getPrice();
                            call.enqueue(new Callback<List<Price>>() {
                                @Override
                                public void onResponse(Call<List<Price>> call, Response<List<Price>> response) {
                                priceList=response.body();
                                for(Price price:priceList){
                                    textItems.add(new CarouselPicker.TextItem(String.format("%d",price.getNumbers()), 20));
                                }
                                    subCount=priceList.get(0).getCost();
                                    carouselPicker.setAdapter(textAdapter);
                                    hideProgressDialog();

                                }

                                @Override
                                public void onFailure(Call<List<Price>> call, Throwable t) {
                                    try {
                                        throw t;
                                    } catch (ConnectException ex) {
                                        hideProgressDialog();

                                    } catch (EOFException ex) {
                                        hideProgressDialog();
                                        String[] visitedDates = {getResources().getString(R.string.emptylessons)};

                                    } catch (Throwable et) {
                                        hideProgressDialog();
                                        String[] visitedDates = {getResources().getString(R.string.helpdesk)};

                                        Log.d("SubActivityBigProblem", t.getMessage());
                                        Log.d("SubActivityBigProblem", et.getMessage());
                                    }
                                }
                            });

                        } else {
                            Log.e("CallbackException", task.getException().toString());
                        }
                    }
                });
    }
}
