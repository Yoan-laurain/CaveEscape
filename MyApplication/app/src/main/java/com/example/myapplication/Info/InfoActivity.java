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
import java.security.Key;
import java.util.HashMap;

public class InfoActivity extends AppCompatActivity
{
    //-----------------------------------------

    Button button_return;
    ImageView background;
    TextView easterEggYoan;
    TextView easterEggHugo;
    TextView easterEggNathan;

    //-----------------------------------------

    HashMap<Key, Object> params = new HashMap<>();

    int countNbTouchH = 0;
    int countNbTouchN = 0;
    int countNbTouchY = 0;

    //-----------------------------------------

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
            countNbTouchH++;
            countNbTouchN = 0;
            countNbTouchY = 0;

            if ( countNbTouchH == 5 )
            {
                AudioPlayer.Play(this,R.raw.hugo_easter_egg);
                countNbTouchH = 0;
                AudioPlayer.isAnEasterEgg = true;
            }

        });

        easterEggNathan.setOnClickListener(var ->
        {
            countNbTouchN++;
            countNbTouchH = 0;
            countNbTouchY = 0;
            if ( countNbTouchN == 5 )
            {
                AudioPlayer.Play(this,R.raw.nathan_easter_oeuf);
                countNbTouchN = 0;
                AudioPlayer.isAnEasterEgg = true;
            }

        });

        easterEggYoan.setOnClickListener(var ->
        {
            countNbTouchY++;
            countNbTouchH = 0;
            countNbTouchN = 0;

            if ( countNbTouchY == 5 )
            {
                AudioPlayer.Play(this,R.raw.yoan_easter_oeuf);
                countNbTouchY = 0;
                AudioPlayer.isAnEasterEgg = true;
            }
        });

        //-------------------------------------------------------------------- //

        Glide.with(this).load(R.drawable.creditback).into(background);
    }
}