package com.subscription.android.client.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.subscription.android.client.R;
import com.subscription.android.client.view.AdminActivity;
import com.subscription.android.client.view.EmailPasswordActivity;
import com.subscription.android.client.view.InstructorChartActitivty;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

/**
 * Created by sonu on 17:07, 10/01/19
 * Copyright (c) 2019 . All rights reserved.
 */
public class BottomSheetNavigationFragment extends BottomSheetDialogFragment {

    private void go2AdminPage() {
        Intent intent = new Intent(getActivity(), AdminActivity.class);
        startActivity(intent);
    }

    private void go2Main() {
        Intent intent = new Intent(getActivity(), EmailPasswordActivity.class);
        startActivity(intent);
    }
    private void go2Chart() {
        Intent intent = new Intent(getActivity(), InstructorChartActitivty.class);
        startActivity(intent);
    }

    public static BottomSheetNavigationFragment newInstance() {

        Bundle args = new Bundle();

        BottomSheetNavigationFragment fragment = new BottomSheetNavigationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            //check the slide offset and change the visibility of close button
            if (slideOffset > 0.5) {
                closeButton.setVisibility(View.VISIBLE);
            } else {
                closeButton.setVisibility(View.GONE);
            }
        }
    };

    FirebaseUser  mUser= FirebaseAuth.getInstance().getCurrentUser();

    private ImageView closeButton, signout;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.bottom_navigation_drawer, null);
        dialog.setContentView(contentView);

        NavigationView navigationView = contentView.findViewById(R.id.navigation_view);
        TextView userName = contentView.findViewById(R.id.bottom_user_name);
        if (mUser.getDisplayName()!=null && !mUser.getDisplayName().equals("")) {
            userName.setText(mUser.getDisplayName());
        }
        TextView userEmail = contentView.findViewById(R.id.bottom_user_email);
        userEmail.setText(mUser.getEmail());
        ImageView userImage = contentView.findViewById(R.id.profile_image);
        if (mUser.getPhotoUrl() != null) {
            Picasso.with(getContext()).load(mUser.getPhotoUrl()).into(userImage);
        }


        //implement navigation menu item click event
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav01:
                        go2Chart();
                        break;
                    case R.id.nav02:
                        go2AdminPage();
                        break;
                }
                return false;
            }
        });
        closeButton = contentView.findViewById(R.id.close_image_view);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dismiss bottom sheet
                dismiss();
            }
        });
        signout = contentView.findViewById(R.id.signout_bottom_sheet);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                go2Main();
            }
        });


        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

}