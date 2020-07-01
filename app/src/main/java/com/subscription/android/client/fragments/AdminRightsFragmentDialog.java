package com.subscription.android.client.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.subscription.android.client.Api;
import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.R;
import com.subscription.android.client.view.AdminActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kirill_code on 30.01.2019.
 */
public class AdminRightsFragmentDialog extends DialogFragment implements DialogInterface.OnClickListener {

    final String LOG_TAG = "adminDialog";
    String uid, admin, displayedName;
    Context context;
    EditText userName, userSurname;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Api api = retrofit.create(Api.class);



    BaseActivity utilObj = new BaseActivity();
    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    LayoutInflater inflater;
    View view;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflater= getActivity().getLayoutInflater();
        view=inflater.inflate(R.layout.dialog_name, null);

        userName = view.findViewById(R.id.dialog_admin_name);
        userSurname = view.findViewById(R.id.dialog_admin_surname);

        admin = getArguments().getString("admin");
        uid = getArguments().getString("uid");
        displayedName = getArguments().getString("dispalyedName");

        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(getActivity())
                .setTitle("Внимание. Права администратора.")
                .setPositiveButton(R.string.assignAdmin, this)
                .setNegativeButton(R.string.deleteAdmin, this)
                .setNeutralButton(R.string.cancel, this)
                .setMessage("Вы уверены, что хотите изменить права " + admin + " ? " + displayedName);
        if (displayedName == null) {
                adb.setView(view);
        }

        return adb.create();
    }

    private void goBack() {
        Intent intent = new Intent(context, AdminActivity.class);
        startActivity(intent);
    }

    public void assignAdmins(String uid) {
        Call<Void> call = api.registerAdmin(utilObj.idToken, uid);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("AssignAdmin", "Admin rights assigned " + uid);
                Toast.makeText(context, R.string.admin_success, Toast.LENGTH_SHORT).show();
                createInstructorDB();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, R.string.networkissue, Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
                Log.w("AssignAdmin", "Admin rights failure " + uid + " " + t.getMessage());

            }
        });
    }

    public void removeAdmins(String uid) {
        Call<Void> call = api.removeAdmin(utilObj.idToken, uid);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("AssignAdmin", "Admin rights deleted " + uid);
                Toast.makeText(context, R.string.admin_success_delete, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, R.string.networkissue, Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
                Log.w("AssignAdmin", "Admin delete failure " + uid + " " + t.getMessage());

            }
        });
    }

 /*   public void onDismiss(DialogInterface dialog) {
        Log.d(LOG_TAG, "Dialog 2: onDismiss");
    }*/

     private void createInstructorDB(){



         /*Call<Void> call = api.assignInstructor(new BaseActivity().idToken, uid);

          call.enqueue(new Callback<Void>() {
              @Override
              public void onResponse(Call<Void> call, Response<Void> response) {
                  Log.i("AssignAdmin", "Admin rights assigned " + uid);
                  Toast.makeText(context, R.string.admin_success, Toast.LENGTH_SHORT).show();
              }

              @Override
              public void onFailure(Call<Void> call, Throwable t) {
                  Toast.makeText(context, R.string.networkissue, Toast.LENGTH_SHORT).show();
                  System.out.println(t.getMessage());
                  Log.w("AssignAdmin", "Admin rights failure " + uid+" "+t.getMessage());

              }
          });*/
      }

    public void onClick(DialogInterface dialog, int which) {

        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE: {
                i = R.string.yes;
                try {
                    if (TextUtils.isEmpty(userName.getText())) {
                        userName.setError(getResources().getText(R.string.usernameError));
                    }
                    if (TextUtils.isEmpty(userSurname.getText())) {
                        userSurname.setError(getResources().getText(R.string.userPhoneError));
                        dialog.dismiss();
                    }
                    if (!TextUtils.isEmpty(userName.getText())
                            && !TextUtils.isEmpty(userSurname.getText())) {
                        assignAdmins(uid);
                        goBack();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                break;
            }
            case Dialog.BUTTON_NEGATIVE:
                i = R.string.no;
                try {
                    removeAdmins(uid);
                    goBack();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            case Dialog.BUTTON_NEUTRAL:


                break;
        }
        if (i > 0)
            Log.d(LOG_TAG, "Admin rights: " + getResources().getString(i));
    }
}
