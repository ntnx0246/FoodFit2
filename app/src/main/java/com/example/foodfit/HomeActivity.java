package com.example.foodfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button button;
    TextView textView;
    FirebaseUser user;
Button apiButton;  // Declare the apiButton here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logoutButton);
        apiButton = findViewById(R.id.apiButton);  // Initialize the apiButton here
        textView = findViewById(R.id.welcomeText);
        user = mAuth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), SignInPage.class);
            startActivity(intent);
            finish();
        }
        else{
            textView.setText("Welcome " + user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), SignInPage.class);
                startActivity(intent);
                finish();
            }
        });

        // The code to transition to the API screen
        apiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent apiIntent = new Intent(HomeActivity.this, APIActivity.class);
                startActivity(apiIntent);
            }
        });

    }

    public void goalEventRunner(View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void benefitsEventRunner(View view){
        Intent i = new Intent(this, BenefitsActivity.class);
        startActivity(i);
    }

    public void recipesEventRunner(View view){
        Intent i = new Intent(this, RecipesActivity.class);
        startActivity(i);
    }

    public void creditsEventRunner(View view){
        Intent i = new Intent(this, CreditsActivity.class);
        startActivity(i);
    }

    public void additionalResourcesEventRunner(View view){
        Intent i = new Intent(this, AdditionalResourcesActivity.class);
        startActivity(i);
    }

}