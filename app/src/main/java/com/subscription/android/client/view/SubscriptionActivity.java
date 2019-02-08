package com.subscription.android.client.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.subscription.android.client.Api;
import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.R;
import com.subscription.android.client.model.Subscriptions;
import com.subscription.android.client.model.VisitDate;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubscriptionActivity extends BaseActivity {
    GridView gvMain;
    ImageButton btnOk, btStartScan;
    TextView hellouser, instructorName, exCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SubActivity", "Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_subscription);

        gvMain = (GridView) findViewById(R.id.gvMain);
        hellouser=(TextView) findViewById(R.id.userName);
        instructorName=(TextView) findViewById(R.id.instructorName);
        exCost = (TextView) findViewById(R.id.cost);
        btnOk =  findViewById(R.id.button2admin);

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               go2Client();
               }
        };
        btnOk.setOnClickListener(oclBtnOk);

        hellouser.setText(getResources().getString(R.string.greetings)+" "+FirebaseAuth.getInstance().getCurrentUser().getEmail());
        getSubscriptions();
        qrGenerator(FirebaseAuth.getInstance().getCurrentUser().getUid());


        btStartScan = findViewById(R.id.photoScan);

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");

               updateText(result.getContents());
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
    private void getSubscriptions() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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

                           showProgressDialog();

                            Call<Subscriptions> call = api.getSubscriptionByUid(idToken,FirebaseAuth.getInstance().getCurrentUser().getUid());
                            call.enqueue(new Callback<Subscriptions>() {
                                @Override
                                public void onResponse(Call<Subscriptions> call, Response<Subscriptions> response) {
                                    hideProgressDialog();
                                    List<VisitDate> dates=response.body().getVisitDates();
                                    String[] visitedDates = new String[dates.size()];

                                    exCost.setText(String.valueOf(response.body().getPrice()));
                                    instructorName.setText(response.body().getInstructor().getName()+" "+response.body().getInstructor().getSurname());
                                    for (int i = 0; i < dates.size(); i++) {
                                        visitedDates[i] = dates.get(i).getDate().toString();
                                    }

                                    gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));
                                    adjustGridView();
                                }

                                @Override
                                public void onFailure(Call<Subscriptions> call, Throwable t) {
                                    if (t instanceof IOException) {
                                        Toast.makeText(SubscriptionActivity.this, getResources().getString(R.string.networkissue), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(SubscriptionActivity.this, getResources().getString(R.string.bigIssue), Toast.LENGTH_SHORT).show();
                                        t.printStackTrace();
                                        Log.d("SubActivityBigProblem", t.getMessage());
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

    public void qrGenerator(String uid){
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(uid, BarcodeFormat.QR_CODE, 512, 512);
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
            e.printStackTrace();
        }
    }

}