package com.subscription.android.client.view;

import android.os.Bundle;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;


import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.fragments.AdminRightsFragmentDialog;

import java.net.ConnectException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.subscription.android.client.R;
import com.subscription.android.client.model.UserAdmins;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends BaseActivity {
    ListView listView;

    public static String msgAdmin;
    static String[] uids, dispalyedName;

    private static final String TAG = AdminActivity.class.getName();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listView = (ListView) findViewById(R.id.listViewAdmins);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                msgAdmin = listView.getItemAtPosition(position).toString();

                //Maybe best option is change to some object
                Bundle args = new Bundle();
                args.putString("admin", msgAdmin);
                args.putString("uid", uids[position]);
                args.putString("dispalyedName", dispalyedName[position]);

            AdminRightsFragmentDialog dg=new AdminRightsFragmentDialog();

             dg.setArguments(args);
             dg.show(getFragmentManager(),"AdminDialog");

            }
        });


        getAdmins();
    }


    private void getAdmins() {

        //Start token verification
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {

                            String idToken = task.getResult().getToken();
                            showProgressDialog();
                            // Send token to your backend via HTTPS
                            Call<List<UserAdmins>> call = api.getAdminEmails(idToken);

                            call.enqueue(new Callback<List<UserAdmins>>() {
                                @Override
                                public void onResponse(Call<List<UserAdmins>> call, Response<List<UserAdmins>> response) {
                                    hideProgressDialog();
                                    List<UserAdmins> userList = response.body();

                                    String[] users = new String[userList.size()];

                                    uids = new String[userList.size()];
                                    dispalyedName = new String[userList.size()];
                                    //looping through all the heroes and inserting the names inside the string array
                                    for (int i = 0; i < userList.size(); i++) {

                                        if(Boolean.TRUE.equals(userList.get(i).getClaim())){
                                            users[i] = userList.get(i).getEmail()+"   "+getResources().getString(R.string.true_tick);
                                        }else {
                                            users[i] = userList.get(i).getEmail();
                                        }
                                        uids[i] = userList.get(i).getUid();
                                        dispalyedName[i]=userList.get(i).getDisplayedName();
                                    }
                                    //displaying the string array into listview
                                    listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, users));


                                }

                                @Override
                                public void onFailure(Call<List<UserAdmins>> call, Throwable t) {
                                    try {
                                        throw t;
                                    } catch (ConnectException ex) {
                                        Log.e(TAG, ex.getMessage());
                                        hideProgressDialog();
                                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.errorconnection) ,
                                                Toast.LENGTH_SHORT).show();
                                    } catch (TimeoutException ex) {
                                        Log.e(TAG, ex.getMessage());
                                        hideProgressDialog();
                                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.timeout) ,
                                                Toast.LENGTH_SHORT).show();
                                    } catch (Throwable et) {
                                        Log.e(TAG, et.getMessage());

                                        hideProgressDialog();
                                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.helpdesk) ,
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        } else {
                            System.out.println( task.getException());
                        }
                    }
                });


    }
}