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

   /* @IgnoreExtraProperties
    public class Employee {

        public String username;
        public String pw;
        public String fn;
        public String ln;

        public Employee() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Employee(String username,String pw,String fn,String ln) {
            this.username = username;
            this.pw = pw;
            this.fn = fn;
            this.ln = ln;
        }

    }

*/



    public void applyClick(View view){

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

    public void backClick(View view){
        Intent employeeSignUpIntent = new Intent(SignUpEmployeeActivity.this,WelcomeActivity.class);
        startActivity(employeeSignUpIntent);
    }
}
