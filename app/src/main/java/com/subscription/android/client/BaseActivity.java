package com.subscription.android.client;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.subscription.android.client.model.DTO.OutcomeSubscriptionDTO;
import com.subscription.android.client.model.User;

import java.io.EOFException;
import java.net.ConnectException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getName();
    public FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public Api api = retrofit.create(Api.class);

    public String idToken = "";
    private long  createduser ;


    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void getToken() {
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            idToken = task.getResult().getToken();

                        } else {
                            Log.e("CallbackException", task.getException().toString());
                        }
                    }
                });
    }



    public void createNewDBSubscription(OutcomeSubscriptionDTO OutcomeSubscriptionDTO) {
        //?? why that
        getToken();

        Call<Void> call = api.savesubscription(/*idToken,*/ OutcomeSubscriptionDTO);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                hideProgressDialog();

                Log.i(TAG, "Subscription created");
                Toast.makeText(getApplicationContext(), "Subscription created",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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

}
