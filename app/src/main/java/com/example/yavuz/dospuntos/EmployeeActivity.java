package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EmployeeActivity extends Activity {
    private Button setting,additem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
       final String username = getIntent().getExtras().getString("username");

        setting=findViewById(R.id.employeeSettingsBtn);
        additem=findViewById(R.id.employeeAddNewProductBtn);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(EmployeeActivity.this,EmployeeSettingsActivity.class);
                settingintent.putExtra("username",username);
                startActivity(settingintent);
            }
        });

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addItemIntent = new Intent(EmployeeActivity.this,AddNewProductActivity.class);
                startActivity(addItemIntent);
            }
        });

    }
}
