package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointsToFriendActivity extends Activity {

    private EditText email,cardNumber,pointCount;
    private Button apply,back;
    private Spinner cardsSpin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_to_friend);

        email = findViewById(R.id.pointsToFriendEmailETxt);
        cardNumber = findViewById(R.id.pointsToFriendCardNumberETxt);
        pointCount = findViewById(R.id.pointsToFriendHowManyETxt);
        apply = findViewById(R.id.pointsToFriendApplyEBtn);
        back = findViewById(R.id.pointsToFriendBackEBtn);
        cardsSpin = findViewById(R.id.pointsToFriendCardsSpinner);

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

        //Preparing Cards
        final String finalUName = uName;
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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

                ArrayAdapter<String> adapterIn = new ArrayAdapter<String>(PointsToFriendActivity.this, android.R.layout.simple_list_item_1,cards);
                cardsSpin.setAdapter(adapterIn);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Apply
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mail = email.getText().toString();
                final int number = Integer.parseInt(cardNumber.getText().toString());
                final float point = Float.parseFloat(pointCount.getText().toString());


                ValueEventListener dbListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        boolean existMail = false;
                        boolean existCard = false;
                        boolean []everythingIsDone = {false,false,false};

                        //DataSnapshot customerSnap = dataSnapshot.child("customers");
                        //Iterable<DataSnapshot> emailValue = customerSnap.getChildren();

                        for (DataSnapshot customers : dataSnapshot.getChildren()) {
                            if(customers.child("email").getValue().toString().equals(mail)){
                                existCard = customers.child("cards").child(String.valueOf(number)).exists();
                                existMail = true;
                               break;
                            }
                        }

                        if (!existMail)
                            Toast.makeText(getApplicationContext(),"Email is wrong or not exist.",Toast.LENGTH_SHORT).show();
                        else if(!existCard)
                            Toast.makeText(getApplicationContext(),"Card is wrong or not exist.",Toast.LENGTH_SHORT).show();
                        else{

                            //Find user and user's friend and do process
                            for (DataSnapshot customers : dataSnapshot.getChildren()) {

                                //For user
                                if(customers.getKey().equals(userName) && !everythingIsDone[2]){
                                    String [] cardsValues = cardsSpin.getSelectedItem().toString().split(":");
                                    float oldPoint = Float.parseFloat(cardsValues[4]);
                                    float newPoint = oldPoint - point;
                                    if(newPoint<0){
                                        Toast.makeText(getApplicationContext(),"You don't have enough points. Please enter less points.",Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                    else{
                                        mDatabase.child(userName).child("cards").child(String.valueOf(cardsValues[1])).setValue(newPoint);

                                        everythingIsDone[2] = true;
                                        everythingIsDone[1] = true;
                                    }
                                }
                                if (everythingIsDone[1]==true)
                                    break;
                            }
                            for (DataSnapshot customers : dataSnapshot.getChildren()) {
                                //For user's friend
                                if(customers.child("email").getValue().toString().equals(mail) && everythingIsDone[2]){
                                    float oldPoint = Float.parseFloat(customers.child("cards").child(String.valueOf(number)).getValue().toString());
                                    float newPoint = oldPoint + (point*4/5);
                                    String friend = customers.getKey();
                                    mDatabase.child(friend).child("cards").child(String.valueOf(number)).setValue(newPoint);
                                    everythingIsDone[0] = true;
                                    Toast.makeText(getApplicationContext(),"Successfailed.",Toast.LENGTH_SHORT).show();

                                }
                                if (everythingIsDone[0]==true)
                                    break;
                            }
                        }
                        if (everythingIsDone[0]==true && everythingIsDone[1]==true){
                            //TODO Successfailed i degismeyi unutma
                            //TODO Ekstra olarak apply yaparken sorabilir emin misin falan diye

                            Toast.makeText(getApplicationContext(),"Successfailed.",Toast.LENGTH_SHORT).show();
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

    }
}
