package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends Activity {

    private Button login,guest,customerSignUp,employeeSignUp,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        login = findViewById(R.id.loginBtn);
        customerSignUp = findViewById(R.id.customerSignUpBtn);
        employeeSignUp = findViewById(R.id.employeeSignUpBtn);
        guest = findViewById(R.id.GuestBtn);
        exit = findViewById(R.id.exitBtn);

        //Login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(loginIntent);

            }
        });

        //customerSignUp
        customerSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customerSignUpIntent = new Intent(WelcomeActivity.this,signUpCustomer.class);
                startActivity(customerSignUpIntent);
            }
        });

        //employeeSignUp
        employeeSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent employeeSignUpIntent = new Intent(WelcomeActivity.this,SignUpEmployeeActivity.class);
                startActivity(employeeSignUpIntent);
            }
        });

        //guest
        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guestIntent = new Intent(WelcomeActivity.this,ProductPriceActivity.class);
                startActivity(guestIntent);
            }
        });

        //exit
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
