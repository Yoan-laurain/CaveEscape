package com.example.myapplication.Option;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.R;
import com.example.myapplication.Lib.Navigation;

import java.util.HashMap;

public class OptionActivity extends AppCompatActivity
{
    Button button_option;
    HashMap params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        button_option = findViewById(R.id.button_option_return);
        button_option.setOnClickListener(view -> Navigation.switchActivities(this, LoadingActivity.class,params));
    }

}
