package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewProductActivity extends Activity {
    private Button add;
    EditText name,price,quantity;

    final   FirebaseDatabase database = FirebaseDatabase.getInstance();
    final   DatabaseReference mDatabaseRef = database.getReference();

    private void writeNewUser(String name, String price, String quantity) {
        Product newItem = new Product(name, price,quantity);

        mDatabaseRef.child("products").child(name).setValue(newItem);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);




        name=findViewById(R.id.addNewProductNameETxt);
        price=findViewById(R.id.addNewProductPriceETxt);
        quantity=findViewById(R.id.addNewQuanlityNameETxt2);
        add=findViewById(R.id.addNewProductApplyBtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeNewUser(name.getText().toString(),price.getText().toString(),quantity.getText().toString());

            }
        });
    }
}
