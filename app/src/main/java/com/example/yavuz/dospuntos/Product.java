package com.example.yavuz.dospuntos;

public class Product {

        public String name;
        public String price;
        public  String quantity;

        public Product() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public Product(String name, String price,String quantity) {
            this.name = name;
            this.price = price;
            this.quantity=quantity;
        }

    }

