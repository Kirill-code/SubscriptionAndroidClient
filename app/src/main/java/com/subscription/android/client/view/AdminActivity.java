package com.subscription.android.client.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.subscription.android.client.Api;

import com.subscription.android.client.R;
import com.subscription.android.client.fragments.Dialog2;
import com.subscription.android.client.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminActivity extends AppCompatActivity {
    ListView listView;

    public static String msgAdmin;
    static String[] uids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listView = (ListView) findViewById(R.id.listViewAdmins);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                msgAdmin = listView.getItemAtPosition(position).toString();

                Bundle args = new Bundle();
                args.putString("admin", msgAdmin);
                args.putString("uid", uids[position]);

            Dialog2 dg=new Dialog2();

             dg.setArguments(args);
             dg.show(getFragmentManager(),"AdminDialog");

            }
        });


        getAdmins();
    }


    private void getAdmins() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        //Start token verification
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {

                            String idToken = task.getResult().getToken();

                            // Send token to your backend via HTTPS
                            Call<List<User>> call = api.getAdminEmails(idToken);

                            call.enqueue(new Callback<List<User>>() {
                                @Override
                                public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                                    List<User> userList = response.body();

                                    String[] users = new String[userList.size()];
                                    uids = new String[userList.size()];
                                    //looping through all the heroes and inserting the names inside the string array
                                    for (int i = 0; i < userList.size(); i++) {
                                        users[i] = userList.get(i).getEmail();
                                        uids[i] = userList.get(i).getUid();
                                    }
                                    //displaying the string array into listview
                                    listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, users));


                                }

                                @Override
                                public void onFailure(Call<List<User>> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    System.out.println(t.getMessage());
                                    t.printStackTrace();
                                }
                            });

                        } else {
                            System.out.println( task.getException());
                        }
                    }
                });


    }
}