package com.subscription.android.client.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.subscription.android.client.Api;
import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.NFC.NFC;
import com.subscription.android.client.R;
import com.subscription.android.client.model.Instructor;
import com.subscription.android.client.model.Subscription;
import com.subscription.android.client.model.VisitDate;
import com.subscription.android.client.print.PrinterActivity;

import java.io.EOFException;
import java.net.ConnectException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubscriptionActivity extends BaseActivity  {
    private static final String TAG = SubscriptionActivity.class.getName();;

    GridView gvMain;
    ImageButton btnOk, btStartScan, imageSignOut;
    TextView hellouser, instructorName, exCost;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Date currentTime = Calendar.getInstance().getTime();
    int backButtonCount=0;

    Instructor intentInstructor=new Instructor();



    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Api api = retrofit.create(Api.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_subscription);
//DELETE ALL UNUSED STUFF
        gvMain = (GridView) findViewById(R.id.gvMain);
        hellouser = (TextView) findViewById(R.id.userName);
        instructorName = (TextView) findViewById(R.id.instructorName);
        exCost = (TextView) findViewById(R.id.cost);


        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2Client();
            }
        };
        btnOk.setOnClickListener(oclBtnOk);

        /*nfcScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go2NFC();
            }
        });*/
       /* instrChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go2Chart();
            }
        });
        insertNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               go2Print();
            }
        });*/
        hellouser.setText(getResources().getString(R.string.greetings) + " " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

        ////////////////////////////
            getSubscriptions();
        ///////////////////////////
        qrGenerator(FirebaseAuth.getInstance().getCurrentUser().getUid());

        imageSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                go2Main();
            }
        });


        btStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(SubscriptionActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.initiateScan();
            }
        });
    }
    private void go2Print() {
        Intent intent = new Intent(this, PrinterActivity.class);
        intent.putExtra("Instructor2Sub", intentInstructor);

        startActivity(intent);
    }
    private void go2Chart() {
        Intent intent = new Intent(this, InstructorChartActitivty.class);
        intent.putExtra("Instructor2Sub", intentInstructor);

        startActivity(intent);
    }
    private void go2Main() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent);
    }
    private void go2NFC() {
        Intent intent = new Intent(this, NFC.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, getResources().getText(R.string.exit), Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.i(TAG, "Cancelled scan");

            } else {
                System.out.println(result.getContents().substring(0, 7));
                if (result.getContents().substring(0, 8).equals("syryauid")) {
                    Log.i(TAG, "Scanned:" + result.getContents());
                    updateText(result.getContents().substring(8));
                    updateSubscription(result.getContents().substring(8));
                } else {
                    Log.e(TAG, result.getContents());
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.qrscanerror),
                            Toast.LENGTH_SHORT).show();
                }


            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    private void updateText(String scanCode) {
        exCost.setText(scanCode);
    }

    private void go2Client() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }

    private void updateSubscription(String uid) {

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();

                            showProgressDialog();

                            Call<Subscription> call = api.getSubscriptionByUid(/*idToken,*/uid);
                            call.enqueue(new Callback<Subscription>() {
                                @Override
                                public void onResponse(Call<Subscription> call, Response<Subscription> response) {

                                    Call<Void> call2save = api.savevisit(response.body().getAssociatedUserId());

                                    call2save.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call2save, Response<Void> response) {
                                            hideProgressDialog();
                                            Toast.makeText(getApplicationContext(), getResources().getText(R.string.scanSucsessful), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call2save, Throwable t) {
                                            Log.e(TAG, t.getMessage());
                                            hideProgressDialog();
                                            Toast.makeText(getApplicationContext(), getResources().getText(R.string.repeatlater), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<Subscription> call, Throwable t) {
                                    try {
                                        throw t;
                                    } catch (ConnectException ex) {
                                        hideProgressDialog();
                                        Log.e(TAG, ex.getMessage());
                                        String[] visitedDates = {getResources().getString(R.string.errorconnection)};
                                        gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));
                                    } catch (EOFException ex) {
                                        Log.e(TAG, ex.getMessage());
                                        hideProgressDialog();
                                        String[] visitedDates = {getResources().getString(R.string.emptylessons)};
                                        gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));
                                    } catch (Throwable et) {
                                        Log.e(TAG, et.getMessage());
                                        hideProgressDialog();
                                        String[] visitedDates = {getResources().getString(R.string.helpdesk)};
                                        gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));

                                    }
                                }
                            });

                        } else {
                            Log.e(TAG, task.getException().toString());
                        }
                    }
                });
    }

    private void getSubscriptions() {
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();

                            showProgressDialog();

                            Call<Subscription> call = api.getSubscriptionByUid(/*idToken,*/FirebaseAuth.getInstance().getCurrentUser().getUid());
                            call.enqueue(new Callback<Subscription>() {
                                @Override
                                public void onResponse(Call<Subscription> call, Response<Subscription> response) {
                                    if (response.body() == null) {
                                        hideProgressDialog();
                                        String[] visitedDates = {getResources().getString(R.string.novisits)};
                                        gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));
                                        Log.e(TAG, "Empty response");
                                    } else {
                                        hideProgressDialog();
                                        List<VisitDate> dates = response.body().getVisitDates();

                                        response.body().setVisitDates(dates);
                                        String[] visitedDates = new String[dates.size()];




                                        exCost.setText("Стоимость: "+String.valueOf(response.body().getPrice()));
                                        instructorName.setText("Ваш инструктор: "+response.body().getInstrName() + " " + response.body().getInstrSurname());

                                        for (int i = 0; i < dates.size(); i++) {
                                            Date date = dates.get(i).getDate();
                                            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
                                           // mTimeText.setText("Time: " + dateFormat.format(date));
                                           visitedDates[i] = dateFormat.format(date).toString();
                                           // visitedDates[i]="new new";
                                        }

                                        intentInstructor.setId(response.body().getInstructorId());
                                        intentInstructor.setName(response.body().getInstrName());
                                        intentInstructor.setSurname(response.body().getInstrSurname());

                                        gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));
                                        adjustGridView();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Subscription> call, Throwable t) {
                                    try {
                                        throw t;
                                    } catch (ConnectException ex) {
                                        Log.e(TAG, ex.getMessage());
                                        hideProgressDialog();
                                        String[] visitedDates = {getResources().getString(R.string.errorconnection)};
                                        gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));
                                    } catch (EOFException ex) {
                                        Log.e(TAG, ex.getMessage());
                                        hideProgressDialog();
                                        String[] visitedDates = {getResources().getString(R.string.emptylessons)};
                                        gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));
                                    } catch (Throwable et) {
                                        Log.e(TAG, et.getMessage());
                                        hideProgressDialog();
                                        String[] visitedDates = {getResources().getString(R.string.helpdesk)};
                                        gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));

                                    }
                                }
                            });

                        } else {
                            Log.e("CallbackException", task.getException().toString());
                        }
                    }
                });


    }

    private void adjustGridView() {
        gvMain.setNumColumns(4);
    }

    public void qrGenerator(String uid) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode("syryauid" + uid, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);

        } catch (WriterException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}