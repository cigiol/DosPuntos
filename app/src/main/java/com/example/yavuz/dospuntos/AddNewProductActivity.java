package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddNewProductActivity extends Activity {
    private Button add, back;
    EditText name,price,quantity;
    final Context context = this;

    final   FirebaseDatabase database = FirebaseDatabase.getInstance();
    final   DatabaseReference mDatabaseRef = database.getReference();

    private void writeNewUser(final String name, final String price, final String quantity) {


        ValueEventListener postlisten=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String N="",P="",Q="";
                if(dataSnapshot.child("products").child(name).child("name").exists())
                { N =dataSnapshot.child("products").child(name).child("name").getValue().toString();
                P=dataSnapshot.child("products").child(name).child("price").getValue().toString();
                Q=dataSnapshot.child("products").child(name).child("quantity").getValue().toString();
                int newq=0;
                newq=Integer.parseInt(Q);
                newq+=Integer.parseInt(quantity);
                Product newItem = new Product(name, price,String.valueOf(newq));
                mDatabaseRef.child("products").child(name).setValue(newItem);}
                else
                {
                    Product newItem1 = new Product(name, price,quantity);
                    mDatabaseRef.child("products").child(name).setValue(newItem1);
                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseRef.addListenerForSingleValueEvent(postlisten);



    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        final String[] Cname = new String[1];
        final String[] Cquantity = new String[1];
        name=findViewById(R.id.addNewProductNameETxt);
        price=findViewById(R.id.addNewProductPriceETxt);
        quantity=findViewById(R.id.addNewQuanlityNameETxt2);
        back=findViewById(R.id.addNewProductBackBtn);
        add=findViewById(R.id.addNewProductApplyBtn);






        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    writeNewUser(name.getText().toString(),price.getText().toString(),quantity.getText().toString());
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
