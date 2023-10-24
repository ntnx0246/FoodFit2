package com.example.foodfit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kotlinx.coroutines.Job;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> goals;
    private ArrayAdapter<String> goalsAdapter;
    private ListView listView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(view);
            }
        });

        goals = new ArrayList<>();
        goalsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goals);
        listView.setAdapter(goalsAdapter);
        setUpListViewListener();
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = getApplicationContext();
                Toast.makeText(context, "Goal Completed! Great Job!", Toast.LENGTH_LONG).show();

                goals.remove(position);
                goalsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    private void addItem(View view){
        EditText input = findViewById(R.id.editTextText);
        String itemText = input.getText().toString();

        if(!(itemText.equals(""))){
            goalsAdapter.add(itemText);
            Toast.makeText(getApplicationContext(), "Goal Added", Toast.LENGTH_LONG).show();
            input.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(), "Please enter text...", Toast.LENGTH_LONG).show();
        }

    }

}