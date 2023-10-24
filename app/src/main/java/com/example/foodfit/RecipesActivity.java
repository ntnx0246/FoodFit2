package com.example.foodfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RecipesActivity extends AppCompatActivity {

    ImageButton meatloaf;
    ImageButton turkey;
    ImageButton creamyRice;
    ImageButton pepper;
    ImageButton salad;
    ImageButton bananaBread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        meatloaf = findViewById(R.id.meatLoafButton);
        turkey = findViewById(R.id.turkeyButton);
        creamyRice = findViewById(R.id.creamyRiceButton);
        pepper = findViewById(R.id.pepperButton);
        salad = findViewById(R.id.saladButton);
        bananaBread = findViewById(R.id.bananaButton);


        meatloaf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.allrecipes.com/recipe/240747/mommas-healthy-meatloaf/");
            }
        });

        turkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.allrecipes.com/recipe/31813/marinated-turkey-breast/");
            }
        });

        creamyRice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.allrecipes.com/recipe/219442/healthier-creamy-rice-pudding/");
            }
        });

        pepper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.allrecipes.com/recipe/219447/healthier-stuffed-peppers/");
            }
        });

        salad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.allrecipes.com/recipe/276599/healthier-mediterranean-tuna-salad/");
            }
        });

        bananaBread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.allrecipes.com/article/healthy-snacks-banana-bread/");
            }
        });



    }
            private void gotoUrl(String s){
                Uri uri = Uri.parse(s);
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }

}