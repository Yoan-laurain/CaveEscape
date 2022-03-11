package com.example.myapplication.Info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Lib.AudioPlayer;
import com.example.myapplication.Lib.Navigation;
import com.example.myapplication.Option.OptionActivity;
import com.example.myapplication.R;

import java.util.HashMap;

public class InfoActivity extends AppCompatActivity
{
    Button button_return;
    ImageView background;
    HashMap params = new HashMap<>();
    TextView easterEggYoan;
    TextView easterEggHugo;
    TextView easterEggNathan;
    int coutNbTouchH = 0;
    int coutNbTouchN = 0;
    int coutNbTouchY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //---------------------- Tool selector -------------------------------- //

        background = findViewById(R.id.View_BackGround_Info);
        button_return = findViewById(R.id.button_info_return);
        easterEggYoan = findViewById(R.id.text_Info);
        easterEggHugo = findViewById(R.id.text_Info2);
        easterEggNathan = findViewById(R.id.text_Info3);



        //-------------------------------------------------------------------- //

        //---------------------- Set clicks actions -------------------------- //

        button_return.setOnClickListener(view -> Navigation.switchActivities(this, OptionActivity.class,params));
        easterEggHugo.setOnClickListener(var ->
        {
            coutNbTouchH++;
            coutNbTouchN = 0;
            coutNbTouchY = 0;
            if ( coutNbTouchH == 5 )
            {
                AudioPlayer.Play(this,R.raw.hugo_easter_egg);
                coutNbTouchH = 0;
            }

        });
        easterEggNathan.setOnClickListener(var ->
        {
            coutNbTouchN++;
            coutNbTouchH = 0;
            coutNbTouchY = 0;
            if ( coutNbTouchN == 5 )
            {
                AudioPlayer.Play(this,R.raw.nathan_esater_oeuf);
                coutNbTouchN = 0;
            }

        });

        //-------------------------------------------------------------------- //

        Glide.with(this).load(R.drawable.creditback).into(background);
    }
}