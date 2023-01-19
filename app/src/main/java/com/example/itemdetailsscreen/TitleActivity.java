package com.example.itemdetailsscreen;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import com.google.firebase.firestore.FirebaseFirestore;

        import java.util.HashMap;

public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        setListeners();
    }
    private void setListeners() {
        Button loginBtn = (Button) findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TitleActivity.this, SigninActivity.class);
                startActivity (i);
            }
        });
        Button signupBtn = (Button) findViewById(R.id.signup);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent i = new Intent(TitleActivity.this, SignupActivity.class);
                startActivity (i);
            }
        });
    }
}