package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class CustomerActivity extends Activity {

    Button addCard,pointToCash,pointToFriend,showList,createList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        showList = findViewById(R.id.customerShowListBtn);
        createList = findViewById(R.id.customerCreateListBtn);
        showList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guestIntent = new Intent(CustomerActivity.this,ProductPriceActivity.class);
                startActivity(guestIntent);
            }
        });


        addCard = findViewById(R.id.customerAddCardBtn);
        pointToCash = findViewById(R.id.customerPointsToCashBtn);
        pointToFriend = findViewById(R.id.customerPointsToFriendBtn);

        String uName = null;

        try{
            Bundle extras = getIntent().getExtras();
             uName = extras.getString("username");
        }catch (Exception e){

        }
        final String userName = uName;

        //Database init
        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("customers");

        createList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guestIntent = new Intent(CustomerActivity.this,CreateShoppingListActivity.class);
                guestIntent.putExtra("username",userName);
                startActivity(guestIntent);
            }
        });

        //Points To Friend
        pointToFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toFriendIntent = new Intent(CustomerActivity.this,PointsToFriendActivity.class);
                toFriendIntent.putExtra("username",userName);
                startActivity(toFriendIntent);
            }
        });

        //Points To Cash
        pointToCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Add Card
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValueEventListener dbListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        boolean existCard = true;
                        String number = "";

                        while(existCard){

                            int randomCardNumber= new Random().nextInt(1000000);
                            number = String.valueOf(randomCardNumber);
                            if(number.length()<6){
                                for (short i = 0 ;i < 6-number.length();i++)
                                    number+="0";
                            }

                            for (DataSnapshot customers : dataSnapshot.getChildren()) {
                                existCard = customers.child("cards").child(number).exists();
                                if(existCard)
                                    break;
                            }
                        }

                        for (DataSnapshot customers : dataSnapshot.getChildren()) {
                            if(customers.getKey().equals(userName) && !existCard){
                                mDatabase.child(userName).child("cards").child(number).setValue(0);
                                Toast.makeText(getApplicationContext(),"Card added.",Toast.LENGTH_SHORT).show();
                                break;
                            }
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
    }
}
