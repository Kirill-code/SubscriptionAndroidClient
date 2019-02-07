package com.subscription.android.client.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.R;

/**
 * Created by Kirill_code on 07.02.2019.
 */
public class ScanQR extends BaseActivity {

    TextView tvCardText;
    ImageButton btStartScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_subscription);

        tvCardText = (TextView)findViewById(R.id.tv_code_text);
        btStartScan = findViewById(R.id.photoScan);

        btStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRScanner();
            }
        });

    }

    private void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result =   IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this,    "Cancelled",Toast.LENGTH_LONG).show();
            } else {
                updateText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateText(String scanCode) {
        tvCardText.setText(scanCode);
    }

}
