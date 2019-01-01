package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerSettingsActivity extends Activity {

    private EditText name,sname,adress,email,pass;
    private Button apply, back;
    private Spinner list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_settings);
        final String uname=getIntent().getExtras().getString("username");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mDatabaseRef = database.getReference();

        apply=findViewById(R.id.customerSettingsApplyBtn);
        back = findViewById(R.id.customerSettingsBackBtn);
        list=findViewById(R.id.CostumerCards);

        name=findViewById(R.id.customerSettingsNameETxt);
        sname=findViewById(R.id.customerSettingsSurnameETxt);
        adress=findViewById(R.id.customerSettingsAddressETxt);
        email=findViewById(R.id.customerSettingsEmailETxt);
        pass=findViewById(R.id.customerSettingsPasswordETxt);

        //fill editTExtS
        ValueEventListener postlisten=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pass.setText(dataSnapshot.child("customers").child(uname).child("pw").getValue().toString());
                name.setText(dataSnapshot.child("customers").child(uname).child("fName").getValue().toString());
                sname.setText(dataSnapshot.child("customers").child(uname).child("lName").getValue().toString());
                email.setText(dataSnapshot.child("customers").child(uname).child("email").getValue().toString());
                adress.setText(dataSnapshot.child("customers").child(uname).child("address").getValue().toString());
                List<String> cards = new ArrayList<String>();

                for (DataSnapshot customers : dataSnapshot.child("customers").getChildren()) {

                    if(customers.getKey().equals(uname)){
                        Iterable<DataSnapshot> cardValue = customers.child("cards").getChildren();

                        for (DataSnapshot card : cardValue){
                            cards.add("Number:"+card.getKey()+"::Points:"+card.getValue());
                        }
                        break;
                    }
                }

                ArrayAdapter<String> adapterIn = new ArrayAdapter<String>(CustomerSettingsActivity.this, android.R.layout.simple_list_item_1,cards);
                list.setAdapter(adapterIn);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseRef.addListenerForSingleValueEvent(postlisten);



        ///update database
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseRef.child("customers").child(uname).child("pw").setValue(pass.getText().toString());
                mDatabaseRef.child("customers").child(uname).child("fName").setValue(name.getText().toString());
                mDatabaseRef.child("customers").child(uname).child("lName").setValue(sname.getText().toString());
                mDatabaseRef.child("customers").child(uname).child("email").setValue(email.getText().toString());
                mDatabaseRef.child("customers").child(uname).child("address").setValue(adress.getText().toString());
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
