package com.example.itemdetailsscreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SigninActivity extends AppCompatActivity {
    EditText emailEt, passwordEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        emailEt = (EditText) findViewById(R.id.email);
        passwordEt = (EditText) findViewById(R.id.password);

        Button signupBtn = (Button) findViewById(R.id.signup_logAct);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent i = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity (i);
            }
        });

        Button loginBtn = (Button) findViewById(R.id.login_logAct);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckAllFields()) {
                    Toast.makeText(getApplicationContext(), "Trying to sign in!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean CheckAllFields() {
        if (emailEt.length() == 0) {
            emailEt.setError("This field is required");
            return false;
        }
        if (passwordEt.length() == 0) {
            passwordEt.setError("This field is required");
            return false;
        }
        return true;
    }

}