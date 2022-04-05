package com.CaveEscape.myapplication.Info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.CaveEscape.myapplication.Lib.AudioPlayer;
import com.CaveEscape.myapplication.Lib.Navigation;
import com.CaveEscape.myapplication.Option.OptionActivity;
import com.CaveEscape.myapplication.R;
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

    int countNbTouch = 0;

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

        easterEggHugo.setOnClickListener(var -> ClickEasterEgg());

        easterEggNathan.setOnClickListener(var -> ClickEasterEgg());

        easterEggYoan.setOnClickListener(var -> ClickEasterEgg() );

        //-------------------------------------------------------------------- //

        Glide.with(this).load(R.drawable.creditback).into(background);
    }

    public void ClickEasterEgg()
    {
        countNbTouch++;

        if ( countNbTouch == 5 )
        {
            AudioPlayer.Play(this,R.raw.easter_egg_song);
            countNbTouch = 0;
            AudioPlayer.isAnEasterEgg = true;
        }
    }
}