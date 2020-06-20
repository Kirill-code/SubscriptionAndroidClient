package com.subscription.android.client.view;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.subscription.android.client.Api;
import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.R;

import java.net.ConnectException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InstructorChartActitivty extends BaseActivity {
    ListView listView;

    public static String msgAdmin;
    static String[] uids;

    private static final String TAG = InstructorChartActitivty.class.getName();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insrt_chart);

        listView = (ListView) findViewById(R.id.listViewAdmins);
        /*change this for catch clicks

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
        });*/


        getAdmins();
    }


    private void getAdmins() {

        Call<List> call = api.getInstructorsVisits(idToken);

        call.enqueue(new Callback<List>() {
            @Override
            public void onResponse(Call<List> call, Response<List> response) {
                hideProgressDialog();
                String [] instrRow=new String[response.body().size()];
                List<List> instrList = response.body();
                for (int i = 0; i < response.body().size(); i++) {
                    instrRow[i]=instrList.get(i).get(1).toString()+" "+instrList.get(i).get(2).toString()+" "+getResources().getString(R.string.visits4instructor)+instrList.get(i).get(0);
                }

                listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, instrRow));


            }

            @Override
            public void onFailure(Call<List> call, Throwable t) {
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

        //Start token verification
        /*FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {

                            String idToken = task.getResult().getToken();
                            showProgressDialog();
                            // Send token to your backend via HTTPS

                        } else {
                            System.out.println( task.getException());
                        }
                    }
                });
*/

    }
}