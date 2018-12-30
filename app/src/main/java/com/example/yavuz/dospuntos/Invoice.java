package com.example.yavuz.dospuntos;

public class Invoice {

    public String name;
    public String price;
    public String quantity;

    public Invoice() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Invoice(String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
