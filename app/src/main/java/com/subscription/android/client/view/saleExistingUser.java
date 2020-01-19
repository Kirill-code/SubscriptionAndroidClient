package com.subscription.android.client.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.R;

/**
 * Created by Kirill_code on 06.02.2019.
 */
public class saleExistingUser extends BaseActivity {
    Button signIn;
    EditText userName, userSurname, usrPhone, usrEmail, usrPswd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);

        signIn = (Button) findViewById(R.id.login);
        userName = (EditText) findViewById(R.id.usrName);
        userSurname = (EditText) findViewById(R.id.usrSurName);
        usrPhone = (EditText) findViewById(R.id.usrPhone);
        usrEmail = (EditText) findViewById(R.id.usrEmail);
        usrPswd = (EditText) findViewById(R.id.usrPassword);


        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(userName.getText())) {
                    userName.setError(getResources().getText(R.string.usernameError));
                } else {
                    userName.setError(null);
                }
                if (TextUtils.isEmpty(userSurname.getText())) {
                    userSurname.setError(getResources().getText(R.string.usersurnameError));
                } else {
                    userSurname.setError(null);
                }
                if (TextUtils.isEmpty(usrPhone.getText())) {
                    usrPhone.setError(getResources().getText(R.string.userPhoneError));
                } else {
                    usrPhone.setError(null);
                }
                if (TextUtils.isEmpty(usrEmail.getText())) {
                    usrEmail.setError(getResources().getText(R.string.userEmailError));
                } else {
                    userSurname.setError(null);
                }
                if (TextUtils.isEmpty(usrPswd.getText())) {
                    usrPswd.setError(getResources().getText(R.string.userEmailError));
                } else {
                    usrPswd.setError(null);
                }
                if (!TextUtils.isEmpty(userName.getText())
                        && !TextUtils.isEmpty(userSurname.getText())
                        && !TextUtils.isEmpty(usrPhone.getText())
                        && !TextUtils.isEmpty(usrEmail.getText())
                        && !TextUtils.isEmpty(usrPswd.getText())) {
                    Toast.makeText(getApplicationContext(), "OK",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
