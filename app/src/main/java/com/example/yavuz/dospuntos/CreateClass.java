package com.example.yavuz.dospuntos;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CreateClass extends ArrayAdapter<String> {
    private final ArrayList<String> products;
    private final ArrayList<String> quantities;
    private final ArrayList<String> prices;
    private final Activity context;

    public CreateClass(ArrayList<String> products, ArrayList<String> quantities, ArrayList<String> prices, Activity context) {
        super(context,R.layout.create_view,products);
        this.products = products;
        this.quantities = quantities;
        this.prices = prices;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View createView = layoutInflater.inflate(R.layout.create_view,null,true);

        TextView product = (TextView)createView.findViewById(R.id.createProduct);
        TextView price = (TextView)createView.findViewById(R.id.createPrice);
        TextView quantity = (TextView)createView.findViewById(R.id.createQuantity);

        product.setText(products.get(position));
        String p=""+String.valueOf(prices.get(position));
        price.setText(p);
        String r=""+String.valueOf(quantities.get(position));
        quantity.setText(r);

        return createView;
    }
}
