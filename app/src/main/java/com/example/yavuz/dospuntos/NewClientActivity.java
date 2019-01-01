package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewClientActivity extends Activity {

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
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);
        Bundle extras = getIntent().getExtras();
        userName = extras.getString("username");
        howMany = (TextView) findViewById(R.id.newClientHowManyETxt);
        listView = findViewById(R.id.newClientProductListView);
        cListView = findViewById(R.id.newClientChoosenListView);
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
                    Map<String, JSONObject> userMap= new HashMap<String, JSONObject>();
                    JSONObject jo=new JSONObject();
                    try {

                        String time=DateFormat.getDateTimeInstance().format(new Date());
                        Invoice inv=new Invoice(cProductFromFB.get(position),cPriceFromFB.get(position),cQuantityFromFB.get(position));
                        myRef.child("invoices").child(userName).child(time).child(cProductFromFB.get(position)).setValue(inv);
                        System.out.println(inv);//TODO toplam ekle
                    }
                    catch (Exception e){

                    }

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
}
