package com.example.yavuz.dospuntos;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class employee {

    public String username;
    public String pw;
    public String fn;
    public String ln;

    public employee() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public employee(String username,String pw,String fn,String ln) {
        this.username = username;
        this.pw = pw;
        this.fn = fn;
        this.ln = ln;
    }

}
