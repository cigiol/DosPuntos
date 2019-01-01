package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmployeeSettingsActivity extends Activity {
    private Button apply,back;
    private EditText fn,ln,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_settings);

        final String uname=getIntent().getExtras().getString("username");
        final String[] dbPass = new String[1];

        final   FirebaseDatabase database = FirebaseDatabase.getInstance();
        final   DatabaseReference mDatabaseRef = database.getReference();



        back=findViewById(R.id.employeeSettingsBackBtn);
        apply=findViewById(R.id.employeeSettingsApplyBtn);
        fn=findViewById(R.id.employeeSettingsFNameETxt);
        ln=findViewById(R.id.employeeSettingsLNameETxt);
        pass=findViewById(R.id.employeeSettingsPasswordETxt);
        ValueEventListener postlisten=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fn.setText(dataSnapshot.child("employee").child(uname).child("fn").getValue().toString());
                ln.setText(dataSnapshot.child("employee").child(uname).child("ln").getValue().toString());
                pass.setText(dataSnapshot.child("employee").child(uname).child("pw").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
       mDatabaseRef.addValueEventListener(postlisten);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef.child("employee").child(uname).child("pw").setValue(pass.getText().toString());
                mDatabaseRef.child("employee").child(uname).child("fn").setValue(fn.getText().toString());
                mDatabaseRef.child("employee").child(uname).child("ln").setValue(ln.getText().toString());

                finish();

                }
            });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
