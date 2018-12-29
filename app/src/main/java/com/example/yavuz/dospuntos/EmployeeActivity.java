package com.example.yavuz.dospuntos;

        import android.app.Activity;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

public class EmployeeActivity extends Activity {

    private Button Setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Setting = findViewById(R.id.employeeSettingsBtn);

        //Login
        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intocan = new Intent(EmployeeActivity.this, EmployeeSettingsActivity.class);
                startActivity(intocan);
                 }
            });
    }
}