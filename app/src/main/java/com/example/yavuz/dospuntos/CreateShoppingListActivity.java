package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateShoppingListActivity extends Activity {

    TextView howMany;
    ListView listView;
    ListView cListView;
    AllPostClass adapter;
    CreateClass adapter2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayList<String> productFromFB;
    ArrayList<String> priceFromFB;
    ArrayList<String> cProductFromFB;
    ArrayList<String> cPriceFromFB;
    ArrayList<String> cQuantityFromFB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);

        howMany = (TextView) findViewById(R.id.createShoppingListHowManyETxt);
        listView = findViewById(R.id.createShoppingListProductListView);
        cListView = findViewById(R.id.createShoppingListChoosenListView);
        productFromFB = new ArrayList<String>();
        priceFromFB = new ArrayList<String>();
        cProductFromFB = new ArrayList<String>();
        cPriceFromFB = new ArrayList<String>();
        cQuantityFromFB = new ArrayList<String>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new AllPostClass(productFromFB,priceFromFB,this);
        adapter2 = new CreateClass(cProductFromFB,cQuantityFromFB,cPriceFromFB,this);
        cListView.setAdapter(adapter2);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),productFromFB.get(position),Toast.LENGTH_SHORT).show();
                if(!howMany.getText().toString().isEmpty()) {
                    cProductFromFB.add(productFromFB.get(position));
                    Float calculate = Float.parseFloat(String.valueOf(priceFromFB.get(position))) * Float.parseFloat(howMany.getText().toString());
                    cPriceFromFB.add(String.valueOf(calculate));
                    cQuantityFromFB.add(howMany.getText().toString());
                    adapter2.notifyDataSetChanged();
                }
                else{
                    howMany.setError("FILL IT UP");
                }
            }
        });

        getDataFromFB();
        //getDataFromFBC();
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
    /*public void getDataFromFBC(){
        DatabaseReference newRef2= firebaseDatabase.getReference("products");
        newRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //System.out.println("dataFB"+dataSnapshot.getChildren());
                //System.out.println("dataFB"+dataSnapshot.getKey());
                //System.out.println("dataFB"+dataSnapshot.getValue());
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    HashMap<String,String> hashMap = (HashMap<String,String>) ds.getValue();
                    //cProductFromFB.add(hashMap.get("name"));
                    //cQuantityFromFB.add(hashMap.get("quantity"));
                    //cPriceFromFB.add(hashMap.get("price"));
                    //adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
}
