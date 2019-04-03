package com.example.moviemax;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.moviemax.MainActivity.loggedIn;
import static com.example.moviemax.MainActivity.loginEmail;
import static com.example.moviemax.MainActivity.loginPassword;

public class login extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
    private TextView email;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabaseHelper = new DatabaseHelper(this);

        email = findViewById(R.id.emailLoginBox);
        password = findViewById(R.id.passwordLoginBox);


    }

    public void onClickLogin(View v){
       if (mDatabaseHelper.checkIfLoggedIn(email.getText().toString(), password.getText().toString())) {
            Toast.makeText(this, "Logged in!", Toast.LENGTH_SHORT).show();
            loginEmail = email.getText().toString();
            loginPassword = password.getText().toString();
            loggedIn = true;
            finish();
        } else {
            Toast.makeText(this, "Wrong credentials", Toast.LENGTH_LONG).show();
        }
    }

}
