package com.example.testmapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void test2(View view){
        Intent intent = new Intent(getApplicationContext(), LocationsActivity.class);
        startActivity(intent);
    }

    public void test3(View view){
        Intent intent = new Intent(getApplicationContext(),MarkerActivity.class);
        startActivity(intent);
    }
}