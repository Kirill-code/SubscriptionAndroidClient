package com.subscription.android.client.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.subscription.android.client.Api;
import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.R;
import com.subscription.android.client.model.DTO.InstructorDTO;
import com.subscription.android.client.model.Instructor;
import com.subscription.android.client.view.AdminActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminDialogFragment extends DialogFragment {


    final String LOG_TAG = "adminDialog";
    String uid, admin, displayedName;
    Context context;
    EditText userName, userSurname;

    LayoutInflater inflater;
    View view;

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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        utilObj.getToken();
        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_name, null);

        userName = view.findViewById(R.id.dialog_admin_name);
        userSurname = view.findViewById(R.id.dialog_admin_surname);

        admin = getArguments().getString("admin");
        uid = getArguments().getString("uid");
        displayedName = getArguments().getString("dispalyedName");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle("Внимание. Права администратора.")
                .setMessage("Вы уверены, что хотите изменить права " + admin + " ? " + displayedName)
                .setPositiveButton(R.string.assignAdmin, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if name null
                        // userName.setError(getResources().getText(R.string.usernameError));
                        assignAdmins();
                        goBack();
                        dialog.cancel();

                    }
                })
                .setNegativeButton(R.string.deleteAdmin, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeAdmins();
                        goBack();
                        dialog.cancel();
                    }
                })
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        goBack();
                        dialog.cancel();
                    }
                });
        /*Should be implemented!!
        if (displayedName == null) {
            builder.setView(view);
        }*/
        return builder.create();
    }

    public void assignAdmins() {
        createInstructorDB();

        Call<Void> call = api.registerAdmin(utilObj.idToken, uid);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("AssignAdmin", "Admin rights assigned " + uid);
                Toast.makeText(context, R.string.admin_success, Toast.LENGTH_SHORT).show();
//                createInstructorDB();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, R.string.networkissue, Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
                Log.w("AssignAdmin", "Admin rights failure " + uid + " " + t.getMessage());

            }
        });
    }

    public void removeAdmins() {

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

    private void createInstructorDB() {
        InstructorDTO temp = new InstructorDTO();
        if (displayedName != null) {
            temp.setId(1);
            temp.setName(displayedName.substring(0, displayedName.indexOf(" ")));
            temp.setSurname(displayedName.substring(displayedName.indexOf(" ")+1));
        }else {
            temp.setName("Change");
            temp.setSurname("it");
        }
        temp.setUid(uid);

        Call<Void> call = api.assignInstructor(utilObj.idToken, temp);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("AssignAdmin", "Admin created  " + uid);
                Toast.makeText(context, R.string.admin_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, R.string.networkissue, Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());
                Log.w("AssignAdmin", "Admin rights failure " + uid + " " + t.getMessage());

            }
        });
    }
    private void goBack() {
        Intent intent = new Intent(context, AdminActivity.class);
        startActivity(intent);
    }
}
