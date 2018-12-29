package com.example.yavuz.dospuntos;

public class Customer {

    public String pw;
    public String fName;
    public String lName;
    public String email;
    public String address;


    public Customer() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Customer(String password,String fName,String lName,String email, String address) {
        this.pw = password;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.address = address;
    }

}
