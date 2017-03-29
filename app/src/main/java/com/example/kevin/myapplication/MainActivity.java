package com.example.kevin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void formSubmit(View v)
    {
        startActivity(new Intent(this,LoadDetails.class));
    }
    public void formSubmit2(View v)
    {
        startActivity(new Intent(this,UnloadDetails.class));
    }
}
