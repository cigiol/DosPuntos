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

    ListView productsLV;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_price);

        back = findViewById(R.id.productPriceBackBtn);
        productsLV = findViewById(R.id.productPriceListView);

        final List<String> products = new ArrayList<String>();
        final List<String> prices = new ArrayList<String>();
        final List<String> quantities = new ArrayList<String>();

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("products");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //DataSnapshot productsSnap = dataSnapshot.child("products");
                //Iterable<DataSnapshot> proPrice = customerSnap.getChildren();
                String values;
                //sasasasasasasas
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String,String> hashMap = (HashMap<String,String>) ds.getValue();
                    values = hashMap.get("name")+"-"+hashMap.get("price")+"-"+hashMap.get("quantity");
                    products.add(values);
                }
                ArrayAdapter<String> adapterIn = new ArrayAdapter<String>(ProductPriceActivity.this, android.R.layout.simple_list_item_1,products);

                productsLV.setAdapter(adapterIn);
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


    }
}
