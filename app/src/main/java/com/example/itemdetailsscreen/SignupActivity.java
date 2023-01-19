package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button loginBtn = (Button) findViewById(R.id.login_signupAct);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity (i);
            }
        });

        Button signupBtn = (Button) findViewById(R.id.signup_signupAct);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (CheckAllFields()) {
                    Toast.makeText(getApplicationContext(), "Trying to sign up!",
                            Toast.LENGTH_SHORT).show();
                // }
            }
        });
    }

//    private boolean CheckAllFields() {
//        if (emailEt.length() == 0) {
//            emailEt.setError("This field is required");
//            return false;
//        }
//        if (passwordEt.length() == 0) {
//            passwordEt.setError("This field is required");
//            return false;
//        }
//        return true;
//    }

}