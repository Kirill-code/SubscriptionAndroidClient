package com.subscription.android.client.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.R;
import com.subscription.android.client.model.DTO.OutcomeSubscriptionDTO;
import com.subscription.android.client.model.Instructor;
import com.subscription.android.client.model.Price;
import com.subscription.android.client.model.User;

import java.io.EOFException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kirill_code on 06.02.2019.
 */
public class saleExistingUser extends BaseActivity {
    private static final String TAG = saleExistingUser.class.getName();


    Button signIn;
    EditText userName, userSurname, usrPhone, usrEmail, usrPswd,usrDesciption;
    long subCount, priceId;
    List<Price> priceList;

    CarouselPicker carouselPicker;
    List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
    List<String> usersTmp = new ArrayList<>();
    CarouselPicker.CarouselViewAdapter textAdapter;
    private AutoCompleteTextView autoTextView;
    ArrayAdapter<String> usersAdapter;
    private FirebaseAuth mAuth1;
    private FirebaseAuth mAuth2;
    Instructor intentInstructor = new Instructor();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);
        getPrice();
        getUsers();

        Intent i = getIntent();
        intentInstructor = (Instructor) i.getSerializableExtra("Instructor2Sub");
//TODO move it to properties
        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setApiKey("AIzaSyBuXBwvwnaUQ0ZBKssZoAWpH9SUpWEpnC0")
                .setApplicationId("fir-test-e9bb1").build();
        mAuth1 = FirebaseAuth.getInstance();

        try {
            FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "AnyAppName");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e) {
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("AnyAppName"));
        }
        signIn = (Button) findViewById(R.id.login);
        autoTextView = findViewById(R.id.autoTextView);
        usrPhone = (EditText) findViewById(R.id.usrPhone);
        usrEmail = (EditText) findViewById(R.id.usrEmail);
        usrPswd = (EditText) findViewById(R.id.usrPassword);
        usrDesciption = (EditText) findViewById(R.id.usrDescription);

        autoTextView.setThreshold(1); //will start working from first character



        carouselPicker = (CarouselPicker) findViewById(R.id.carousel);
        textAdapter = new CarouselPicker.CarouselViewAdapter(this, textItems, 0);
        usersAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, usersTmp);


        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                subCount = priceList.get(position).getCost();
                priceId=priceList.get(position).getId();
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(autoTextView.getText())) {
                    autoTextView.setError(getResources().getText(R.string.usernameError));
                } else {
                    autoTextView.setError(null);
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
                if (!TextUtils.isEmpty(autoTextView.getText())
                        && !TextUtils.isEmpty(usrPhone.getText())
                        && !TextUtils.isEmpty(usrEmail.getText())
                        && !TextUtils.isEmpty(usrPswd.getText())) {
                    Toast.makeText(getApplicationContext(), " " + subCount,
                            Toast.LENGTH_SHORT).show();
                    //TODO user entity call for create new record
                    User newUser = new User();
                    newUser.setEmail(usrEmail.getText().toString());
                    newUser.setName(autoTextView.getText().toString().substring(0, autoTextView.getText().toString().indexOf(" ")));
                    newUser.setSurname(autoTextView.getText().toString().substring(autoTextView.getText().toString().indexOf(" ") + 1));
                    newUser.setMobile(usrPhone.getText().toString());
                    createAccount(newUser, usrPswd.getText().toString()); //API call to Firebase

                }
            }
        });

    }

    private void go2Main() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent);
    }

    private void createAccount(User newUser, String password) {

        showProgressDialog();

        mAuth2.createUserWithEmailAndPassword(newUser.getEmail(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth2.getCurrentUser();
                            System.out.println(user.getUid()+" UID!!!");
                            String unicId=user.getUid();
                            newUser.setUid(unicId);
                            //newDBuser= createNewDBUser(newUser); //create new user in REST db

                            //call PrinterActivit printPhoto
                            /*нужно создать новый абонемент и пользователя
                            updateUI(user);*/
                            mAuth2.signOut();
                            createNewDBUser(newUser);
                           // go2Main();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                /*создаю только новый абонемент*/

                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }


                        }
                    }
                });
    }
    public void createNewDBUser(User newUser) {

        Call<User> call = api.newUser(newUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideProgressDialog();

                Toast.makeText(getApplicationContext(), "User " + newUser.getUid() + " created",
                        Toast.LENGTH_SHORT).show();

                OutcomeSubscriptionDTO outcomeSubscriptionDTO =new OutcomeSubscriptionDTO();
                    /*incomeSubscriptionDTO.setSaleDate(currentDate);
                    incomeSubscriptionDTO.setFinishDate(finishDate);*/
                outcomeSubscriptionDTO.setId(0);
                outcomeSubscriptionDTO.setPrice(priceId);
                outcomeSubscriptionDTO.setInstructorId(intentInstructor.getId());
                //TODO change problemヾ(≧▽≦*)o
                //this  problem!!!
                outcomeSubscriptionDTO.setAssociatedUserId(response.body().getId());//API call
                outcomeSubscriptionDTO.setDescription("");
                //（￣︶￣）↗　
                //API call
                createNewDBSubscription(outcomeSubscriptionDTO);
                Log.i(TAG, "User " + newUser.getUid() + " created");

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                try {
                    throw t;
                } catch (ConnectException ex) {
                    Log.e(TAG, ex.getMessage());
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorconnection),
                            Toast.LENGTH_SHORT).show();
                } catch (EOFException ex) {
                    Log.e(TAG, ex.getMessage());
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.helpdesk),
                            Toast.LENGTH_SHORT).show();
                } catch (Throwable et) {
                    Log.e(TAG, et.getMessage());

                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.helpdesk),
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
                                    priceList = response.body();
                                    for (Price price : priceList) {
                                        textItems.add(new CarouselPicker.TextItem(String.format("%d", price.getNumbers()), 20)); //I'm not proud of it

                                    }//is it correct?
                                    subCount = priceList.get(0).getCost();
                                    carouselPicker.setAdapter(textAdapter);
                                    hideProgressDialog();

                                }

                                @Override
                                public void onFailure(Call<List<Price>> call, Throwable t) {
                                    try {
                                        throw t;
                                    } catch (ConnectException ex) {
                                        Log.e(TAG, ex.getMessage());
                                        hideProgressDialog();
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorconnection),
                                                Toast.LENGTH_SHORT).show();
                                    } catch (EOFException ex) {
                                        Log.e(TAG, ex.getMessage());
                                        hideProgressDialog();
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.helpdesk),
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Throwable et) {
                                        Log.e(TAG, et.getMessage());

                                        hideProgressDialog();
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.helpdesk),
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        } else {
                            Log.e("CallbackException", task.getException().toString());
                        }
                    }
                });
    }

    private void getUsers() {

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();

                            showProgressDialog();

                            Call<List<User>> call = api.getUsersList(idToken);
                            call.enqueue(new Callback<List<User>>() {
                                @Override
                                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                    List<User> users = response.body();
                                    for (User user : users) {
                                        usersAdapter.add(user.getName() + " " + user.getSurname());
                                    }
                                    autoTextView.setAdapter(usersAdapter);

                                    hideProgressDialog();

                                }

                                @Override
                                public void onFailure(Call<List<User>> call, Throwable t) {
                                    try {
                                        throw t;
                                    } catch (ConnectException ex) {
                                        Log.e(TAG, ex.getMessage());
                                        hideProgressDialog();
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorconnection),
                                                Toast.LENGTH_SHORT).show();
                                    } catch (EOFException ex) {
                                        Log.e(TAG, ex.getMessage());
                                        hideProgressDialog();
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.helpdesk),
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Throwable et) {
                                        Log.e(TAG, et.getMessage());

                                        hideProgressDialog();
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.helpdesk),
                                                Toast.LENGTH_SHORT).show();

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
