package com.subscription.android.client.view;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.subscription.android.client.Api;
import com.subscription.android.client.R;
import com.subscription.android.client.model.Subscriptions;
import com.subscription.android.client.model.VisitDate;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubscriptionActivity extends AppCompatActivity {
    GridView gvMain;
    Button btnOk;
    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SubActivity", "Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_subscription);

        gvMain = (GridView) findViewById(R.id.gvMain);
        user=(TextView) findViewById(R.id.userName);
        btnOk = (Button) findViewById(R.id.button2admin);

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               go2Client();
               }
        };
        btnOk.setOnClickListener(oclBtnOk);
        user.setText("Hello "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        getSubscriptions();
    }
    private void go2Client() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
    private void getSubscriptions() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);

       // call;
        //Start token verification
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();


                            // Send token to your backend via HTTPS
                            // ...
                            Call<List<Subscriptions>> call = api.getSubscriptionByUid(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            call.enqueue(new Callback<List<Subscriptions>>() {
                                @Override
                                public void onResponse(Call<List<Subscriptions>> call, Response<List<Subscriptions>> response) {

                                    List<Subscriptions> dateList = response.body();

                                    List<VisitDate> dates=dateList.get(0).getVisitDates();
                                    String[] visitedDates = new String[dates.size()];


                                    for (int i = 0; i < dates.size(); i++) {
                                        visitedDates[i] = dates.get(i).getDate().toString();
                                    }

                                    System.out.println("debug");//change to log
                                    //displaying the string array into listview

                                    gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));
                                    adjustGridView();
                                }

                                @Override
                                public void onFailure(Call<List<Subscriptions>> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    String[] str = {"Error","='("};
                                    gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, str));
                                    System.out.println(t.getMessage());
                                    t.printStackTrace();
                                }
                            });

                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });


    }
    private void adjustGridView() {
        gvMain.setNumColumns(4);
    }

}