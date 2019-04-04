package com.example.moviemax;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Signup extends AppCompatActivity {

    private TextView name;
    private TextView lastName;
    private TextView age;
    private TextView email;
    private TextView password;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mDatabaseHelper = new DatabaseHelper(this);

        Button signUpBtn = findViewById(R.id.signUpBtn);

        name = findViewById(R.id.nameBox);
        lastName = findViewById(R.id.lastNameBox);
        age = findViewById(R.id.ageBox);
        email = findViewById(R.id.emailBox);
        password = findViewById(R.id.passwordBox);

    }

    public void onClickSingUp(View v){
        if (!(name.getText().toString().matches("")  || lastName.getText().toString().matches("")  || age.getText().toString().matches("")   || email.getText().toString().matches("")   || password.getText().toString().matches("")  )) {
            if (mDatabaseHelper.insertUser(name.getText().toString(), lastName.getText().toString(), Integer.parseInt(age.getText().toString()), email.getText().toString(), password.getText().toString())) {
                Toast.makeText(this, "Registered!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "You could not be registered", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Fill in all credientials", Toast.LENGTH_LONG).show();

        }
    }


}
