package com.CaveEscape.myapplication.Option;

import android.support.v7.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import com.CaveEscape.myapplication.Info.InfoActivity;
import com.CaveEscape.myapplication.Lib.AudioPlayer;
import com.CaveEscape.myapplication.MainMenu.LoadingActivity;
import com.CaveEscape.myapplication.R;
import com.CaveEscape.myapplication.Lib.Navigation;

import java.security.Key;
import java.util.HashMap;

public class OptionActivity extends AppCompatActivity
{
    Button button_return;
    Button button_credit;
    HashMap<Key,Object> params = new HashMap<>();
    ImageView background;
    ImageView sound;
    SeekBar volume;
    int currentVolume = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        //---------------------- Tool selector -------------------------------- //

        background = findViewById(R.id.View_BackGround_Option);
        button_return = findViewById(R.id.button_option_return);
        button_credit = findViewById(R.id.button_option_credit);
        sound = findViewById(R.id.sound);
        volume = findViewById(R.id.volume_level);

        //-------------------------------------------------------------------- //

        //---------------------- Set clicks actions -------------------------- //

        button_return.setOnClickListener(view -> Navigation.switchActivities(this, LoadingActivity.class,params));
        button_credit.setOnClickListener(view -> Navigation.switchActivities(this, InfoActivity.class, params));

        sound.setOnClickListener(v -> {

            if ((Integer)v.getTag() == R.drawable.sound)
            {
                sound.setImageResource(R.drawable.sound_mute);
                sound.setTag(R.drawable.sound_mute);
                AudioPlayer.ChangeVolume(0);
                volume.setProgress( 0 );
            }
            else
            {
                sound.setImageResource(R.drawable.sound);
                sound.setTag(R.drawable.sound);
                AudioPlayer.ChangeVolume(currentVolume - 1);
                volume.setProgress( currentVolume - 1 );
            }
        });

        //-------------------------------------------------------------------- //

        Glide.with(this).load(R.drawable.optionback).into(background);

        volume.setMax(100);

        if ( AudioPlayer.getCurrent_volume() == 0 )
        {
            sound.setImageResource(R.drawable.sound_mute);
            sound.setTag(R.drawable.sound_mute);
        }
        else
        {
            sound.setImageResource(R.drawable.sound);
            sound.setTag(R.drawable.sound);
        }

        volume.setProgress( (int) AudioPlayer.getCurrent_volume() );
        currentVolume = volume.getProgress();


        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {

            // When Progress value changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser)
            {
                if ( progressValue != 0 )
                {
                    currentVolume = progressValue;
                    sound.setImageResource(R.drawable.sound);
                    sound.setTag(R.drawable.sound);
                }

                AudioPlayer.ChangeVolume(progressValue);
            }

            // Notification that the user has started a touch gesture.
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            // Notification that the user has finished a touch gesture
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

    }
}
