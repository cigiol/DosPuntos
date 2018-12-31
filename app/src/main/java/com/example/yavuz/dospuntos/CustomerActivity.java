package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerActivity extends Activity {

    Button addCard,pointToFriend,showList,createList;
    Spinner cardSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        showList = findViewById(R.id.customerShowListBtn);
        createList = findViewById(R.id.customerCreateListBtn);
        cardSpinner = findViewById(R.id.customerCardSpinner);
        showList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guestIntent = new Intent(CustomerActivity.this,ProductPriceActivity.class);
                startActivity(guestIntent);
            }
        });


        addCard = findViewById(R.id.customerAddCardBtn);
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

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Array for cards info
                List<String> cards = new ArrayList<String>();

                for (DataSnapshot customers : dataSnapshot.getChildren()) {

                    if(customers.getKey().equals(userName)){
                        Iterable<DataSnapshot> cardValue = customers.child("cards").getChildren();

                        for (DataSnapshot card : cardValue){
                            float po = Float.parseFloat(card.getValue().toString());
                            cards.add("Number:"+card.getKey()+"::Points:"+new DecimalFormat("##.##").format(po));
                        }
                        break;
                    }
                }

                ArrayAdapter<String> adapterIn = new ArrayAdapter<String>(CustomerActivity.this, android.R.layout.simple_list_item_1,cards);
                cardSpinner.setAdapter(adapterIn);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent guestIntent = new Intent(CustomerActivity.this,CreateShoppingListActivity.class);
                guestIntent.putExtra("username",userName);
                startActivity(guestIntent);
            }
        });

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
