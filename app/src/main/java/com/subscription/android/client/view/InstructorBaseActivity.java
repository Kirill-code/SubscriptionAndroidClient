package com.subscription.android.client.view;

import android.os.Bundle;
import android.util.Log;

import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.R;

public class InstructorBaseActivity extends BaseActivity {

    private static final String TAG = InstructorBaseActivity.class.getName();


    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_base);
    }
}
