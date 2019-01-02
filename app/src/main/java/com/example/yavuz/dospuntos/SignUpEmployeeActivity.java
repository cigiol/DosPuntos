package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class SignUpEmployeeActivity extends Activity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private Button apply ;
    private Button back ;
    private EditText un;
    private EditText pw;
    private EditText fn;
    private EditText ln;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_employee);

        apply = findViewById(R.id.employeeSignUpApplyBtn);
        back = findViewById(R.id.employeeSignUpBackBtn);
        un = findViewById(R.id.employeeSignUpUsernameETxt);
        pw = findViewById(R.id.employeeSignUpPassETxt);
        fn = findViewById(R.id.employeeSignUpFNameETxt);
        ln = findViewById(R.id.employeeSignUpLNameETxt);




    }

    public void applyClick(View view){
        boolean[] check={true,true,true,true};
        final String uName = un.getText().toString().trim();
        final boolean[] usernameExist = {false,false,false};
        final String pw1 = pw.getText().toString();
        final String firstName = fn.getText().toString();
        final String lastName = ln.getText().toString();

        if(uName.isEmpty()) {
            check[0]=false;
            un.setError("Fill it");
        }
        if(pw1.isEmpty()) {
            check[1]=false;
            pw.setError("Fill it");

        }
        if(firstName.isEmpty()) {
            check[2]=false;
            fn.setError("Fill it");

        }
        if(lastName.isEmpty()) {
            check[3]=false;
            ln.setError("Fill it");
        }

        if(check[0]&&check[1]&&check[2]&&check[3]&&check[4]&&check[5])
        {
            firebaseDatabase = FirebaseDatabase.getInstance();
            myRef = firebaseDatabase.getReference();

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.child("employee").child(un.getText().toString()).exists() || snapshot.child("customers").child(un.getText().toString()).exists() ) {
                        Toast.makeText(getApplicationContext(),"USERNAME EXIST",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        myRef = firebaseDatabase.getReference();

                        employee employee = new employee(un.getText().toString(), pw.getText().toString(),fn.getText().toString(),ln.getText().toString());

                        myRef.child("employee").child(un.getText().toString()).setValue(employee);
                        Toast.makeText(getApplicationContext(),"SUCCESSFULLY",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    public void backClick(View view){
        finish();
    }
}
