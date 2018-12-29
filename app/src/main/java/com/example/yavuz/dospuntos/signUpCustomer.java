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
import com.google.firebase.database.ValueEventListener;

public class signUpCustomer extends Activity {

    private EditText userName,password,fName,lName,email,address;
    private Button apply,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_customer);

        userName = findViewById(R.id.customerSignUpUsernameETxt);
        password = findViewById(R.id.customerSignUpPassETxt);
        fName = findViewById(R.id.customerSignUpFNameETxt);
        lName = findViewById(R.id.customerSignUpLNameETxt);
        email = findViewById(R.id.customerSignUpEmailETxt);
        address = findViewById(R.id.customerSignUpAddressETxt);
        apply = findViewById(R.id.customerSignUpApplyBtn);
        back = findViewById(R.id.customerSignUpBackBtn);

        //Apply
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uName = userName.getText().toString();
                final boolean[] usernameExist = {false,false,false};
                final String pw = password.getText().toString();
                final String firstName = fName.getText().toString();
                final String lastName = lName.getText().toString();
                final String mail = email.getText().toString();
                final String adres = address.getText().toString();

                final DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                ValueEventListener dbListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot customerSnap = dataSnapshot.child("customers");
                        Iterable<DataSnapshot> emailValue = customerSnap.getChildren();

                        for (DataSnapshot customers : emailValue) {
                            if(customers.child("email").getValue().toString().equals(mail)){
                                usernameExist[1] = true;
                                break;
                            }
                            else
                                usernameExist[1] = false;
                        }

                        usernameExist[0] = dataSnapshot.child("customers").child(uName).exists();
                        usernameExist[2] = dataSnapshot.child("employee").child(uName).exists();

                        if(usernameExist[0] == true || usernameExist[2] == true)
                            Toast.makeText(getApplicationContext(),"This username is already exist.",Toast.LENGTH_SHORT).show();
                        else if(usernameExist[1] == true)
                            Toast.makeText(getApplicationContext(),"This email is already exist.",Toast.LENGTH_SHORT).show();
                        else{
                            Customer customer = new Customer(pw,firstName,lastName,mail,adres);
                            mDatabase.child("customers").child(uName).setValue(customer);
                            Toast.makeText(getApplicationContext(),"Successfully.",Toast.LENGTH_SHORT).show();
                            Intent toLogin = new Intent(signUpCustomer.this,LoginActivity.class);
                            startActivity(toLogin);
                            finish();
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("Error: ", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
                mDatabase.addListenerForSingleValueEvent(dbListener);

            }
        });

        //Back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
