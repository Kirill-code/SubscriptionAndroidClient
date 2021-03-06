package com.subscription.android.client.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.subscription.android.client.Api;
import com.subscription.android.client.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class ScanDialog extends DialogFragment implements DialogInterface.OnClickListener {

    final String LOG_TAG = "visitDialog";
    String uid, admin,msg;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        admin = getArguments().getString("admin");
        uid = getArguments().getString("uid");
        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(getActivity())
                .setTitle("Отметка о посещении")
                .setPositiveButton(R.string.assignAdmin, this)
                .setNegativeButton(R.string.deleteAdmin, this)
                .setNeutralButton(R.string.cancel, this)
                .setMessage("Вы уверены, что хотите назначить права "+admin+" ?");
        return adb.create();
    }
 public  void assignAdmins(String uid) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Call<Void> call = api.registerAdmin(idToken, uid);

                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    msg="успешно.";
                                    response.body();
                                    notifyUser();
                                    Log.d("AssignAdmin", "Admin rights assigned "+uid);
                       }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    msg="с ошибкой.";
                                    System.out.println(t.getMessage());
                                    t.printStackTrace();
                                }
                            });

                        } else {
                            System.out.println(task.getException());
                        }
                    }
                });


    }
    public void notifyUser(){
        Toast.makeText(getActivity(), "Права назначены "+msg, Toast.LENGTH_SHORT).show();
    }

    public void onClick(DialogInterface dialog, int which) {

        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE: {
                i = R.string.yes;
                try{
                  assignAdmins(uid);
                  notifyUser();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

                break;
            }
            case Dialog.BUTTON_NEGATIVE:
                i = R.string.no;
                break;
            case Dialog.BUTTON_NEUTRAL:
                break;
        }
        if (i > 0)
            Log.d(LOG_TAG, "Admin rights: " + getResources().getString(i));
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 2: onDismiss");
    }
}
