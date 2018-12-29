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

public class LoginActivity extends Activity {

    private EditText userName,password;
    private Button login,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.loginUsernameETxt);
        password = findViewById(R.id.loginPasswordETxt);
        login = findViewById(R.id.loginLoginBtn);
        back = findViewById(R.id.loginBackBtn);

        //Login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uName = userName.getText().toString();
                final String passW = password.getText().toString();
                final boolean[] usernameExist = {false};
                final boolean[] eUsernameExist = {false};
                final String[] dbPass = new String[1];

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                ValueEventListener dbListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        usernameExist[0] = dataSnapshot.child("customers").child(uName).exists();
                        eUsernameExist[0] = dataSnapshot.child("employee").child(uName).exists();

                        Log.d("userName2", "onDataChange: " + usernameExist[0]);
                        if(usernameExist[0] == true) {
                            dbPass[0] = dataSnapshot.child("customers").child(uName).child("pw").getValue().toString();
                            Log.d("Password: ", "onDataChange: " + dbPass[0]);
                            if(!dbPass[0].equals(passW)) {
                                Toast.makeText(getApplicationContext(), "Password is wrong.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //To customerMainMenu
                                Intent customerMainIntent = new Intent(LoginActivity.this,CustomerActivity.class);
                                startActivity(customerMainIntent);
                            }
                        }
                        else if(eUsernameExist[0] == true) {
                            dbPass[0] = dataSnapshot.child("employee").child(uName).child("pw").getValue().toString();
                            Log.d("Password: ", "onDataChange: " + dbPass[0]);
                            if(!dbPass[0].equals(passW)) {
                                Toast.makeText(getApplicationContext(), "Password is wrong.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //To customerMainMenu
                                Intent employeeMainIntent = new Intent(LoginActivity.this,EmployeeActivity.class);
                                startActivity(employeeMainIntent);
                            }
                        }
                        else{

                                Toast.makeText(getApplicationContext(), "USER DOESN'T EXIST", Toast.LENGTH_SHORT).show();

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
