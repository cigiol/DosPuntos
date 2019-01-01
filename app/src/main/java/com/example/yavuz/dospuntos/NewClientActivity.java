package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewClientActivity extends Activity {

    String it;
    String it2;
    String it3;
    TextView howMany,totalETxt,mailETxt,cardETxt;
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
    Button apply;
    Invoice inv;
    ArrayList<Invoice> listInv;
    String userName;
    float total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);
        Bundle extras = getIntent().getExtras();
        totalETxt=findViewById(R.id.newClientTotallyTxt);
        cardETxt=findViewById(R.id.newClientCardNumberETxt);
        mailETxt=findViewById(R.id.newClientEmailETxt);
        apply=findViewById(R.id.newClientApplyBtn);
        userName = extras.getString("username");
        howMany = (TextView) findViewById(R.id.newClientHowManyETxt);
        listView = findViewById(R.id.newClientProductListView);
        cListView = findViewById(R.id.newClientChoosenListView);
        productFromFB = new ArrayList<String>();
        priceFromFB = new ArrayList<String>();
        cProductFromFB = new ArrayList<String>();
        cPriceFromFB = new ArrayList<String>();
        cQuantityFromFB = new ArrayList<String>();
        listInv = new ArrayList<Invoice>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        final JSONArray jArray = null;
        adapter = new AllPostClass(productFromFB,priceFromFB,this);
        adapter2 = new CreateClass(cProductFromFB,cQuantityFromFB,cPriceFromFB,this);
        cListView.setAdapter(adapter2);
        listView.setAdapter(adapter);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time=DateFormat.getDateTimeInstance().format(new Date());
                for(int i=0;i<listInv.size();i++){
                    myRef.child("invoices").child(cardETxt.getText().toString()).child(time).child(listInv.get(i).name).setValue(listInv.get(i));
                    Log.d("APTAL", "onClick: "+listInv.get(i).name);
                }
                myRef.child("invoices").child(cardETxt.getText().toString()).child(time).child("total").setValue(total);
                myRef.child("invoices").child(cardETxt.getText().toString()).child(time).child("email").setValue(mailETxt.getText().toString());
                myRef.child("invoices").child(cardETxt.getText().toString()).child(time).child("employee").setValue(userName);
                System.out.println(inv.toString());//TODO toplam ekle
            }
        });


        cListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cProductFromFB.remove(position);
                total-= Float.parseFloat(cPriceFromFB.get(position).toString());
                totalETxt.setText(String.valueOf(total));
                cPriceFromFB.remove(position);
                cQuantityFromFB.remove(position);
                adapter2.notifyDataSetChanged();
                listInv.remove(position);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),productFromFB.get(position),Toast.LENGTH_SHORT).show();
                if(!howMany.getText().toString().isEmpty()) {
                    it=productFromFB.get(position);
                    cProductFromFB.add(it);
                    Float calculate = Float.parseFloat(String.valueOf(priceFromFB.get(position))) * Float.parseFloat(howMany.getText().toString());
                    total+=calculate;
                    totalETxt.setText(String.valueOf(total));
                    cPriceFromFB.add(String.valueOf(calculate));
                    cQuantityFromFB.add(howMany.getText().toString());
                    adapter2.notifyDataSetChanged();
                    try {

                       // String time=DateFormat.getDateTimeInstance().format(new Date());
                        inv = new Invoice(productFromFB.get(position),calculate.toString(),howMany.getText().toString());

                        listInv.add(inv);
                        Log.d("EKLEME", "onItemClick: "+inv.name);
                        //myRef.child("invoices").child(userName).child(time).child(cProductFromFB.get(position)).setValue(inv);
                        //myRef.child("invoices").child(userName).child(formattedDate).child("total").setValue(total);
                        System.out.println("BAKSANA"+inv);//TODO toplam ekle
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
