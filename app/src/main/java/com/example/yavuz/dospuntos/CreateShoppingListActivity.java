package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateShoppingListActivity extends Activity {

    String it;
    String it2;
    String it3;
    TextView howMany,totalETxt;
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
    Button apply, back;
    ArrayList<Invoice> listInv;
    Invoice inv;
    float total=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shopping_list);
        Bundle extras = getIntent().getExtras();
        userName = extras.getString("username");
        howMany = (TextView) findViewById(R.id.createShoppingListHowManyETxt);
        listView = findViewById(R.id.createShoppingListProductListView);
        cListView = findViewById(R.id.createShoppingListChoosenListView);
        apply = findViewById(R.id.createShoppingListApplyBtn);
        back = findViewById(R.id.createShoppingListBackBtn);
        totalETxt = findViewById(R.id.createShoppingListTotallyTxt);

        productFromFB = new ArrayList<String>();
        priceFromFB = new ArrayList<String>();
        cProductFromFB = new ArrayList<String>();
        cPriceFromFB = new ArrayList<String>();
        cQuantityFromFB = new ArrayList<String>();

        listInv = new ArrayList<Invoice>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new AllPostClass(productFromFB,priceFromFB,this);
        adapter2 = new CreateClass(cProductFromFB,cQuantityFromFB,cPriceFromFB,this);
        cListView.setAdapter(adapter2);
        listView.setAdapter(adapter);


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
                Toast.makeText(getApplicationContext(),position+"-"+productFromFB.get(position),Toast.LENGTH_SHORT).show();
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
                        System.out.println("BAKSANA"+inv);
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

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateShoppingListActivity.this);
                final View mView = getLayoutInflater().inflate(R.layout.invoiceaccept,null);
                final Spinner cardSpin = (Spinner) mView.findViewById(R.id.invoiceAcceptCardSpinner);
                Button applyBtn = (Button) mView.findViewById(R.id.invoiceAcceptApplyBtn);
                Button backBtn = (Button) mView.findViewById(R.id.invoiceAcceptBackBtn);
                final CheckBox sale = (CheckBox) mView.findViewById(R.id.invoiceSaleCheck);

                final DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Array for cards info
                        List<String> cards = new ArrayList<String>();

                        for (DataSnapshot customers : dataSnapshot.child("customers").getChildren()) {

                            if(customers.getKey().equals(userName)){
                                Iterable<DataSnapshot> cardValue = customers.child("cards").getChildren();

                                for (DataSnapshot card : cardValue){
                                    float po = Float.parseFloat(card.getValue().toString());
                                    cards.add("Number:"+card.getKey()+"::Points:"+new DecimalFormat("##.##").format(po));
                                }
                                break;
                            }
                        }

                        ArrayAdapter<String> adapterIn = new ArrayAdapter<String>(CreateShoppingListActivity.this, android.R.layout.simple_list_item_1,cards);
                        cardSpin.setAdapter(adapterIn);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                applyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String time=DateFormat.getDateTimeInstance().format(new Date());
                        //Invoice inv=new Invoice(cProductFromFB,cPriceFromFB,cQuantityFromFB);
                        //myRef.child("invoices").child(userName).child(time).child(cProductFromFB).setValue(inv);

                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                boolean okay = false;

                                String [] cardsValues = cardSpin.getSelectedItem().toString().split(":");//Index 1 Number , index 4 points
                                for (DataSnapshot customers : dataSnapshot.child("customers").getChildren()) {
                                    if(customers.getKey().equals(userName)){
                                        float oldPoint = Float.parseFloat(cardsValues[4]);
                                        oldPoint = oldPoint * (float) 0.05;
                                        float newPoint = oldPoint - total;
                                        if(sale.isChecked()){
                                            if(newPoint<0){
                                                Toast.makeText(getApplicationContext(),"You don't have enough points. You have to pay more "+ (total - oldPoint) ,Toast.LENGTH_SHORT).show();
                                                mDatabase.child("customers").child(customers.getKey()).child("cards").child(String.valueOf(cardsValues[1])).setValue(0);
                                                Toast.makeText(getApplicationContext(),"Points added.",Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                newPoint = newPoint / (float) 0.05;
                                                float newP = newPoint + total;
                                                mDatabase.child("customers").child(customers.getKey()).child("cards").child(String.valueOf(cardsValues[1])).setValue(newP);
                                                Toast.makeText(getApplicationContext(),"Points added.",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{
                                            float newP = Float.parseFloat(cardsValues[4]) + total;
                                            mDatabase.child("customers").child(customers.getKey()).child("cards").child(String.valueOf(cardsValues[1])).setValue(newP);
                                            Toast.makeText(getApplicationContext(),"Points added.",Toast.LENGTH_SHORT).show();
                                        }
                                        okay = true;
                                    }
                                    if (okay == true)
                                        break;
                                }
                                if(okay){
                                    String time=DateFormat.getDateTimeInstance().format(new Date());
                                    for(int i=0;i<listInv.size();i++){
                                        myRef.child("invoices").child(cardsValues[1]).child(time).child(listInv.get(i).name).setValue(listInv.get(i));
                                        Log.d("APTAL", "onClick: "+listInv.get(i).name);
                                    }
                                    myRef.child("invoices").child(cardsValues[1]).child(time).child("total").setValue(total);

                                    for(int i=0;i<listInv.size();i++){
                                        for(DataSnapshot products : dataSnapshot.child("products").getChildren()){
                                            if (products.child("name").getValue().equals(listInv.get(i).name)){
                                                int newQuantity = Integer.parseInt(products.child("quantity").getValue().toString()) - Integer.parseInt(listInv.get(i).quantity);
                                                mDatabase.child("products").child(products.getKey()).child("quantity").setValue(newQuantity);
                                                break;
                                                //TODO Productlar 0 in altina inerse uyarsin ama satis gerceklesmesin
                                            }
                                        }

                                    }

                                    Toast.makeText(getApplicationContext(),"Succesfully.",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        finish();
                    }
                });

                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


            }
        });
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
