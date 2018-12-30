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

public class AllPostClass extends ArrayAdapter<String> {

    private final ArrayList<String> products;
    private final ArrayList<String> prices;
    private final Activity context;

    public AllPostClass(ArrayList<String> products, ArrayList<String> prices, Activity context) {
        super(context,R.layout.all_view,products);
        this.products = products;
        this.prices = prices;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position,@Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View allview = layoutInflater.inflate(R.layout.all_view,null,true);

        TextView product = (TextView)allview.findViewById(R.id.productName);
        TextView price = (TextView)allview.findViewById(R.id.priceName);

        product.setText(products.get(position));
        String p=""+String.valueOf(prices.get(position));
        price.setText(p);

        return allview;
    }
}
