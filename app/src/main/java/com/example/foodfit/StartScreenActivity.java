package com.example.foodfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
    }

    public void loginEventRunner(View view){
        Intent i = new Intent(this, SignInPage.class);
        startActivity(i);
    }

    public void onClick(View view) {
        Intent i = new Intent(this, register.class);
        startActivity(i);
    }
}