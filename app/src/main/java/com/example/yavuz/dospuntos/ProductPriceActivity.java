package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductPriceActivity extends Activity {

    ListView listView;
    AllPostClass adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayList<String> productFromFB;
    ArrayList<String> priceFromFB;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_price);

        listView = findViewById(R.id.listView);
        back = findViewById(R.id.productPriceBackBtn);
        productFromFB = new ArrayList<String>();
        priceFromFB = new ArrayList<String>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new AllPostClass(productFromFB,priceFromFB,this);

        listView.setAdapter(adapter);
        getDataFromFB();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void getDataFromFB(){
        DatabaseReference newRef= firebaseDatabase.getReference("products");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //System.out.println("dataFB"+dataSnapshot.getChildren());
                //System.out.println("dataFB"+dataSnapshot.getKey());
                //System.out.println("dataFB"+dataSnapshot.getValue());
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    HashMap<String,String> hashMap = (HashMap<String,String>) ds.getValue();
                    productFromFB.add(hashMap.get("name"));
                    priceFromFB.add(hashMap.get("price"));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
