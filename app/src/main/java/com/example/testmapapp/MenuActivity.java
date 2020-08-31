package com.example.testmapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {


    String tag;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            tag = bundle.getString("tag");
            name = bundle.getString("name");

        }

        setTitle(name);

    }

    public void menuPhotosTabbed(View view){
        Intent intent = new Intent(getApplicationContext(),ListPhotosActivity.class);
        intent.putExtra("tag",tag);
        intent.putExtra("name",name);
        startActivity(intent);
    }

    public void menuVideosTabbed(View view){
        Intent intent = new Intent(getApplicationContext(),ListVideosActivity.class);
        intent.putExtra("tag",tag);
        intent.putExtra("name",name);
        startActivity(intent);
    }
}