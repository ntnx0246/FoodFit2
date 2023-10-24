package com.example.foodfit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AdditionalResourcesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_resources);
        TextView textview = findViewById(R.id.additionalResourcesTextLink);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textview2 = findViewById(R.id.additionalResourcesTextLink2);
        textview2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textview3 = findViewById(R.id.additionalResourcesTextLink3);
        textview3.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textview4 = findViewById(R.id.additionalResourcesTextLink4);
        textview4.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textview5 = findViewById(R.id.additionalResourcesTextLink5);
        textview5.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textview6 = findViewById(R.id.additionalResourcesTextLink6);
        textview6.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textview7 = findViewById(R.id.additionalResourcesTextLink7);
        textview7.setMovementMethod(LinkMovementMethod.getInstance());
    }
}