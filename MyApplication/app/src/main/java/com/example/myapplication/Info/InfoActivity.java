package com.example.myapplication.Info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.myapplication.Lib.Navigation;
import com.example.myapplication.Option.OptionActivity;
import com.example.myapplication.R;

import java.util.HashMap;

public class InfoActivity extends AppCompatActivity
{
    Button button_return;
    ImageView background;
    HashMap params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //---------------------- Tool selector -------------------------------- //

        background = findViewById(R.id.View_BackGround_Info);
        button_return = findViewById(R.id.button_info_return);

        //-------------------------------------------------------------------- //

        //---------------------- Set clicks actions -------------------------- //

        button_return.setOnClickListener(view -> Navigation.switchActivities(this, OptionActivity.class,params));

        //-------------------------------------------------------------------- //

        Glide.with(this).load(R.drawable.creditback).into(background);
    }
}